package ai.heuristic;

import ai.State;

public interface Heuristic {
    int getEvaluation(State state);
}
