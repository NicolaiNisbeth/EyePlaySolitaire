package gui.gamescene.gamecomponent;

import ai.state.Card;
import gui.util.MarginContainer;
import javafx.scene.paint.Color;
import sun.awt.SunHints;


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
