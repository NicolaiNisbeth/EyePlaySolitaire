package gui.gamescene.consolecomponent;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * UI component containing a textfield which can scroll
 * when it gets too many message.
 */
class ScrollableTextField extends ScrollPane {

    private Text output = new Text();
    private List<Text> texts = new ArrayList<>();
    private VBox outputContainer = new VBox();
    private String fontFamily;
    private int fontSize;


    ScrollableTextField(String fontFamily, int fontSize){
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;

        //setHbarPolicy(ScrollBarPolicy.NEVER);

        // Auto scale the scroll pane
        setFitToHeight(true);
        setFitToWidth(true);
        new Insets(10,0,10,0);

        // Align text to bottom left
        outputContainer.setAlignment(Pos.BOTTOM_LEFT);

        // Wrapping in stackpane Required for automatic scaling of the text field
        setContent(outputContainer);

        // Scroll ScrollPane to button when new message is printed
        outputContainer.heightProperty().addListener(observable -> setVvalue(1.0));
    }


    /** Append a new some text to the textfield */
    void appendText(String text, Paint color, boolean bold){
        Text output = new Text();
        output.setFont(Font.font(fontFamily, bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize ));
        output.setFill(color);
        output.setText(text);
        output.wrappingWidthProperty().bind(widthProperty().subtract(20));
        texts.add(output);
        VBox.setMargin(output, new Insets(0, 0, 5, 10));
        outputContainer.getChildren().add(output);
    }

}
