package gui.gamescene.gamecomponent;

import gui.util.MarginContainer;

/**
 * A card container for a single card.
 *
 * It extends the MarginContainer, so you may easily
 * apply a margin to the card contained.
 */
public class CardContainer extends MarginContainer {
    public CardContainer( double margin ){
        setMargin(margin);
    }

    public void setCard(CardPane card) {
        setNode(card);
    }

    public void clearCard(){
        removeNode();
    }
}
