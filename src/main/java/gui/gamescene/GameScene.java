package gui.gamescene;

import gui.gamescene.aiinterface.IActionPrompter;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.control.ControlComponent;
import gui.gamescene.gamecomponent.GameComponent;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;


public class GameScene extends Scene implements IActionPrompter {

    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;

    private GridPane grid;
    private CameraComponent cameraComponent;
    private GameComponent gameComponent;
    private IActionPrompter prompter;
    private ControlComponent controlComponent;

    private GameController gameController;


    public GameScene(boolean useManualAI, boolean useManualCV, boolean usePreDefinedStock) {
        super(new GridPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        grid = (GridPane) getRoot();

        // Setup Row constraints
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(40);
        grid.getRowConstraints().add(row1);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(35);
        grid.getColumnConstraints().add(column1);

        controlComponent = new ControlComponent();
        grid.add(controlComponent.getNode(), 0,1 );
        GridPane.setHgrow(controlComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(controlComponent.getNode(), Priority.ALWAYS);

        // Add Game Component
        gameComponent = new GameComponent();
        Node gameNode = gameComponent.getNode();
        grid.add(gameNode, 1, 0, 1, 2);
        GridPane.setHgrow(gameNode, Priority.ALWAYS);
        GridPane.setVgrow(gameNode, Priority.ALWAYS);

        // Add Camera Component
        cameraComponent = new CameraComponent();
        Node cameraNode = cameraComponent.getNode();
        grid.add(cameraNode, 0,0 );
        GridPane.setHgrow(cameraNode, Priority.ALWAYS);
        GridPane.setVgrow(cameraNode, Priority.ALWAYS);

        prompter = this;

        gameController = new GameController(this, useManualAI, useManualCV, usePreDefinedStock);
    }


    public GameComponent getGameComponent() {
        return gameComponent;
    }

    public CameraComponent getCameraComponent() {
        return cameraComponent;
    }

    public IActionPrompter getPrompter() {
        return prompter;
    }

    public ControlComponent getControlComponent() {
        return controlComponent;
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    // Action prompting


    private static final Color COLOR_TARGET = Color.DARKORANGE;
    private static final Color COLOR_SOURCE = Color.CORNFLOWERBLUE;

    private void printActionPrompt(String prompt){
        // Cause the prompt to run on the UI thread
        //console.print("Take action: " + prompt, Color.DARKGREEN, false);
        controlComponent.displayMessage("Take action:\n" + prompt);
    }


    @Override
    public void promptTableauToTableau(int sourceTableauIndex, int targetTableauIndex) {
        GameState state = gameController.getCurrentGameState();
        List<Card> sourceTableau = state.getTableau(sourceTableauIndex);

        // Figuring out if we should move just 1 card or a stack of cards
        List<Card> cardsToMove = new LinkedList<>();
        for( int i=sourceTableau.size()-1; i >= 0; i-- ){
            Card current = sourceTableau.get(i);
            Card previous = cardsToMove.size() == 0 ? null : cardsToMove.get(cardsToMove.size()-1);
            if( previous == null ) {
                cardsToMove.add(current);
            }else if(current.getValue() == previous.getValue()+1 && current.getColor() != previous.getColor() ){
                cardsToMove.add(current);
            }else{
                break;
            }
        }

        // Create string of card names to move
        String cardNames = "";
        if( cardsToMove.size() == 1 )
            cardNames = cardsToMove.get(0).toStringPretty();
        else {
            cardNames = cardsToMove.subList(0, cardsToMove.size() - 1).stream().map(Card::toStringPretty).collect(joining(", "));
            cardNames += " and " + cardsToMove.get(cardsToMove.size()-1).toStringPretty();
        }

        // Highlighting cards
        List<List<Card>> tableaus = gameController.getCurrentGameState().getTableaus();
        for( int i=1; i<=cardsToMove.size(); i++){
            gameComponent.highlightTableauCard(sourceTableauIndex, tableaus.get(sourceTableauIndex).size()-i, COLOR_SOURCE);
        }
        gameComponent.highlightTableauCard(targetTableauIndex, tableaus.get(targetTableauIndex).size()-1, COLOR_TARGET);
        printActionPrompt(String.format("Move %s from the %s tableau to the %s tableau", cardNames, indexOrdinal(sourceTableauIndex), indexOrdinal(targetTableauIndex)));
    }


    @Override
    public void promptStockToTableau(int stockIndex, int tableauIndex) {
        Card card = gameController.getCurrentGameState().getStock().get(stockIndex);
        List<List<Card>> tableaus = gameController.getCurrentGameState().getTableaus();
        gameComponent.highlightStockCard(stockIndex, COLOR_SOURCE);
        gameComponent.highlightTableauCard(tableauIndex, tableaus.get(tableauIndex).size()-1, COLOR_TARGET);
        printActionPrompt(String.format("Move %s from the stock to the %s tableau", card.toStringPretty(), indexOrdinal(tableauIndex)));
    }


    @Override
    public void promptTableauToFoundation(int tableauIndex, int foundationIndex) {
        GameState state = gameController.getCurrentGameState();
        Card card = state.getTableauTop(tableauIndex);
        List<List<Card>> tableaus = gameController.getCurrentGameState().getTableaus();
        gameComponent.highlightTableauCard(tableauIndex, tableaus.get(tableauIndex).size()-1, COLOR_SOURCE);
        gameComponent.highlightFoundation(foundationIndex, COLOR_TARGET);
        printActionPrompt(String.format("Move %s from the %s tableau to the %s foundation", card.toStringPretty(), indexOrdinal(tableauIndex), indexOrdinal(foundationIndex)));
    }

    @Override
    public void promptStockToFoundation(int stockIndex, int foundationIndex) {
        Card card = gameController.getCurrentGameState().getStock().get(stockIndex);
        gameComponent.highlightStockCard(stockIndex, COLOR_SOURCE);
        gameComponent.highlightFoundation(foundationIndex, COLOR_TARGET);
        printActionPrompt(String.format("Move %s from the stock to the %s foundation", card.toStringPretty(), indexOrdinal(foundationIndex)));

    }


    @Override
    public void noActionComputed() {
        controlComponent.displayMessage("No action was computed!");
    }

    @Override
    public void gameLost() {
        printActionPrompt("The solitaire can't be solved - too bad :(");
    }


    private String indexOrdinal(int i) {
        // Source: https://stackoverflow.com/questions/6810336/is-there-a-way-in-java-to-convert-an-integer-to-its-ordinal
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        i++;
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }
}
