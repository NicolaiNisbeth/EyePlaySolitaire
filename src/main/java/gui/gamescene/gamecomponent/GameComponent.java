package gui.gamescene.gamecomponent;

import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.Card.Suit;
import gui.gamescene.gamestate.GameState;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
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
        grid.setGridLinesVisible(true);



        //Set row
        for (int i = 0; i < 3; i++) {
            RowConstraints constraints = new RowConstraints();

            if (i==0) {
                constraints.setPercentHeight(10);
                grid.getRowConstraints().add(constraints);
            }

            if (i==1) {
                constraints.setPercentHeight(5);
                grid.getRowConstraints().add(constraints);
            }

            if (i==2) {
                constraints.setPercentHeight(85);
                grid.getRowConstraints().add(constraints);
            }

        }

        //Set Colonner
        for (int i = 0; i < 8; i++) {
            ColumnConstraints constraintss = new ColumnConstraints();

            constraintss.setPercentWidth(100);
            grid.getColumnConstraints().add(constraintss);

        }
/*
        //Card container
        CardContainer deck = new CardContainer(0.05);
        deck.setCard(createCardBackPane());
        grid.add(deck, 0, 0);

        CardContainer setHeart = new CardContainer(0.05);
        setHeart.setCard(createCardPane(new Card(Suit.HEARTS, 1)));
        grid.add(setHeart, 4, 0);

        CardContainer setDiamonds = new CardContainer(0.05);
        setDiamonds.setCard(createCardPane(new Card(Suit.DIAMONDS, 1)));
        grid.add(setDiamonds, 5, 0);

        CardContainer setClubs = new CardContainer(0.05);
        setClubs.setCard(createCardPane(new Card(Suit.CLUBS, 1)));
        grid.add(setClubs, 6, 0);

        CardContainer setSpade = new CardContainer(0.05);
        setSpade.setCard(createCardPane(new Card(Suit.SPADES, 1)));
        grid.add(setSpade, 7, 0);


        //Card stack container

        CardContainer setFive = new CardContainer(0.05);
        setFive.setCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
        grid.add(setFive, 3, 2);

        //CardStackContainer cardStackContainer = new CardStackContainer(14, 0.05, 0.05);

*/

        this.stock = new CardContainer(0.04);
        grid.add(stock, 0, 0);

        this.flipped = new CardStackContainer(3, 0.05, 0.04);
        grid.add(flipped,1,0);

        this.tableaus = new ArrayList<>();
        for( int i=0; i<7; i++){
            tableaus.add(new CardStackContainer(19, 0.05, 0.04));
            grid.add(tableaus.get(i), i+1, 2);
        }

        this.foundations = new ArrayList<>();
        for( int i=0; i<4; i++){
            foundations.add(new CardContainer(0.05));
            grid.add(foundations.get(i), i+4, 0);
        }

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

    @Override
    public void updateGameState(GameState gameState) {
        System.out.println("GameComponenet: New GameState recieved");
        System.out.println(gameState);
        // TODO: Setup board according to game state

        if (gameState.getStock().size() != 0) {
            stock.setCard(createCardBackPane());
        }

        List<Card> flipped = gameState.getFlipped();

        for (Card card : flipped) {
            CardPane pane = createCardPane(card);
            this.flipped.addCard(pane);
        }


        for (int i = 0; i < gameState.getTableaus().size(); i++) {

            List<Card> tableau = gameState.getTableaus().get(i);

            for (Card card : tableau) {
                CardPane pane = createCardPane(card);
                tableaus.get(i).addCard(pane);
            }
        }

        for (int i = 0; i < gameState.getFoundations().size(); i++) {

            List<Card> foundation = gameState.getFoundations().get(i);
            int getLastIndex = foundation.size()-1;
            Card lastIndex = foundation.get(getLastIndex);

            CardPane pane = createCardPane(lastIndex);
            foundations.get(i).setCard(pane);

        }
    }

    @Override
    public Node getNode() {
        return grid;
    }

}
