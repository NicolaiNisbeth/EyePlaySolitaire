package gui.gamescene;

import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamecomponent.GameComponent;
import gui.gamescene.gamecomponent.IGameComponent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class GameScene extends Scene {

    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 900;

    private GridPane grid;
    private IConsole console;
    private CameraComponent cameraComponent;
    private IGameComponent gameComponent;
    private IGamePrompter prompter;


    public GameScene() {
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
        prompter = consoleComponent;

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

        new GameController(this);
    }


    public IConsole getConsole() {
        return console;
    }

    public IGameComponent getGameComponent() {
        return gameComponent;
    }

    public CameraComponent getCameraComponent() {
        return cameraComponent;
    }

    public IGamePrompter getPrompter() {
        return prompter;
    }

}
