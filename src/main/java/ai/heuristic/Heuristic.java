package ai.heuristic;

import ai.state.State;

public interface Heuristic {
    double evaluate(State state);
}
