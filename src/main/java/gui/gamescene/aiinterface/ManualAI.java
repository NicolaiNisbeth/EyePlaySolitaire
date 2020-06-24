package gui.gamescene.aiinterface;


import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamestate.GameState;
import gui.util.ActionConsole;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ManualAI implements ISolitaireAI, ActionConsole.ActionListener {

    private ConsoleComponent console = new ActionConsole(this);
    private ActionConsole.Argument arg1 = null;
    private ActionConsole.Argument arg2 = null;

    public ManualAI(){
        Stage stage = new Stage();
        stage.setTitle("AI");
        StackPane container = new StackPane();
        container.getChildren().add(console.getNode());
        stage.setScene(new Scene(container, 225, 300));
        stage.show();
        console.printInfo("Manual AI Console\nType one of the available commands to prompt an action: 'tX tY', 'tX fY', 'sX tY' or 'sX fY', where X and Y are indexes of the tableau, foundation or stock (starting from 0)");
    }


    @Override
    public void startActionComputation(GameState gameState) throws IllegalGameStateException {
        arg1 = null;
        arg2 = null;
        console.printInfo("Computing initialized");
    }


    /**
     * When the computation is told to end, the input string will be evaluated,
     * and the appropriate prompt will be called.
     */
    @Override
    public void endActionComputation(IActionPrompter prompter) {
        console.printInfo("Computing ended");

        if( arg1 != null && arg2 != null ){
            if( arg1.prefix.equals("t") && arg2.prefix.equals("t") )
                prompter.promptTableauToTableau(arg1.value, arg2.value);
            else
            if( arg1.prefix.equals("t") && arg2.prefix.equals("f") )
                prompter.promptTableauToFoundation(arg1.value, arg2.value);
            else
            if( arg1.prefix.equals("s") && arg2.prefix.equals("f") )
                prompter.promptStockToFoundation(arg1.value, arg2.value);
            else
            if( arg1.prefix.equals("s") && arg2.prefix.equals("t") )
               prompter.promptStockToTableau(arg1.value, arg2.value);
        }else{
            console.printError("No action was input");
            prompter.noActionComputed();
        }
    }

    // Unused method
    public void computeAction(GameState gameState, IActionPrompter prompter) throws IllegalGameStateException {}

    @Override
    public void onTableauToTableau(int sourceIndex, int targetIndex) {
        arg1 = new ActionConsole.Argument("t", sourceIndex);
        arg2 = new ActionConsole.Argument("t", targetIndex);
    }

    @Override
    public void onTableauToFoundation(int tableauIndex, int foundationIndex) {
        arg1 = new ActionConsole.Argument("t", tableauIndex);
        arg2 = new ActionConsole.Argument("f", foundationIndex);
    }

    @Override
    public void onStockToTableau(int stockIndex, int tableauIndex) {
        arg1 = new ActionConsole.Argument("s", stockIndex);
        arg2 = new ActionConsole.Argument("T", tableauIndex);
    }

    @Override
    public void onStockToFoundation(int stockIndex, int foundationIndex) {
        arg1 = new ActionConsole.Argument("s", stockIndex);
        arg2 = new ActionConsole.Argument("f", foundationIndex);
    }
}
