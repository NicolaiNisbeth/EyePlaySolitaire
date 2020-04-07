package gui.gamescene.gamecomponent;

import gui.gamescene.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

// JUST A TEMPORARY TEST CLASS
public class GameComponentTest implements IGameComponent {

    private Pane imagePane = new Pane();
    private GridPane grid;

    public GameComponentTest(){

        grid = new GridPane();
        StackPane stackPane = new StackPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label deck = new Label(" \nDeck\n \n");
        GridPane.setConstraints(deck,1,1);
        deck.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        //Sets
        Label setHearts = new Label(" \n   ♥   \n \n");
        GridPane.setConstraints(setHearts,11,1);
        setHearts.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        Label setSpades = new Label(" \n   ♠   \n \n");
        GridPane.setConstraints(setSpades,13,1);
        setSpades.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        Label setClubs = new Label(" \n   ♣   \n \n");
        GridPane.setConstraints(setClubs,15,1);
        setClubs.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        Label setDiamonds = new Label(" \n   ♦   \n \n");
        GridPane.setConstraints(setDiamonds,17,1);
        setDiamonds.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");


        //Piles
        Label pile1 = new Label(" \n   1   \n \n");
        GridPane.setConstraints(pile1,5,5);
        pile1.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        Label pile2 = new Label(" \n   2   \n \n");
        GridPane.setConstraints(pile2,7,5);
        pile2.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");
        Label pile21 = new Label(" \n   .    \n ");
        StackPane test = new StackPane(pile2,pile21);
        StackPane.setAlignment(pile21, Pos.CENTER);

        pile21.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-arc-height: 2;");

        Label pile3 = new Label(" \n   3   \n \n");
        GridPane.setConstraints(pile3,9,5);

        Label pile4 = new Label(" \n   4   \n \n");
        GridPane.setConstraints(pile4,11,5);

        Label pile5 = new Label(" \n   5   \n \n");
        GridPane.setConstraints(pile5,13,5);

        Label pile6 = new Label(" \n   6   \n \n");
        GridPane.setConstraints(pile6,15,5);

        Label pile7 = new Label(" \n   7   \n \n");
        GridPane.setConstraints(pile7,17,5);


        grid.getChildren().addAll(deck,setClubs,setDiamonds,setHearts,setSpades,pile1,pile2,pile21,pile3,pile4,pile5,pile6,pile7);

        grid.setGridLinesVisible(true);

        /*
        Image image = new Image("images/solitaire_bagside.jpg", false);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        imagePane.setBackground(background);
        */
    }

    @Override
    public Node getNode() {
        return grid;
    }

    @Override
    public void updateGameState(GameState gameState) {

    }
}
