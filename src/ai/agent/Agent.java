package ai.agent;

import ai.state.State;
import ai.action.Action;
import ai.heuristic.Heuristic;

public interface Agent {
    Action getAction(State state);
}
