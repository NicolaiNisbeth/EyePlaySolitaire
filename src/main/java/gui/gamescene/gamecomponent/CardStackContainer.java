package gui.gamescene.gamecomponent;


import gui.util.MarginContainer;
import javafx.scene.layout.StackPane;



/**
 * A card container for a cascading stack of cards.
 * The size of the stack (amounts of cards that may be
 * added) may be adjusted.
 */
public class CardStackContainer extends StackPane {

    private MarginContainer[] containers;
    private int cardCount = 0;
    private int size;

    /**
     * @param size  The amount of cards you may add to the container. This
     *              is required to reserve the required space for a full stack
     *              of cards.
     * @param generalMargin A general decimal percentage margin for all cards
     *                      in the stack. Can be seen as the margin of a single
     *                      card in a stack of size 1.
     * @param stackMargin The decimal percentage margin added between each card
     *                    added to the stack (for the cascading effect).
     */
    public CardStackContainer(int size, double generalMargin, double stackMargin ) {

        this.size = size;
        containers = new MarginContainer[size];

        for( int i=0; i<size; i++ ){
            MarginContainer container = new MarginContainer();
            container.setMargin(generalMargin);
            container.setMarginTop(generalMargin + stackMargin*i);
            container.setMarginBottom(generalMargin + stackMargin*(size-1-i));
            containers[i] = container;
            getChildren().add(container);

        }
    }

    /**
     * Add a card to the stack. The card will be added to the "top" of the
     * stack.
     *
     * @throws IndexOutOfBoundsException If the stack is full (size given on construction)
     */
    public void addCard(CardPane card){
        if( cardCount == size )
            throw new IndexOutOfBoundsException(String.format("Too many cards in container. Size is %d.",size));

        containers[cardCount].setNode(card);
        cardCount++;
    }


    /**
     * Removes all cards from the stack.
     * This doesn't reset size or margins.
     */
    public void clear(){
        for( MarginContainer container : containers ){
            container.removeNode();
        }
    }

}
