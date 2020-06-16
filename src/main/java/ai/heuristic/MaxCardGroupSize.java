package ai.heuristic;

import ai.state.Card;
import ai.state.State;
import ai.state.Tableau;

import java.util.Stack;

public class MaxCardGroupSize implements Heuristic {



    @Override
    public double evaluate(State state) {
        int odd = 0, even = 0;
        Tableau tableau = state.getTableau();
        for (Stack<Card> stack : tableau.getStacks()) {
            for (Card card : stack) {
                if(card == null) continue;
                if(card.getColour() == Card.RED) {
                    int value = card.getValue();
                    if(value % 2 == 0) {
                        odd++;
                    } else {
                        even++;
                    }
                } else {
                    int value = card.getValue();
                    if(value % 2 == 0) {
                        even++;
                    } else {
                        odd++;
                    }
                }
            }
        }
        if (odd == 0 && even == 0) return 0;
        return Math.max(odd, even) / (double)(odd+even);
    }

}
