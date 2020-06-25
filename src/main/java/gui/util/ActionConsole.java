package gui.util;

import gui.gamescene.consolecomponent.ConsoleComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class ActionConsole extends ConsoleComponent implements ConsoleComponent.InputListener {

    private ActionListener actionListener;

    public ActionConsole(ActionListener actionListener){
        this.actionListener = actionListener;
        setInputListener(this);
    }

    @Override
    public void onConsoleInput(String input, boolean wasCommand) {
        String[] tokens = getTokens(input);

        if( tokens.length != 2 ){
            printError("Actions take exactly 2 arguments");
            return;
        }

        Argument arg1 = getArgument(tokens[0]);
        if(arg1 == null) return;

        Argument arg2 = getArgument(tokens[1]);
        if(arg2 == null) return;

        if( arg1.prefix.equals("t") && arg2.prefix.equals("t") )
            actionListener.onTableauToTableau(arg1.value, arg2.value);
        else
        if( arg1.prefix.equals("t") && arg2.prefix.equals("f") )
            actionListener.onTableauToFoundation(arg1.value, arg2.value);
        else
        if( arg1.prefix.equals("s") && arg2.prefix.equals("f") )
            actionListener.onStockToFoundation(arg1.value, arg2.value);
        else
        if( arg1.prefix.equals("s") && arg2.prefix.equals("t") )
            actionListener.onStockToTableau(arg1.value, arg2.value);
        else
            printError("Unknown argument combination");
    }


    private Argument getArgument(String token){
        try{
            String identifier = token.substring(0,1);
            int value = Integer.parseInt(token.substring(1));
            switch (identifier) {
                case "t":
                    if(value < 0 || value > 8){
                        printError("Tableau index must be between 0 and 9");
                        return null;
                    }
                    break;
                case "f":
                    if(value < 0 || value > 3){
                        printError("Tableau index must be between 0 and 9");
                        return null;
                    }
                    break;
                case "s":
                    if(value < 0 || value > 23){
                        printError("Tableau index must be between 0 and 9");
                        return null;
                    }
                    break;
                default:
                    printError(String.format("Unrecognized argument identifier '%s'", identifier));
                    break;
            }
            return new Argument(identifier, value);
        }catch(NumberFormatException | IndexOutOfBoundsException e ){
            printError(String.format("Argument '%s' is not correct format (should be single identifier followed by index)", token));
            return null;
        }
    }


    private static String[] getTokens(String input){
        List<String> tokens = new ArrayList<>();
        StringTokenizer defaultTokenizer = new StringTokenizer(input);
        while( defaultTokenizer.hasMoreTokens() ){
            String tokenString = defaultTokenizer.nextToken();
            tokens.add(tokenString);
        }
        return tokens.toArray(new String[]{});
    }


    public interface ActionListener {
        void onTableauToTableau(int sourceIndex, int targetIndex);
        void onTableauToFoundation(int tableauIndex, int foundationIndex);
        void onStockToTableau(int stockIndex, int tableauIndex);
        void onStockToFoundation(int stockIndex, int foundationIndex);
    }


    /**
     * Helper methods for the ManualCV and AI, in order to pass command inputs
     * (i.e. 't1 t2' to move from tableau 1 to 2).
     */
    public static class Argument {
        public String prefix;
        public int value;

        public Argument(String prefix, int value) {
            this.prefix = prefix;
            this.value = value;
        }
    }
}
