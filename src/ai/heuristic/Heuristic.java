package ai.heuristic;

import ai.state.State;

public interface Heuristic {
    int getEvaluation(State state);
}
