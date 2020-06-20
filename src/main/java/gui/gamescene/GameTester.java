package gui.gamescene;


import gui.gamescene.aiinterface.IActionPrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.Card.Suit;
import gui.gamescene.gamestate.GameState;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class GameTester implements ISolitaireAI, ISolitaireCV, ConsoleComponent.InputListener {

    private GameStateUpdateListener stateListener;
    private boolean isComputing = false;
    private ConsoleComponent console = new ConsoleComponent();
    private String computingInput = null;

    private static GameState physicalGameState;

    static{
        physicalGameState = GameState.randomStartingState();
        GameController.setInitialStock(physicalGameState.getStock());
    }


    public GameTester(){
        Stage stage = new Stage();
        stage.setTitle("Game Tester");

        StackPane container = new StackPane();

        stage.setScene(new Scene(container, 450, 450));
        stage.show();

        console.registerInputCommand("update", this::updateGameState);
        console.setInputListener(this);
        container.getChildren().add(console.getNode());
    }

    @Override
    public void computeAction(GameState gameState, IActionPrompter prompter) throws IllegalGameStateException {

    }

    @Override
    public void startActionComputation(GameState gameState) throws IllegalGameStateException {
        isComputing = true;
        computingInput = "";
        console.printInfo("Computing initialized");
    }

    @Override
    public void endActionComputation(IActionPrompter prompter) {
        console.printInfo("Computing ended");
        Token[] tokens = Token.fromString(computingInput);

        if(tokens[0].hasPrefix("s")){
            if(tokens[1].hasPrefix("t")){
                prompter.promptStockToTableau(tokens[0].value, tokens[1].value);
            }
        }

        if(tokens[0].hasPrefix("t")){
            if(tokens[1].hasPrefix("t")){
                prompter.promptTableauToTableau(tokens[0].value, tokens[1].value);
            }
        }

        if(tokens[0].hasPrefix("s")){
            if(tokens[1].hasPrefix("f")){
                prompter.promptStockToFoundation(tokens[0].value, tokens[1].value);
            }
        }

        if(tokens[0].hasPrefix("t")){
            if(tokens[1].hasPrefix("f")){
                prompter.promptTableauToFoundation(tokens[0].value, tokens[1].value);
            }
        }

        updateGameState();
    }

    @Override
    public void start() { }


    private void updateGameState(){
        if( computingInput != null ){
            Token[] tokens = Token.fromString(computingInput);
            if(tokens[0].hasPrefix("s")){
                if(tokens[1].hasPrefix("t")){
                    Card card = physicalGameState.getStock().remove(tokens[0].value);
                    physicalGameState.addToTableau(tokens[1].value, card);
                }
            }

            if(tokens[0].hasPrefix("t")){
                if(tokens[1].hasPrefix("t")){
                    List<Card> tableau = physicalGameState.getTableau(tokens[0].value);
                    Card card = tableau.remove(tableau.size()-1);
                    physicalGameState.addToTableau(tokens[1].value, card);
                }
            }

            if(tokens[0].hasPrefix("s")){
                if(tokens[1].hasPrefix("f")){
                    Card card = physicalGameState.getStock().remove(tokens[0].value);
                    physicalGameState.addToFoundations(tokens[1].value, card);
                }
            }

            if(tokens[0].hasPrefix("t")){
                if(tokens[1].hasPrefix("f")){
                    List<Card> tableau = physicalGameState.getTableau(tokens[0].value);
                    Card card = tableau.remove(tableau.size()-1);
                    physicalGameState.addToFoundations(tokens[1].value, card);
                }
            }

            // Turn over unknown cards in tableau
            for( List<Card> tableau : physicalGameState.getTableaus() ){
                if( tableau.size() > 0 ) {
                    Card card = tableau.get(tableau.size() - 1);
                    if (card.isUnknown()) {
                        Card missingCard = physicalGameState.getMissingCard();
                        card.setSuit(missingCard.getSuit());
                        card.setValue(missingCard.getValue());
                    }
                }
            }
            computingInput = null;
        }



        // Copy to a "detected" game state
        GameState detectedGameState = new GameState();
        for(int i=0; i<7; i++){
            for( Card card : physicalGameState.getTableau(i) )
                if( !card.isUnknown() ) detectedGameState.addToTableau(i, card.copy());
        }
        for(int i=0; i<4; i++){
            List<Card> foundation = physicalGameState.getFoundations().get(i);
            if( foundation.size() > 0 ) detectedGameState.addToFoundations(i, foundation.get(foundation.size()-1));
        }

        System.out.println(detectedGameState);

        stateListener.onGameStateUpdate(detectedGameState);
    }

    @Override
    public void pause() {

    }

    @Override
    public void unpause() {

    }

    @Override
    public void setImageUpdateListener(ImageUpdateListener imageUpdateListener) {

    }

    @Override
    public void setGameStateUpdateListener(GameStateUpdateListener gameStateUpdateListener) {
        this.stateListener = gameStateUpdateListener;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {

    }

    @Override
    public void onConsoleInput(String input, boolean wasCommand) {
        System.out.printf("New input: '%s' (command: %s)\n", input, wasCommand);
        if( !wasCommand ){
            if( !isComputing ){
                console.printError("Not currently computing");
                return;
            }

            computingInput = input;
        }
    }


    static class Token{
        String prefix;
        int value;

        public Token(String prefix, int value) {
            this.prefix = prefix;
            this.value = value;
        }

        public boolean hasPrefix(String prefix){
            return this.prefix.equals(prefix);
        }

        public static Token[] fromString(String input){
            List<Token> tokens = new ArrayList<>();
            StringTokenizer defaultTokenizer = new StringTokenizer(input);
            while( defaultTokenizer.hasMoreTokens() ){
                String tokenString = defaultTokenizer.nextToken();
                tokens.add(new Token(tokenString.substring(0,1), Integer.parseInt(tokenString.substring(1))));
            }
            return tokens.toArray(new Token[]{});
        }
    }

    private static GameState createGameState(int version){
        GameState state = new GameState();

        switch(version){
            case 1:
                state.addToTableau(0, new Card(Suit.CLUBS, 2));
                state.addToTableau(1, new Card(Suit.CLUBS, 1));
                state.addToTableau(2, new Card(Suit.HEARTS, 2));
                state.addToTableau(3, new Card(Suit.SPADES, 3));



               /* stock.set(0, new Card(Card.Suit.HEARTS,  1));
                stock.set(1, new Card(Card.Suit.SPADES,  6));
                stock.set(2, new Card(Card.Suit.DIAMONDS, 13));
                stock.set(3, new Card(Card.Suit.HEARTS, 9));
                stock.set(4, new Card(Card.Suit.CLUBS, 11));
                stock.set(5, new Card(Card.Suit.SPADES, 2));
                stock.set(6, new Card(Card.Suit.SPADES, 4));
                stock.set(7, new Card(Card.Suit.DIAMONDS, 5));
                stock.set(8, new Card(Card.Suit.DIAMONDS, 7));
                stock.set(9, new Card(Card.Suit.HEARTS, 8));
                stock.set(10, new Card(Card.Suit.CLUBS, 6));
                stock.set(11, new Card(Card.Suit.SPADES, 13));
                stock.set(12, new Card(Card.Suit.CLUBS, 7));
                stock.set(13, new Card(Card.Suit.DIAMONDS, 1));
                stock.set(14, new Card(Card.Suit.HEARTS, 7));
                stock.set(15, new Card(Card.Suit.HEARTS, 4));
                stock.set(16, new Card(Card.Suit.CLUBS, 10));
                stock.set(17, new Card(Card.Suit.CLUBS, 12));
                stock.set(18, new Card(Card.Suit.CLUBS, 3));
                stock.set(19, new Card(Card.Suit.SPADES, 1));
                stock.set(20, new Card(Card.Suit.HEARTS, 5));
                stock.set(21, new Card(Card.Suit.DIAMONDS, 8));
                stock.set(22, new Card(Card.Suit.HEARTS, 11));
                stock.set(23, new Card(Card.Suit.SPADES, 10));*/
                break;

            default:
                throw new RuntimeException("Wrong game state version");
        }

        return state;
    }
}
