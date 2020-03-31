package gui.gamescene.gamecomponent;

import gui.util.MarginContainer;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class CardPane extends Pane {

    public CardPane(Image cardImage){

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(cardImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        setBackground(background);

        // Add card drop shadow
        DropShadow shadow = new DropShadow(2, new Color(0,0,0,0.5));
        shadow.setWidth(0);
        shadow.setOffsetY(-2);
        setEffect(shadow);

/*
        BackgroundFill fill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY);
        Background gridBackgound = new Background(fill);
        setBackground(gridBackgound);*/
/*

        ColumnConstraints columnConstraint;

        columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(10);
        getColumnConstraints().add(columnConstraint);

        columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(80);
        getColumnConstraints().add(columnConstraint);

        columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(10);
        getColumnConstraints().add(columnConstraint);



        RowConstraints rowConstraint;

        rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(10+topMargin/2);
        getRowConstraints().add(rowConstraint);

        rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(80);
        getRowConstraints().add(rowConstraint);

        rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(10-topMargin/2);
        getRowConstraints().add(rowConstraint);
        add(card, 1,1);

*/

    }





}
