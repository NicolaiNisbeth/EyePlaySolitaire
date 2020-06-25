package gui.gamescene.consolecomponent;


import gui.gamescene.IComponent;
import gui.gamescene.IConsole;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;


public class ConsoleComponent implements IComponent, IConsole {

    private VBox container = new VBox();
    private ScrollableTextField outputField = new ScrollableTextField("verdana", 12);
    private TextField inputField = new TextField();
    private HashMap<String, InputCommand> registeredCommands = new HashMap<>();
    private InputListener inputListener;


    public ConsoleComponent(){

        // Register Enter key pressed on inputfield
        inputField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if( event.getCode() == KeyCode.ENTER){
                onInput(inputField.getText());
                inputField.setText("");
                event.consume();
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


    private void onInput(String input){
        if( input != null && input.length() > 0 ){
            boolean wasCommand = false;
            print("> " +input, Color.DARKGRAY, false);
            for (String registeredCommand : registeredCommands.keySet()){
                if( registeredCommand.equals(input) ) {
                    registeredCommands.get(registeredCommand).run();
                    wasCommand = true;
                }
            }
            if(inputListener != null)
                inputListener.onConsoleInput(input, wasCommand);
        }
    }


    public void registerInputCommand(String input, InputCommand action){
        registeredCommands.put(input, action);
    }
    
    public void setInputListener(InputListener listener){
        this.inputListener = listener;
    }

    /**
     * Print a message in the console on a new line.
     */
    public void print(String msg, Paint color, boolean bold){
        Platform.runLater(() -> outputField.appendText(msg, color, bold));
    }


    public void printError(String errorMsg){
        print("Error: " + errorMsg, Color.DARKRED, false);
    }

    public void printInfo(String infoMsg){
        print(infoMsg, Color.DARKORANGE, false);
    }


    @Override
    public Node getNode() {
        return container;
    }





    /**
     * Callback interface for user input in the console */
    public interface InputListener{
        void onConsoleInput(String input, boolean wasCommand);
    }




}
