package gui.gamescene;

import ai.agent.MCTSGuiAgent;
import ai.heuristic.Cocktail;
import ai.heuristic.Heuristic;
import cv.SolitaireCV;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import javafx.scene.paint.Paint;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;


class GameController {

    private GameScene scene;
    private GameState lastGameState; // The most recent game state received from CV (not the most recent activated one though)
    private GameState detectedGameState;
    private GameState newGameState = null;
    private GameState currentGameState = setupInitialGameState();

    private boolean firstGameState = true;

    private IConsole console = null;
    private Heuristic heuristic = new Cocktail(1,1,1,1,1,1,1,1,1);
    private ISolitaireAI ai = new MCTSGuiAgent(-1,heuristic);
    private ISolitaireCV cv = new SolitaireCV();

    private boolean computationRunning = false;
    private boolean detectionRunning = false;

    // TODO: Remove this
    private GameTester tester;


    GameController(GameScene scene) {
        this.scene = scene;
        console = scene.getConsole();

        tester = new GameTester();
        cv = tester;
        ai = tester;

        cv.setImageUpdateListener( (newImage) -> scene.getCameraComponent().updateImage(newImage) );
        cv.setErrorListener(err -> scene.getCameraComponent().showError(err) );
        cv.setGameStateUpdateListener(this::onDetection);
        cv.start();

        scene.getGameComponent().updateGameState(currentGameState);

        scene.getCameraComponent().startLoading("Starting computer vision...");

        // Apply initial stock if set
        if( initialStock != null ) currentGameState.setStock(initialStock);

        registerInputCommands();
    }


    // Sets the initial stock to be used by used, to prevent from having to
    // traverse the stock when starting the game
    private static List<Card> initialStock = null;
    public static void setInitialStock(List<Card> stock){
        if( stock.size() != 24 )
            throw new IllegalArgumentException("Stock must be of size 24");
        List<Card> stockCopy = new LinkedList<>();
        for( Card card : stock ) stockCopy.add(card.copy());
        initialStock = stockCopy;
    }

    // TODO: Comment this in if you want to use a pre-defined stock
    static{ setInitialStock(createTestStock()); }


    // For testing purposes
    private GameController() { }

    public GameState getCurrentGameState(){
        return currentGameState;
    }


    private void registerInputCommands() {
        console.registerInputCommand("detect", this::startDetection);
        console.registerInputCommand("stop", this::stopWork);
        console.registerInputCommand("compute", this::compute);
    }


    private void startDetection() {
        console.printInfo("Starting computervision detection (write 'stop' to lock detection)");
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


    // Stops both computervision, and AI
    private void stopWork()  {
        if( detectionRunning ) {
            detectionRunning = false;
            console.printInfo("Stopped detection");
        }
        if( computationRunning ){
            computationRunning = false;
            console.printInfo("Stopping computation of best move");
            ai.endActionComputation(scene.getPrompter());
        }
    }


    private void compute(){
        stopWork();
        firstGameState = false;
        currentGameState = newGameState;

        // Update the stock cards
        if( currentGameState.getStock().contains(Card.createUnknown()) ){
            updateStock();
        }


        // Run the computation if game state is ready
        if( !currentGameState.getStock().contains(Card.createUnknown()) ){
            updateGameState();
            // Run computation
            computeNextAction(currentGameState);
            console.printInfo("Computing the best move (write 'stop' to get result)");
        }
    }


    // Creates a game state with all the unknown cards (tableau and stock)
    private static GameState setupInitialGameState(){
        GameState state = new GameState();
        for(int i=0; i<24; i++){
            state.addToStock(Card.createUnknown());
        }
        for(int i=0; i<7; i++){
            for(int j=0; j<i; j++){
                state.addToTableau(i, Card.createUnknown());
            }
        }
        return state;
    }


    private void updateStock(){
        if( detectedGameState.getFlipped().size() == 0){
            int unknownCount = 0;
            for( Card card : currentGameState.getStock() )
                if( card.isUnknown() ) unknownCount++;

            console.printError("Still need to detect " + unknownCount + " cards, but none are drawn from the stock");
            return;
        }

        if( compareCardLists(currentGameState.getStock(), detectedGameState.getFlipped()) ){
            console.printError("You have drawn no new cards. Draw 3 new cards from the stock!");
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
        console.printInfo("Added " + addedCards.size() + " new cards to the stock: " + cardLabels);
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
        new Thread(() -> {
            try{
                computationRunning = true;
                ai.startActionComputation(state);
            }catch(ISolitaireAI.IllegalGameStateException e){
                computationRunning = false;
                e.printStackTrace();
                System.out.println(state);
            }
        }).start();
    }





    private boolean compareCardLists(List<Card> list1, List<Card> list2){
        if( list1.size() != list2.size()) return false;
        for( int i=0; i<list1.size(); i++)
            if( !list1.get(i).equals(list2.get(i))) return false;
        return true;
    }


    public static void main(String[] args) {
        List<GameState> testStates = createTestStates();
        GameController gameController = new GameController();
        gameController.console = new TestConsole();
        gameController.setupInitialGameState();
        System.out.println(gameController.currentGameState);

        // Update flipped
        System.out.println(testStates.get(1));
        gameController.detectedGameState = testStates.get(1);
        gameController.updateStock();

        // Update flipped
        System.out.println(testStates.get(2));
        gameController.detectedGameState = testStates.get(2);
        gameController.updateStock();

        // Update tableaus
        gameController.detectedGameState = testStates.get(0);
        gameController.updateGameState();
        gameController.currentGameState = gameController.newGameState;
        gameController.firstGameState = false;

        // Update
        gameController.detectedGameState = testStates.get(3);
        gameController.updateGameState();
        gameController.currentGameState = gameController.newGameState;

        // Update
        gameController.detectedGameState = testStates.get(4);
        gameController.updateGameState();
        gameController.currentGameState = gameController.newGameState;

        // Update
        gameController.detectedGameState = testStates.get(5);
        gameController.updateGameState();
        gameController.currentGameState = gameController.newGameState;

        // Update
        gameController.detectedGameState = testStates.get(6);
        gameController.updateGameState();
        gameController.currentGameState = gameController.newGameState;


        System.out.println(gameController.currentGameState);
    }



    private static List<GameState> createTestStates(){
        List<GameState> states = new LinkedList<>();

        GameState state;

        state = new GameState();
        state.addToTableau(0, new Card(Card.Suit.HEARTS, 3));
        state.addToTableau(1, new Card(Card.Suit.SPADES, 4));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 5));
        state.addToTableau(3, new Card(Card.Suit.DIAMONDS, 12));
        state.addToTableau(4, new Card(Card.Suit.CLUBS, 13));
        state.addToTableau(5, new Card(Card.Suit.CLUBS, 9));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 2));
        states.add(state);

        state = new GameState();
        state.addToFlipped(new Card(Card.Suit.CLUBS, 2));
        state.addToFlipped(new Card(Card.Suit.SPADES, 4));
        state.addToFlipped(new Card(Card.Suit.HEARTS, 1));
        states.add(state);


        state = new GameState();
        state.addToFlipped(new Card(Card.Suit.HEARTS, 1));
        state.addToFlipped(new Card(Card.Suit.SPADES, 2));
        state.addToFlipped(new Card(Card.Suit.HEARTS, 5));
        states.add(state);

        state = new GameState();
        state.addToTableau(0, new Card(Card.Suit.HEARTS, 3));
        state.addToTableau(1, new Card(Card.Suit.SPADES, 1));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 5));
        state.addToTableau(2, new Card(Card.Suit.SPADES, 4));
        state.addToTableau(3, new Card(Card.Suit.DIAMONDS, 12));
        state.addToTableau(4, new Card(Card.Suit.CLUBS, 13));
        state.addToTableau(5, new Card(Card.Suit.CLUBS, 9));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 2));
        states.add(state);

        state = new GameState();
        state.addToTableau(0, new Card(Card.Suit.HEARTS, 3));
        state.addToTableau(1, new Card(Card.Suit.SPADES, 1));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 5));
        state.addToTableau(2, new Card(Card.Suit.SPADES, 4));
        state.addToTableau(3, new Card(Card.Suit.DIAMONDS, 12));
        state.addToTableau(4, new Card(Card.Suit.CLUBS, 13));
        state.addToTableau(5, new Card(Card.Suit.CLUBS, 9));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 2));
        state.addToTableau(6, new Card(Card.Suit.HEARTS, 1));
        states.add(state);

        state = new GameState();
        state.addToTableau(1, new Card(Card.Suit.SPADES, 1));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 5));
        state.addToTableau(2, new Card(Card.Suit.SPADES, 4));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 3));
        state.addToTableau(3, new Card(Card.Suit.DIAMONDS, 12));
        state.addToTableau(4, new Card(Card.Suit.CLUBS, 13));
        state.addToTableau(5, new Card(Card.Suit.CLUBS, 9));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 2));
        state.addToTableau(6, new Card(Card.Suit.HEARTS, 1));
        states.add(state);

        state = new GameState();
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 5));
        state.addToTableau(2, new Card(Card.Suit.SPADES, 4));
        state.addToTableau(2, new Card(Card.Suit.HEARTS, 3));
        state.addToTableau(3, new Card(Card.Suit.DIAMONDS, 12));
        state.addToTableau(4, new Card(Card.Suit.CLUBS, 13));
        state.addToTableau(5, new Card(Card.Suit.CLUBS, 9));
        state.addToTableau(6, new Card(Card.Suit.SPADES, 2));
        state.addToTableau(6, new Card(Card.Suit.HEARTS, 1));
        state.addToFoundations(0, new Card(Card.Suit.SPADES, 1));
        states.add(state);

        return states;
    }


    private static class TestConsole implements IConsole {

        @Override
        public void print(String msg, Paint color, boolean bold) {

        }

        @Override
        public void printError(String msg) {
            System.out.println("Error: " + msg);
        }

        @Override
        public void printInfo(String msg) {
            System.out.println("Info: " + msg);
        }

        @Override
        public void registerInputCommand(String input, InputCommand action) {

        }
    }


    private static List<Card> createTestStock(){
        List<Card> stock = new LinkedList<>();
        stock.add(new Card(Card.Suit.HEARTS,  1));
        stock.add(new Card(Card.Suit.SPADES,  6));
        stock.add(new Card(Card.Suit.DIAMONDS, 13));
        stock.add(new Card(Card.Suit.HEARTS, 9));
        stock.add(new Card(Card.Suit.CLUBS, 11));
        stock.add(new Card(Card.Suit.SPADES, 2));
        stock.add(new Card(Card.Suit.SPADES, 4));
        stock.add(new Card(Card.Suit.DIAMONDS, 5));
        stock.add(new Card(Card.Suit.DIAMONDS, 7));
        stock.add(new Card(Card.Suit.HEARTS, 8));
        stock.add(new Card(Card.Suit.CLUBS, 6));
        stock.add(new Card(Card.Suit.SPADES, 13));
        stock.add(new Card(Card.Suit.CLUBS, 7));
        stock.add(new Card(Card.Suit.DIAMONDS, 1));
        stock.add(new Card(Card.Suit.HEARTS, 7));
        stock.add(new Card(Card.Suit.HEARTS, 4));
        stock.add(new Card(Card.Suit.CLUBS, 10));
        stock.add(new Card(Card.Suit.CLUBS, 12));
        stock.add(new Card(Card.Suit.CLUBS, 3));
        stock.add(new Card(Card.Suit.SPADES, 1));
        stock.add(new Card(Card.Suit.HEARTS, 5));
        stock.add(new Card(Card.Suit.DIAMONDS, 8));
        stock.add(new Card(Card.Suit.HEARTS, 11));
        stock.add(new Card(Card.Suit.SPADES, 10));
        return stock;
    }
}
