package gui.gamescene;

import gui.gamescene.consolecomponent.ConsoleComponent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GameScene extends Scene {

    private static final int WINDOW_WIDTH = 1080;
    private static final int WINDOW_HEIGHT = 720;

    private GridPane grid;
    private ConsoleComponent consoleComponent;

    public GameScene() {
        super(new GridPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        grid = (GridPane) getRoot();

        consoleComponent = new ConsoleComponent((input) -> System.out.println("Input from console: " + input));
        grid.add(consoleComponent.getNode(), 0,0 );

        // Makes sure the component's node fills its assigned cell
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);

    }
}
