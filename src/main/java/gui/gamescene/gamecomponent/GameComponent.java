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




/*
        CardPane card = new CardPane(new Image("images/cards/ace_of_clubs.png", false));
        card.setMargin(0.05);
        cellPane.getChildren().add(card);

        card = new CardPane(new Image("images/cards/ace_of_clubs.png", false));
        card.setMargin(0.10);
        cellPane.getChildren().add(card);*/

        CardStackContainer cardStackContainer = new CardStackContainer(12, 0.025, 0.05);
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        cardStackContainer.addCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));

        GridPane.setHgrow(cardStackContainer, Priority.ALWAYS);
        GridPane.setVgrow(cardStackContainer, Priority.ALWAYS);

        CardContainer cardContainer = new CardContainer(0.025);
        cardContainer.setCard(new CardPane(new Image("images/cards/ace_of_clubs.png", false)));
        GridPane.setHgrow(cardContainer, Priority.ALWAYS);
        GridPane.setVgrow(cardContainer, Priority.ALWAYS);

        RowConstraints rowConstraint;
        rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(28);
        grid.getRowConstraints().add(rowConstraint);

        grid.add(cardContainer,1,0);
        grid.add(cardStackContainer,0,1);
    }

    @Override
    public void updateGameState(GameState gameState) {

    }

    @Override
    public Node getNode() {
        return grid;
    }

}
