package gui.gamescene.cvinterface;


import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import gui.util.CommandToken;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;


public class ManualCV implements ISolitaireCV, ConsoleComponent.InputListener {

    private ConsoleComponent console = new ConsoleComponent();

    private GameState physicalGameState = GameState.randomStartingState(false);

    private GameStateUpdateListener stateUpdateListener;


    public List<Card> getPredefinedStock(){
        List<Card> stockCopy = new LinkedList<>();
        for( Card card : physicalGameState.getStock()) stockCopy.add(card.copy());
        return stockCopy;
    }


    @Override
    public void start() {
        Stage stage = new Stage();
        stage.setTitle("CV");
        StackPane container = new StackPane();
        container.getChildren().add(console.getNode());
        stage.setScene(new Scene(container, 225, 300));
        stage.show();
        console.setInputListener(this);
        console.printInfo("Manual Computer Vision Console\nType one of the available commands to move cards: 'tX tY', 'tX fY', 'sX tY' or 'sX fY', where X and Y are indexes of the tableau, foundation or stock (starting from 0)");
        sendGameState();
    }


    /**
     * Read input from the console, and performs some sort of movement of
     * the game state (i.e. move card from tableau 1 to 2).
     */
    @Override
    public void onConsoleInput(String input, boolean wasCommand) {
        if (!wasCommand) {

            CommandToken[] tokens = CommandToken.fromString(input);
            if (tokens[0].hasPrefix("s")) {
                if (tokens[1].hasPrefix("t")) {
                    Card card = physicalGameState.getStock().remove(tokens[0].value);
                    physicalGameState.addToTableau(tokens[1].value, card);
                }
            }

            // Tableau to tableau
            if (tokens[0].hasPrefix("t")) {
                if (tokens[1].hasPrefix("t")) {
                    List<Card> tableau = physicalGameState.getTableau(tokens[0].value);

                    // Get all cards to move (we move more than one, if they're a sequence
                    LinkedList<Card> cardsToMove = new LinkedList<>();
                    for( int i=tableau.size()-1; i >= 0; i-- ){
                        Card current = tableau.get(i);
                        Card previous = cardsToMove.size() == 0 ? null : cardsToMove.get(cardsToMove.size()-1);
                        if( previous == null ) {
                            cardsToMove.add(current);
                        }else if(current.getValue() == previous.getValue()+1 && current.getColor() != previous.getColor() ){
                            cardsToMove.addFirst(current);
                        }else{
                            break;
                        }
                    }

                    // Move the card / cards
                    int targetIndex = tokens[1].value;
                    List<Card> targetTableau = physicalGameState.getTableau(targetIndex);
                    for(Card cardToMove : cardsToMove ){
                        // Check if the sequence is legal
                        Card currentCard = targetTableau.size() == 0 ? null : targetTableau.get(targetTableau.size()-1);
                        if( currentCard != null && (currentCard.getColor() == cardToMove.getColor() || currentCard.getValue() != cardToMove.getValue()-1))
                            System.out.printf("WARNING: Moving card '%s' to card '%s' is not a matching sequence!\n", cardToMove, currentCard);
                        tableau.remove(cardToMove);
                        physicalGameState.addToTableau(tokens[1].value, cardToMove);
                    }
                }
            }

            if (tokens[0].hasPrefix("s")) {
                if (tokens[1].hasPrefix("f")) {
                    Card card = physicalGameState.getStock().remove(tokens[0].value);
                    physicalGameState.addToFoundations(tokens[1].value, card);
                }
            }

            if (tokens[0].hasPrefix("t")) {
                if (tokens[1].hasPrefix("f")) {
                    List<Card> tableau = physicalGameState.getTableau(tokens[0].value);
                    Card card = tableau.remove(tableau.size() - 1);
                    physicalGameState.addToFoundations(tokens[1].value, card);
                }
            }

            // Turn over unknown cards in tableau
            for (List<Card> tableau : physicalGameState.getTableaus()) {
                if (tableau.size() > 0) {
                    Card card = tableau.get(tableau.size() - 1);
                    if (card.isUnknown()) {
                        Card missingCard = physicalGameState.getMissingCard();
                        card.setSuit(missingCard.getSuit());
                        card.setValue(missingCard.getValue());
                    }
                }
            }

            sendGameState();
        }
    }


    /* Copy detected cards to a "detected" game state,
    *   and send the detected state to the update listener */
    private void sendGameState(){
        if(stateUpdateListener != null) {
            // Copy visible cards from tableaus
            GameState detectedGameState = new GameState();
            for(int i=0; i<7; i++){
                for( Card card : physicalGameState.getTableau(i) )
                    if( !card.isUnknown() ) detectedGameState.addToTableau(i, card.copy());
            }

            // Copy visible foundations
            for(int i=0; i<4; i++){
                List<Card> foundation = physicalGameState.getFoundations().get(i);
                if( foundation.size() > 0 ) detectedGameState.addToFoundations(i, foundation.get(foundation.size()-1));
            }

            // Note: we're not copying "flipped"/"drawn" cards
            stateUpdateListener.onGameStateUpdate(detectedGameState);
        }
    }





    @Override
    public void setGameStateUpdateListener(GameStateUpdateListener gameStateUpdateListener) {
        this.stateUpdateListener = gameStateUpdateListener;
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    // Implemented methods without functionality
    public void pause() { }
    public void unpause() { }
    public void setErrorListener(ErrorListener errorListener) { }
    public void setImageUpdateListener(ImageUpdateListener imageUpdateListener) { }

}
