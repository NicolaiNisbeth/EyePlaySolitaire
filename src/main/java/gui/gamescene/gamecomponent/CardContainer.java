package gui.gamescene.gamecomponent;

import ai.state.Card;
import gui.util.MarginContainer;
import javafx.scene.effect.DropShadow;
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


    public void borderGlow( Color color) {
        /* https://blog.idrsolutions.com/2014/02/tutorial-create-border-glow-effect-javafx/ */
        DropShadow borderGlow = new DropShadow();
        int depth = 100; //Setting the uniform variable for the glow width and height
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        borderGlow.setSpread(0.5);
        borderGlow.setColor(color);
        setEffect(borderGlow);
    }


    public void disableGlow(){
        // Add card drop shadow
        DropShadow shadow = new DropShadow(2, new Color(0, 0, 0, 0.5));
        shadow.setWidth(0);
        shadow.setOffsetY(-2);
        setEffect(shadow);
    }
}
