package ai.heuristic;

import ai.state.Card;
import ai.state.State;

import java.util.Stack;
import java.util.function.Predicate;

public class BinaryKingAvailableForEmptySpace implements Heuristic {

    @Override
    public double evaluate(State state) {
        boolean emptySpot = false;
        for (Stack<Card> stack : state.getTableau().getStacks()) {
            if(stack.isEmpty()) {
                emptySpot = true;
                break;
            }
        }
        if(!emptySpot) {
            return 0;
        }

        boolean hasKingToMove = false;
        for (Card card : state.getStock().showOptions()) {
            if(card.getValue() == 13) {
                hasKingToMove = true;
                break;
            }
        }

        if(hasKingToMove) {
            return 1;
        }

        for (Stack<Card> stack : state.getTableau().getStacks()) {
            if(stack.isEmpty()) continue;
            for (int i = 1; i < stack.size(); i++) {
                Card card = stack.get(i);
                if(card == null) continue;
                if(card.getValue() == 13) {
                    hasKingToMove = true;
                    break;
                }
            }
            if(hasKingToMove) {
                break;
            }
        }

        return hasKingToMove ? 1 : 0;
    }

}
