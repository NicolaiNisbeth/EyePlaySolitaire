package gui.gamescene;

import ai.agent.MCTSGuiAgent;
import ai.heuristic.Cocktail;

import cv.SolitaireCV;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.aiinterface.ManualAI;
import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.cvinterface.ManualCV;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;


class GameController {

    private GameScene scene;
    private GameState lastGameState; // The most recent game state received from CV (not the most recent activated one though)
    private GameState detectedGameState;
    private GameState newGameState = null;
    private GameState currentGameState = GameState.randomStartingState(true);

    private boolean firstGameState = true;

    //private IConsole console = null;
    private ISolitaireAI ai;
    private ISolitaireCV cv;

    private boolean computationRunning = false;
    private boolean detectionRunning = false;

    private static List<Card> predefinedStock;


    GameController(GameScene scene, boolean manualAI, boolean manualCV, boolean usePredefinedStock) {
        this.scene = scene;

        // Setup control component
        scene.getControlComponent().onDetectStarted(this::startDetection);
        scene.getControlComponent().onComputeStarted(this::compute);
        scene.getControlComponent().onComputeStopped(this::stopCompute);



        scene.getControlComponent().disableButtons(true);

        // Remove top cards from game state
        for( List<Card> tableau : currentGameState.getTableaus() )
            tableau.remove(tableau.size()-1);

        cv = manualCV ? new ManualCV() : new SolitaireCV();
        ai = manualAI ? new ManualAI() : new MCTSGuiAgent(-1,heuristic);

        cv.setImageUpdateListener( (newImage) -> scene.getCameraComponent().updateImage(newImage) );
        cv.setErrorListener(err -> scene.getCameraComponent().showError(err) );
        cv.setGameStateUpdateListener(this::onDetection);



        // Setup initial stock
        List<Card> stock;
        if( manualCV ){
            stock = ((ManualCV) cv).getPredefinedStock();
        }else if( usePredefinedStock ){
            stock = predefinedStock;
        }else{
            stock = new LinkedList<>();
            for(int i=0; i<24; i++) stock.add(Card.createUnknown());
        }
        currentGameState.setStock(stock);

        //currentGameState = customStartingState();
        //firstGameState = false;

        // Intialize game and camera component
        scene.getGameComponent().updateGameState(currentGameState);
        scene.getCameraComponent().startLoading("Starting computer vision...");

        // Initialize computer vision
        cv.initialize(() -> {
            scene.getCameraComponent().showMessage("Computer vision is ready!");
            scene.getControlComponent().disableButtons(false);
        });
    }


    // Default constructor for testing purposes
    private GameController() { }

    public GameState getCurrentGameState(){
        return currentGameState;
    }

    private void startDetection() {
        // console.printInfo("Starting computervision detection (write 'stop' to lock detection)");
        cv.start();
        detectedGameState = lastGameState;
        if( detectedGameState != null )
            updateGameState();
        detectionRunning = true;
    }


    private void onDetection(GameState gameState) {
        lastGameState = gameState;
        if( detectionRunning ) {
            detectedGameState = lastGameState;
            updateGameState();
        }
    }


    private void stopCompute(){
        if( computationRunning ){
            computationRunning = false;
            ai.endActionComputation(scene.getPrompter());
        }
    }


    private void compute(){
        cv.pause();
        if( newGameState == null ){
            scene.getControlComponent().displayMessage("No state has been detected yet!");
        }else{
            detectionRunning = false;
            firstGameState = false;
            currentGameState = newGameState;

            // Update the stock cards
            if( currentGameState.getStock().contains(Card.createUnknown()) ){
                updateStock();
            }

            boolean gameWon = true;
            for( List<Card> foundation : currentGameState.getFoundations() ){
                if( foundation.size() != 13 ) gameWon = false;
            }
            if( gameWon ){
                scene.getPrompter().gameWon();
            }else{
                // Run the computation if game state is ready
                if( !currentGameState.getStock().contains(Card.createUnknown()) ){
                    updateGameState();
                    // Run computation
                    computeNextAction(currentGameState);
                }else{
                    computationRunning = false;
                }
            }
        }

    }


    private void updateStock(){
        if( detectedGameState.getFlipped().size() == 0){
            int unknownCount = 0;
            for( Card card : currentGameState.getStock() )
                if( card.isUnknown() ) unknownCount++;
            scene.getControlComponent().displayMessage("Still need to detect " + unknownCount + " cards, but none are drawn from the stock");
            return;
        }

        if( compareCardLists(currentGameState.getStock(), detectedGameState.getFlipped()) ){
            scene.getControlComponent().displayMessage("You have drawn no new cards. Draw 3 new cards from the stock!");
            return;
        }

        List<Card> addedCards = new LinkedList<>();
        for(Card card : detectedGameState.getFlipped()){
            if( !currentGameState.getStock().contains(card) ){
                for( Card existingCard : currentGameState.getStock() ){
                    if( existingCard.equals(Card.createUnknown()) ){
                        existingCard.setValue(card.getValue());
                        existingCard.setSuit(card.getSuit());
                        break;
                    }
                }
            }
            addedCards.add(card);
        }

        // Print info to user
        String cardLabels = addedCards.stream().map(Card::toStringShort).collect(joining(", ", "", ""));
        scene.getControlComponent().displayMessage("Added " + addedCards.size() + " new cards to the stock: " + cardLabels);
    }


    private void updateGameState(){
        newGameState = new GameState();

        // Copy the stock
        for (Card card : currentGameState.getStock() )
            newGameState.addToStock(card);

        // Setup the tableaus
        for(int i=0; i<7; i++){
            List<Card> newTableau = newGameState.getTableaus().get(i);

            int unknownCount = 0;
            for(Card card : currentGameState.getTableaus().get(i) )
                if( card.equals(Card.createUnknown())) {
                    newTableau.add(Card.createUnknown());
                    unknownCount++;
                }

            for(Card card : detectedGameState.getTableaus().get(i) ) {
                if (!firstGameState && isNewCard(currentGameState, card)) {
                    if (newGameState.getStock().contains(card)) {
                        newGameState.getStock().remove(card);
                    } else {
                        if (unknownCount == 0) {
                            System.out.printf("WARNING: Card %s is detected as a newly flipped card in tableau %d, but there were no unknown cards before\n", card, i);
                        } else if (unknownCount != newTableau.size()) {
                            System.out.printf("WARNING: Card %s is detected as a newly flipped card in tableau %d, but it's not the topmost card\n", card, i);
                        } else {
                            newTableau.remove(newTableau.get(0));
                        }
                    }
                }
                newTableau.add(card);
            }
        }

        // Copy drawn cards
        for( Card card : detectedGameState.getFlipped() ){
            newGameState.addToFlipped(card);
        }

        // Copy the foundation
        for( int i=0; i<4; i++) {
            List<Card> foundation = detectedGameState.getFoundations().get(i);
            if( foundation.size() > 0 ){
                if( foundation.size() > 1 ){
                    System.out.printf("WARNING: Foundation %d of detected game state is larger than 1 - %d to be exact (how's that possible)?", i, foundation.size() );
                }

                Card card = foundation.get(0);

                // Remove card from stock, if it was moved from there
                if (!firstGameState && isNewCard(currentGameState, card) && newGameState.getStock().contains(card) ){
                    newGameState.getStock().remove(card);
                }

                for (int j = 1; j <= card.getValue(); j++) {
                    newGameState.addToFoundations(i, new Card(card.getSuit(), j));
                }
            }
        }

        scene.getGameComponent().updateGameState(newGameState);
    }



    private boolean isNewCard(GameState gameState, Card card){
        if(gameState == null ) return false;
        for(List<Card> tableau : gameState.getTableaus() ){
            for( Card tableauCard : tableau )
                if( card.equals(tableauCard) ) return false;
        }
        return true;
    }


    /**
     * Sends an asynchronous computation request to the AI on a new non-UI thread.
     */
    private void computeNextAction(GameState state){
        Thread thread = new Thread(() -> {
            try{
                computationRunning = true;
                ai.startActionComputation(state);
            }catch(ISolitaireAI.IllegalGameStateException e){
                computationRunning = false;
                e.printStackTrace();
                System.out.println(state);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }





    private boolean compareCardLists(List<Card> list1, List<Card> list2){
        if( list1.size() != list2.size()) return false;
        for( int i=0; i<list1.size(); i++)
            if( !list1.get(i).equals(list2.get(i))) return false;
        return true;
    }



    private static GameState customStartingState(){
        GameState state = new GameState();

        for(int i=1; i<=13; i++){
            state.addToFoundations(0, new Card(Card.Suit.DIAMONDS, i));
        }

        for(int i=1; i<=12; i++){
            state.addToFoundations(1, new Card(Card.Suit.CLUBS, i));
        }

        for(int i=1; i<=11; i++){
            state.addToFoundations(2, new Card(Card.Suit.HEARTS, i));
        }

        for(int i=1; i<=11; i++){
            state.addToFoundations(3, new Card(Card.Suit.SPADES, i));
        }

        state.addToTableau(0, new Card(Card.Suit.SPADES, 13));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 12));

        state.addToTableau(3, new Card(Card.Suit.CLUBS, 13));

        state.addToTableau(4, new Card(Card.Suit.HEARTS, 13));
        state.addToTableau(0, new Card(Card.Suit.HEARTS, 12));

        return state;
    }


    // Setup the predefined stock
    static {
        predefinedStock = new LinkedList<>();
        predefinedStock.add(new Card(Card.Suit.HEARTS,  1));
        predefinedStock.add(new Card(Card.Suit.SPADES,  6));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 13));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 9));
        predefinedStock.add(new Card(Card.Suit.CLUBS, 11));
        predefinedStock.add(new Card(Card.Suit.SPADES, 2));
        predefinedStock.add(new Card(Card.Suit.SPADES, 4));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 5));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 7));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 8));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 6));
        predefinedStock.add(new Card(Card.Suit.SPADES, 13));
        predefinedStock.add(new Card(Card.Suit.CLUBS, 7));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 1));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 7));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 4));
        predefinedStock.add(new Card(Card.Suit.CLUBS, 10));
        predefinedStock.add(new Card(Card.Suit.CLUBS, 12));
        predefinedStock.add(new Card(Card.Suit.CLUBS, 3));
        predefinedStock.add(new Card(Card.Suit.SPADES, 1));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 5));
        predefinedStock.add(new Card(Card.Suit.DIAMONDS, 8));
        predefinedStock.add(new Card(Card.Suit.HEARTS, 11));
        predefinedStock.add(new Card(Card.Suit.SPADES, 10));
    }
}
