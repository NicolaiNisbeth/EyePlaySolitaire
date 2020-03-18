package ai.agent;

import ai.State;
import ai.action.Action;
import ai.heuristic.Heuristic;

public interface Agent {
    Action getAction(State state);
    void setHeuristic(Heuristic heuristic);
}
