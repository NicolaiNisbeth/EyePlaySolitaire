package ai.action;

import ai.State;

public interface Action {
    State getResult(State state);
}
