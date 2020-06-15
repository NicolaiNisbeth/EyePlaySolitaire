package ai.heuristic;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.State;
import ai.state.Tableau;

import java.util.Stack;

public class HasNextInChain implements Heuristic {
    @Override
    public double evaluate(State state) {
        Tableau tableau = state.getTableau();
        int[] amount = new int[26];
        for (Stack<Card> stack : tableau.getStacks()) {
            for (Card card : stack) {
                if(card == null) continue;
                amount[card.getValue() - 1 + card.getColour() * 13]++;
            }
        }
        for (Card card : state.getStock().showOptions()) {
            amount[card.getValue() - 1 + card.getColour() * 13]++;
        }
        Foundation foundation = state.getFoundation();
        for (Stack<Card> stack : foundation.getStacks()) {
            if(stack.isEmpty()) continue;
            for (int i = 0; i < stack.size(); i++) {
                Card card = stack.get(i);
                amount[card.getValue() - 1 + card.getColour() * 13]++;
            }
        }
        int total = 0, has = 0;
        for (Stack<Card> stack : tableau.getStacks()) {
            for (Card card : stack) {
                if(card == null) continue;
                total++;
                if(amount[card.getValue() - 1 + card.getColour() * 13]-- > 0){
                    has++;
                }
            }
        }
        return (double) has/total;
    }
}
