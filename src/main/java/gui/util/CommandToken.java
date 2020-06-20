package gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Helper methods for the ManualCV and AI, in order to pass command inputs
 * (i.e. 't1 t2' to move from tableau 1 to 2).
 */
public class CommandToken {
    public String prefix;
    public int value;

    public CommandToken(String prefix, int value) {
        this.prefix = prefix;
        this.value = value;
    }

    public boolean hasPrefix(String prefix){
        return this.prefix.equals(prefix);
    }

    public static CommandToken[] fromString(String input){
        List<CommandToken> tokens = new ArrayList<>();
        StringTokenizer defaultTokenizer = new StringTokenizer(input);
        while( defaultTokenizer.hasMoreTokens() ){
            String tokenString = defaultTokenizer.nextToken();
            tokens.add(new CommandToken(tokenString.substring(0,1), Integer.parseInt(tokenString.substring(1))));
        }
        return tokens.toArray(new CommandToken[]{});
    }
}
