package ai.heuristic;

import ai.state.State;

public interface Heuristic {
    int evaluate(State state);
}
