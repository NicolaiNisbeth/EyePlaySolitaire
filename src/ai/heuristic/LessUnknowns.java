package ai.heuristic;

import ai.state.State;

public class LessUnknowns implements Heuristic {
    @Override
    public double evaluate(State state) {
        int maxUnknowns = 21;
        int unknowns = state.getRemainingCards().getSize();
        return 1 - (double) unknowns / maxUnknowns;
    }
}
