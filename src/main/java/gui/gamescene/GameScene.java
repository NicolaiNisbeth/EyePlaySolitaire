package gui.gamescene;

import gui.gamescene.consolecomponent.ConsoleComponent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GameScene extends Scene {

    private GridPane grid;
    private ConsoleComponent consoleComponent;

    public GameScene() {
        super(new GridPane());
        grid = (GridPane) getRoot();

        consoleComponent = new ConsoleComponent();
        grid.add(consoleComponent.getNode(), 0,0 );

        // Makes sure the component's node fills its assigned cell
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);


        for(int i=0; i<100; i++){
            consoleComponent.print(String.format("Hello world %d\n", i));
        }
    }
}
