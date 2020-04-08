package gui.gamescene;

import cv_test.Camera;
import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamecomponent.GameComponent;
import gui.gamescene.gamecomponent.IGameComponent;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import gui.gamescene.gamestate.GameStateGenerator;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;


public class GameScene extends Scene implements ConsoleComponent.InputListener {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 720;

    private GridPane grid;
    private ConsoleComponent consoleComponent;
    private CameraComponent cameraComponent;
    private IGameComponent gameComponent;
    private IGamePrompter prompter;
    private ISolitaireAI ai;
    private Camera camera;

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
        consoleComponent = new ConsoleComponent(this);
        grid.add(consoleComponent.getNode(), 0,1 );
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);

        prompter = consoleComponent;


        // Add Game Component
        gameComponent = new GameComponent();
        Node gameNode = gameComponent.getNode();
        grid.add(gameNode, 1, 0, 1, 2);
        GridPane.setHgrow(gameNode, Priority.ALWAYS);
        GridPane.setVgrow(gameNode, Priority.ALWAYS);

        // TODO: Remove this after testing
        GameState state = GameStateGenerator.generateGameState(1000);
        while( state.getTableaus().get(0).size() < 19 ){
            state.getTableaus().get(0).add(Card.createUnknown());
        }
        gameComponent.updateGameState(state);


        // Add Camera Component
        cameraComponent = new CameraComponent();
        Node cameraNode = cameraComponent.getNode();
        grid.add(cameraNode, 0,0 );
        GridPane.setHgrow(cameraNode, Priority.ALWAYS);
        GridPane.setVgrow(cameraNode, Priority.ALWAYS);

        // Just a temporary image
        Image image = new Image("images/solitaire_irl.jpg", false);
        cameraComponent.updateImage(image);


/*
        // Testing camera
        camera = new Camera();
        camera.startCamera( (img) -> {
            cameraComponent.updateImage(img);
        });
        */
    }

    @Override
    public void onConsoleInput(String input) {
        // TODO: Implement meaningful functionality here
        System.out.println("Input from console: " + input);
    }


    /**
     * Sends an asynchronous computation request to the AI on a new non-UI thread.
     */
    public void computeNextAction(GameState state){
        new Thread(() -> {
            // TODO: Implement this with better error message system in the GUI
            try{
                ai.computeAction(state, prompter);
            }catch(ISolitaireAI.IllegalGameStateException e){
                e.printStackTrace();
                System.out.println(state);
            }
        }).start();
    }

}
