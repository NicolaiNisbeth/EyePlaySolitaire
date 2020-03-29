package gui.gamescene.consolecomponent;

import gui.gamescene.IComponent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConsoleComponent implements IComponent {

    private VBox container = new VBox();
    private ScrollableTextField outputField = new ScrollableTextField();
    private TextField inputField = new TextField();


    public ConsoleComponent(InputListener inputListener){

        // Register Enter key pressed on inputfield
        inputField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if( event.getCode() == KeyCode.ENTER){
                String input = inputField.getText();
                if( input != null && input.length() > 0 ){
                    print(input);
                    inputField.setText("");
                    inputListener.onConsoleInput(input);
                    event.consume();
                }
            }
        });

        VBox.setVgrow(outputField, Priority.ALWAYS);
        VBox.setVgrow(inputField, Priority.ALWAYS);

        // Add subcomponents
        container.getChildren().addAll(
                outputField,
                inputField
        );
    }

    /**
     * Print a message in the console on a new line.
     */
    public void print(String msg){
        outputField.appendText("\n" + msg);
    }


    @Override
    public Node getNode() {
        return container;
    }


    /**
     * Callback interface for user input in the console */
    public interface InputListener{
        void onConsoleInput(String input);
    }

}
