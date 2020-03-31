package gui.gamescene.gamecomponent;

import gui.gamescene.GameState;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class GameComponent implements IGameComponent {

    private GridPane grid = new GridPane();

    public GameComponent(){
        BackgroundFill fill = new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        grid.setBackground(background);

        StackPane cellPane = new StackPane();

        CardPane card = new CardPane(new Image("images/cards/ace_of_clubs.png", false), 0);
        cellPane.getChildren().add(card);

        card = new CardPane(new Image("images/cards/ace_of_clubs.png", false), 10);
        cellPane.getChildren().add(card);

        GridPane.setHgrow(cellPane, Priority.ALWAYS);
        GridPane.setVgrow(cellPane, Priority.ALWAYS);
        grid.add(cellPane,0,0);
    }

    @Override
    public void updateGameState(GameState gameState) {

    }

    @Override
    public Node getNode() {
        return grid;
    }

}
