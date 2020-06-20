package gui.gamescene.aiinterface;


import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import gui.util.CommandToken;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;


public class ManualAI implements ISolitaireAI, ConsoleComponent.InputListener {

    private ConsoleComponent console = new ConsoleComponent();
    private String computingInput = null;

    public ManualAI(){
        Stage stage = new Stage();
        stage.setTitle("AI");
        StackPane container = new StackPane();
        container.getChildren().add(console.getNode());
        stage.setScene(new Scene(container, 225, 300));
        stage.show();
        console.setInputListener(this);
        console.printInfo("Manual AI Console\nType one of the available commands to prompt an action: 'tX tY', 'tX fY', 'sX tY' or 'sX fY', where X and Y are indexes of the tableau, foundation or stock (starting from 0)");
    }


    /**
     * Read input from the console, and performs some sort of movement of
     * the game state (i.e. move card from tableau 1 to 2).
     */
    @Override
    public void onConsoleInput(String input, boolean wasCommand) {
        if( !wasCommand ){
            computingInput = input;
        }
    }


    @Override
    public void startActionComputation(GameState gameState) throws IllegalGameStateException {
        computingInput = null;
        console.printInfo("Computing initialized");
    }


    /**
     * When the computation is told to end, the input string will be evaluated,
     * and the appropriate prompt will be called.
     */
    @Override
    public void endActionComputation(IActionPrompter prompter) {
        console.printInfo("Computing ended");

        if( computingInput != null) {
            CommandToken[] tokens = CommandToken.fromString(computingInput);
            if (tokens[0].hasPrefix("s")) {
                if (tokens[1].hasPrefix("t")) {
                    prompter.promptStockToTableau(tokens[0].value, tokens[1].value);
                }
            }

            if (tokens[0].hasPrefix("t")) {
                if (tokens[1].hasPrefix("t")) {
                    prompter.promptTableauToTableau(tokens[0].value, tokens[1].value);
                }
            }

            if (tokens[0].hasPrefix("s")) {
                if (tokens[1].hasPrefix("f")) {
                    prompter.promptStockToFoundation(tokens[0].value, tokens[1].value);
                }
            }

            if (tokens[0].hasPrefix("t")) {
                if (tokens[1].hasPrefix("f")) {
                    prompter.promptTableauToFoundation(tokens[0].value, tokens[1].value);
                }
            }
        }else{
            console.printError("No action was input");
        }


    }

    // Unused method
    public void computeAction(GameState gameState, IActionPrompter prompter) throws IllegalGameStateException {}
}
