package ai.heuristic;

import ai.state.State;

public class FoundationSize implements Heuristic {
    @Override
    public double evaluate(State state) {
        int maxFoundationSize = 52;
        int foundationSize = state.getFoundation().getCount();
        return (double) foundationSize / maxFoundationSize;
    }
}
