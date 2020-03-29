package gui.gamescene.gamecomponent;

import gui.gamescene.GameState;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

// JUST A TEMPORARY TEST CLASS
public class GameComponentTest implements IGameComponent {

    private Pane imagePane = new Pane();

    public GameComponentTest(){
        Image image = new Image("images/solitaire.jpg", false);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        imagePane.setBackground(background);
    }

    @Override
    public Node getNode() {
        return imagePane;
    }

    @Override
    public void updateGameState(GameState gameState) {

    }
}
