package gui.gamescene.gamecomponent;

import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.Card.Suit;
import gui.gamescene.gamestate.GameState;
import static gui.gamescene.gamecomponent.CardStackContainer.Orientation;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class GameComponent implements IGameComponent {

    private GridPane grid = new GridPane();
    private GameState gameState = new GameState();
    private CardImageLoader imageLoader = new CardImageLoader();

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
                constraints.setPercentHeight(25);
                grid.getRowConstraints().add(constraints);
            }

            if (i==1) {
                constraints.setPercentHeight(10);
                grid.getRowConstraints().add(constraints);
            }

            if (i==2) {
                constraints.setPercentHeight(25);
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
        for (Card card : gameState.getTableaus().get(0)) {
            CardPane pane = createCardPane(card);
        }

        CardContainer stackContainer = new CardContainer(0.05);
        stackContainer.setCard(gameState.);
        grid.add(stackContainer, 1, 0);
*/

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


            for (Card card : gameState.getTableaus().get(0)) {
                CardPane pane = createCardPane(card);

                CardStackContainer cardStackContainer = new CardStackContainer(14, 0.05, 0.05, Orientation.VERTICAL);
                cardStackContainer.addCard(pane);
                grid.add(setFive, 4, 2);
            }



            //Card stack container
            CardStackContainer hStack = new CardStackContainer(5, 0.05, 0.05, Orientation.HORIZONTAL);
            hStack.addCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
            hStack.addCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
            hStack.addCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
            hStack.addCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
            hStack.addCard(createCardPane(new Card(Suit.DIAMONDS, 5)));
            grid.add(hStack, 2, 0);

/*
        for(int i=0; i<13; i++){
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100./13);
            grid.getColumnConstraints().add(constraints);
        }

        for(int i=0; i<4; i++){
            RowConstraints constraints = new RowConstraints();
            constraints.setPercentHeight(25);
            grid.getRowConstraints().add(constraints);
        }


        int row = 1;
            for (Suit suit : cardPanesMap.keySet()) {
                int value = 7;
                for (CardPane pane : cardPanesMap.get(suit)) {
                    CardContainer container = new CardContainer(0.05);
                    container.setCard(pane);
                    grid.add(container, value, row);
                    value++;
                }
                row++;

            }








        for (int row = 0; row < 3; row++) {
            if (row == 0) {
                for (Suit suit : cardPanesMap.keySet()) {
                    int value = 0;
                    for (CardPane pane : cardPanesMap.get(suit)) {
                        CardContainer container = new CardContainer(0.05);
                        container.setCard(pane);
                        grid.add(container, value, row);
                        value++;
                    }
                }
            }

        }
*/
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
    }

    @Override
    public Node getNode() {
        return grid;
    }

}
