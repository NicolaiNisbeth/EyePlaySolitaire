package gui.gamescene.consolecomponent;


import ai.state.Card;
import gui.gamescene.IComponent;
import gui.gamescene.IConsole;
import gui.gamescene.aiinterface.IGamePrompter;
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


public class ConsoleComponent implements IComponent, IGamePrompter, IConsole {

    private VBox container = new VBox();
    private ScrollableTextField outputField = new ScrollableTextField("verdana", 12);
    private TextField inputField = new TextField();
    private HashMap<String, InputCommand> registeredCommands = new HashMap<>();


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
            print("> " +input, Color.DARKGRAY, false);
            for (String registeredCommand : registeredCommands.keySet()){
                if( registeredCommand.equals(input) ) {
                    registeredCommands.get(registeredCommand).run();
                }
            }
        }
    }


    public void registerInputCommand(String input, InputCommand action){
        registeredCommands.put(input, action);
    }


    /**
     * Print a message in the console on a new line.
     */
    public void print(String msg, Paint color, boolean bold){
        outputField.appendText(msg, color, bold);
    }


    public void printError(String errorMsg){
        outputField.appendText("Error: " + errorMsg, Color.DARKRED, false);
    }

    public void printInfo(String infoMsg){
        outputField.appendText(infoMsg, Color.DARKORANGE, false);
    }


    @Override
    public Node getNode() {
        return container;
    }


    private void printActionPrompt(String prompt){
        // Cause the prompt to run on the UI thread
        Platform.runLater(() -> {
            print("Take action: " + prompt, Color.DARKGREEN, false);
        });
    }

    @Override
    public void promptTraverseStock() {
        printActionPrompt("Traverse the stock");
    }

    @Override
    public void promptTableauToTableau(int sourceTableauIndex, int targetTableauIndex) {
        printActionPrompt(String.format("Move the topmost card from the %s tableau to the %s", indexOrdinal(sourceTableauIndex), indexOrdinal(targetTableauIndex)));
    }

    @Override
    public void promptStockToTableau(int stockIndex, int tableauIndex) {
        printActionPrompt(String.format("Move the %s card from the stock, to the %s tableau", indexOrdinal(stockIndex), indexOrdinal(tableauIndex)));
    }

    @Override
    public void promptStockToTableau(Card card, int tableauIndex) {
        printActionPrompt(String.format("Move %s from the stock, to the %s tableau", card.toString(), indexOrdinal(tableauIndex)));
    }

    @Override
    public void promptTableauToFoundation(int tableauIndex, int foundationIndex) {
        printActionPrompt(String.format("Move the topmost card from the %s tableau the %s foundation", indexOrdinal(tableauIndex), indexOrdinal(foundationIndex)));

    }

    @Override
    public void promptStockToFoundation(Card card, int foundationIndex) {
        printActionPrompt(String.format("Move %s from the stock, to the %s foundation", card.toString(), indexOrdinal(foundationIndex)));
    }

    @Override
    public void promptStockToFoundation(int stockIndex, int foundationIndex) {
        printActionPrompt(String.format("Move the %s card from the stock, to the %s foundation", indexOrdinal(stockIndex), indexOrdinal(foundationIndex)));
    }

    @Override
    public void gameLost() {
        printActionPrompt("The solitaire can't be solved - too bad :(");
    }


    private String indexOrdinal(int i) {
        // Source: https://stackoverflow.com/questions/6810336/is-there-a-way-in-java-to-convert-an-integer-to-its-ordinal
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        i++;
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }

    /**
     * Callback interface for user input in the console */
    public interface InputListener{
        void onConsoleInput(String input);
    }




}
