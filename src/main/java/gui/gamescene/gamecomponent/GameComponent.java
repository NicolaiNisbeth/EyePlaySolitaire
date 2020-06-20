package gui.gamescene.gamecomponent;


import gui.gamescene.IComponent;
import gui.gamescene.gamestate.GameState;
import gui.gamescene.gamecomponent.CardStackContainer.Orientation;
import gui.gamescene.gamestate.Card;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class GameComponent implements IComponent {

    private StackPane container = new StackPane();
    private Text infoText = new Text();

    private GridPane grid = new GridPane();
    private CardImageLoader imageLoader = new CardImageLoader();
    private CardContainer stock;
    private CardStackContainer stockOverview;
    private CardStackContainer flipped;
    private List<CardStackContainer> tableaus;
    private List<CardContainer> foundations;


    public GameComponent(){

        // Set background color
        BackgroundFill fill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        grid.setBackground(background);

        //Set row
        for (int i = 0; i < 5; i++) {
            RowConstraints constraints = new RowConstraints();

            if (i==0){
                constraints.setPercentHeight(14);
                grid.getRowConstraints().add(constraints);
            }

            if (i==1) {
                constraints.setPercentHeight(1);
                grid.getRowConstraints().add(constraints);
            }

            if (i==2) {
                constraints.setPercentHeight(17);
                grid.getRowConstraints().add(constraints);
            }

            if (i==3) {
                constraints.setPercentHeight(3);
                grid.getRowConstraints().add(constraints);
            }

            if (i==4) {
                constraints.setPercentHeight(65);
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
        grid.add(stock, 0, 2);

        this.flipped = new CardStackContainer(3, 0.05, 0.10, Orientation.HORIZONTAL);
        grid.add(flipped,1,2);

        this.tableaus = new ArrayList<>();
        for( int i=0; i<7; i++){
            tableaus.add(new CardStackContainer(19, 0.02, 0.04, Orientation.VERTICAL));
            grid.add(tableaus.get(i), i, 4);
        }

        this.foundations = new ArrayList<>();
        for( int i=0; i<4; i++){
            foundations.add(new CardContainer(0.05));
            grid.add(foundations.get(i), i+3, 2);
        }
        container.getChildren().add(grid);

        // Overview Stock
        stockOverview = new CardStackContainer(24, 0.10, 0.02, Orientation.HORIZONTAL);
        grid.add(stockOverview,0,0, 7, 1);

        // Setup infotext
        infoText.setFont(new Font(18));
        infoText.setFill(Color.WHITE);
        infoText.setText("No game state has been recieved from computervision yet");
        container.getChildren().add(infoText);
    }

    /**
     * Return the Card Pane matching the given card.
     */
    private CardPane createCardPane(Card card){
        if( card.isUnknown() )
            return createCardBackPane();
        else
            return new CardPane(imageLoader.getCard(card));
    }

    private CardPane createCardBackPane(){
        return new CardPane(imageLoader.getCardBack());
    }

    private CardPane createFoundationBorderPane(){
        return new CardPane(imageLoader.getFoundationBorder());
    }


    public void updateGameState(GameState gameState) {
        // Make sure it's run on the ui thread
        Platform.runLater(() -> {
            infoText.setVisible(false);

            // Update stock
            stock.clearCard();
            if (gameState.getStock().size() > 0) {
                stock.setCard(createCardBackPane());
            }

            // Update stock overview
            stockOverview.clearCards();
            List<Card> stock = gameState.getStock();
            for (Card card : stock) {
                CardPane pane = createCardPane(card);
                this.stockOverview.addCardPane(pane);
            }

            // Update Flipped cards
            flipped.clearCards();
            List<Card> flipped = gameState.getFlipped();
            for (Card card : flipped) {
                CardPane pane = createCardPane(card);
                this.flipped.addCardPane(pane);
            }

            // Update tableaus
            for (int i = 0; i < gameState.getTableaus().size(); i++) {
                List<Card> tableau = gameState.getTableaus().get(i);

                tableaus.get(i).clearCards();

                for (Card card : tableau) {
                    CardPane pane = createCardPane(card);
                    tableaus.get(i).addCardPane(pane);
                }
            }

            // Update Foundations
            for (int i = 0; i < gameState.getFoundations().size(); i++) {
                foundations.get(i).clearCard();

                // Only display the top card, if foundation is not empty
                List<Card> foundation = gameState.getFoundations().get(i);
                if( foundation.size() > 0 ){
                    Card topCard = foundation.get(foundation.size()-1);
                    foundations.get(i).setCard(createCardPane(topCard));
                }else{
                    foundations.get(i).setCard(createFoundationBorderPane());
                }
            }
        });
    }

    public void highlightTableauCard(int tableauIndex, int cardIndex,  Color color){
        tableaus.get(tableauIndex).getCard(cardIndex).borderGlow(color);
    }

    public void highlightFoundation(int index, Color color){
        ((CardPane) foundations.get(index).getChildren().get(0)).borderGlow(color);
    }

    public void highlightStockCard(int index, Color color){
        stockOverview.getCard(index).borderGlow(color);
    }

    @Override
    public Node getNode() {
        return container;
    }

}
