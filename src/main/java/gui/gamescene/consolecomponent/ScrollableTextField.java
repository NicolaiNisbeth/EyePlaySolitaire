package gui.gamescene.consolecomponent;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * UI component containing a textfield which can scroll
 * when it gets too many message.
 */
class ScrollableTextField extends ScrollPane {

    private Text output = new Text();
    private String outputMessage = "";

    ScrollableTextField(){

        // Auto scale the scroll pane
        setFitToHeight(true);
        setFitToWidth(true);

        // Align text to bottom left
        StackPane.setAlignment(output, Pos.BOTTOM_LEFT);

        // Margin of the textoutput
        StackPane.setMargin(output, new Insets(10,10,10,10));

        // Wrapping in stackpane Required for automatic scaling of the text field
        StackPane outputContainer = new StackPane(output);
        setContent(outputContainer);

        // Scroll ScrollPane to button when new message is printed
        outputContainer.heightProperty().addListener(observable -> setVvalue(1.0));
    }


    /** Append a new some text to the textfield */
    void appendText(String text){
        // Add text to output list
        outputMessage += text;
        output.setText(outputMessage);
    }

}
