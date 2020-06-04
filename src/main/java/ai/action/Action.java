package ai.action;

import ai.state.State;
import gui.gamescene.aiinterface.IGamePrompter;

import java.util.Collection;

public interface Action {
    Collection<State> getResults(State state);
    void prompt(IGamePrompter prompter, State state);
}
