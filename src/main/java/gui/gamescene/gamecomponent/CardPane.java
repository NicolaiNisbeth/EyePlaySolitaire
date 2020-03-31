package gui.gamescene.gamecomponent;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * Node (Pane) which allows for a simple displaying
 * of a dynamic card image. */
class CardPane extends Pane {

    CardPane(Image cardImage){
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(cardImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        setBackground(background);

        // Add card drop shadow
        DropShadow shadow = new DropShadow(2, new Color(0,0,0,0.5));
        shadow.setWidth(0);
        shadow.setOffsetY(-2);
        setEffect(shadow);
    }
}
