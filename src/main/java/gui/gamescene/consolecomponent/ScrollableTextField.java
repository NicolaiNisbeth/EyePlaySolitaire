package gui.gamescene.consolecomponent;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.LinkedList;
import java.util.List;

public class ScrollableTextField extends ScrollPane {

    private Text output = new Text();
    private List<String> outputs = new LinkedList<String>();

    public ScrollableTextField(){

        StackPane stackPane = new StackPane(output);

        setFitToHeight(true);
        setFitToWidth(true);
        setContent(stackPane);

        output.setTextAlignment(TextAlignment.CENTER);


        //prefWidthProperty().bind();
    }


    void appendText(String text){

        outputs.add(text);

        StringBuilder outputString = new StringBuilder();
        for( String str : outputs ){
            outputString.append(str);
            outputString.append('\n');
        }

        output.setText(outputString.toString());
    }



}
