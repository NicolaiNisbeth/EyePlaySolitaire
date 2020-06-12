package ai.heuristic;

import ai.state.State;

public class LessUnknowns implements Heuristic {
    // TODO: Expose hidden cards. If you have a choice from several possible moves that expose hidden cards, choose column with the largest number of hidden cards.

    @Override
    public double evaluate(State state) {
        int maxUnknowns = 21;
        int unknowns = state.getRemainingCards().getSize();
        return 1 - (double) unknowns / maxUnknowns;
    }
}
