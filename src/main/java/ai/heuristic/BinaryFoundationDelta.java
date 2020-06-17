package ai.heuristic;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.State;

import java.util.Stack;

public class BinaryFoundationDelta implements Heuristic {

    @Override
    public double evaluate(State state) {
        Foundation foundation = state.getFoundation();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(Stack<Card> stack : foundation.getStacks()) {
            int value = stack.isEmpty() ? 0 : stack.peek().getValue();
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        return max-min > 2 ? 0 : 1;
    }

}
