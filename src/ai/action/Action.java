package ai.action;

import ai.state.State;

import java.util.Collection;

public interface Action {
    Collection<State> getResult(State state);
}
