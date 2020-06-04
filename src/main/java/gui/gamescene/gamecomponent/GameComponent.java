package gui.gamescene.gamecomponent;


import gui.gamescene.gamestate.GameState;
import gui.gamescene.gamecomponent.CardStackContainer.Orientation;
import gui.gamescene.gamestate.UICard;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class GameComponent implements IGameComponent {

    private GridPane grid = new GridPane();
    private GameState gameState = new GameState();
    private CardImageLoader imageLoader = new CardImageLoader();
    private CardContainer stock;
    private CardStackContainer flipped;
    private List<CardStackContainer> tableaus;
    private List<CardContainer> foundations;


    public GameComponent(){

        // Set background color
        BackgroundFill fill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        grid.setBackground(background);


        //Set row
        for (int i = 0; i < 3; i++) {
            RowConstraints constraints = new RowConstraints();

            if (i==0) {
                constraints.setPercentHeight(20);
                grid.getRowConstraints().add(constraints);
            }

            if (i==1) {
                constraints.setPercentHeight(5);
                grid.getRowConstraints().add(constraints);
            }

            if (i==2) {
                constraints.setPercentHeight(75);
                grid.getRowConstraints().add(constraints);
            }

        }

        //Set Colonner
        for (int i = 0; i < 7; i++) {
            ColumnConstraints constraintss = new ColumnConstraints();

            constraintss.setPercentWidth(100);
            grid.getColumnConstraints().add(constraintss);

        }

        this.stock = new CardContainer(0.04);
        grid.add(stock, 0, 0);

        this.flipped = new CardStackContainer(3, 0.05, 0.10, Orientation.HORIZONTAL);
        grid.add(flipped,1,0);

        this.tableaus = new ArrayList<>();
        for( int i=0; i<7; i++){
            tableaus.add(new CardStackContainer(19, 0.02, 0.04, Orientation.VERTICAL));
            grid.add(tableaus.get(i), i, 2);
        }

        this.foundations = new ArrayList<>();
        for( int i=0; i<4; i++){
            foundations.add(new CardContainer(0.05));
            grid.add(foundations.get(i), i+3, 0);
        }

    }

    /**
     * Return the Card Pane matching the given card.
     */
    private CardPane createCardPane(UICard card){
        if( card.isUnknown() )
            return createCardBackPane();
        else
            return new CardPane(imageLoader.getCard(card));
    }

    private CardPane createCardBackPane(){
        return new CardPane(imageLoader.getCardBack());
    }


    @Override
    public void updateGameState(GameState gameState) {
        // Make sure it's run on the ui thread
        Platform.runLater(() -> {

            System.out.println("GameComponenet: New GameState recieved");
            System.out.println(gameState);

            // Update stock
            stock.clearCard();
            if (gameState.getStock().size() > 0) {
                stock.setCard(createCardBackPane());
            }

            // Update Flipped cards
            flipped.clearCards();
            List<UICard> flipped = gameState.getFlipped();
            for (UICard card : flipped) {
                CardPane pane = createCardPane(card);
                this.flipped.addCard(pane);
            }


            // Update tableaus
            for (int i = 0; i < gameState.getTableaus().size(); i++) {
                List<UICard> tableau = gameState.getTableaus().get(i);

                tableaus.get(i).clearCards();

                for (UICard card : tableau) {
                    CardPane pane = createCardPane(card);
                    tableaus.get(i).addCard(pane);
                }
            }

            // Update Foundations
            for (int i = 0; i < gameState.getFoundations().size(); i++) {
                foundations.get(i).clearCard();

                // Only display the top card, if foundation is not empty
                List<UICard> foundation = gameState.getFoundations().get(i);
                if( foundation.size() > 0 ){
                    UICard topCard = foundation.get(foundation.size()-1);
                    foundations.get(i).setCard(createCardPane(topCard));
                }
            }
        });
    }

    @Override
    public Node getNode() {
        return grid;
    }

}