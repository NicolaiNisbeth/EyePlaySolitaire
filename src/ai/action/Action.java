package ai.action;

import ai.state.State;

public interface Action {
    State getResult(State state);
}
