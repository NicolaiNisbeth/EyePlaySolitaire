package gui.gamescene.consolecomponent;


import gui.gamescene.IComponent;
import gui.gamescene.aiinterface.IGamePrompter;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConsoleComponent implements IComponent, IGamePrompter {

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


    private void printActionPrompt(String prompt){
        // Cause the prompt to run on the UI thread
        Platform.runLater(() -> {
            print("Take action: " + prompt);
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
        printActionPrompt(String.format("Move the %s card from the stock, to the %s foundation", indexOrdinal(stockIndex), indexOrdinal(tableauIndex)));
    }

    @Override
    public void promptTableauToFoundation(int tableauIndex, int foundationIndex) {
        printActionPrompt(String.format("Move the topmost card from the %s tableau the %s foundation", indexOrdinal(tableauIndex), indexOrdinal(foundationIndex)));

    }

    @Override
    public void promptStockToFoundation(int stockIndex, int foundationIndex) {
        printActionPrompt(String.format("Move the %s card from the stock, to the %s foundation", indexOrdinal(stockIndex), indexOrdinal(foundationIndex)));
    }

    @Override
    public void gameLost() {
        print("The solitaire can't be solved - too bad :(");
    }


    private String indexOrdinal(int i) {
        // Source: https://stackoverflow.com/questions/6810336/is-there-a-way-in-java-to-convert-an-integer-to-its-ordinal
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
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
