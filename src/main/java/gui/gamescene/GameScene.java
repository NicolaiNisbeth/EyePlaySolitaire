package gui.gamescene;

import gui.gamescene.aiinterface.IActionPrompter;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.consolecomponent.ConsoleComponent;
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

import java.util.List;


public class GameScene extends Scene implements IActionPrompter {

    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;

    private GridPane grid;
    private IConsole console;
    private CameraComponent cameraComponent;
    private GameComponent gameComponent;
    private IActionPrompter prompter;

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

        // Add Console component
        ConsoleComponent consoleComponent = new ConsoleComponent();
        grid.add(consoleComponent.getNode(), 0,1 );
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);
        console = consoleComponent;
        prompter = this;

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

        gameController = new GameController(this, useManualAI, useManualCV, usePreDefinedStock);
    }


    public IConsole getConsole() {
        return console;
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




    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    // Action prompting


    private static final Color COLOR_TARGET = Color.DARKORANGE;
    private static final Color COLOR_SOURCE = Color.CORNFLOWERBLUE;

    private void printActionPrompt(String prompt){
        // Cause the prompt to run on the UI thread
        console.print("Take action: " + prompt, Color.DARKGREEN, false);

    }


    @Override
    public void promptTableauToTableau(int sourceTableauIndex, int targetTableauIndex) {
        GameState state = gameController.getCurrentGameState();
        Card card = state.getTableauTop(sourceTableauIndex);
        List<List<Card>> tableaus = gameController.getCurrentGameState().getTableaus();
        gameComponent.highlightTableauCard(sourceTableauIndex, tableaus.get(sourceTableauIndex).size()-1, COLOR_SOURCE);
        gameComponent.highlightTableauCard(targetTableauIndex, tableaus.get(targetTableauIndex).size()-1, COLOR_TARGET);
        printActionPrompt(String.format("Move %s from the %s tableau to the %s tableau", card.toStringPretty(), indexOrdinal(sourceTableauIndex), indexOrdinal(targetTableauIndex)));

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
