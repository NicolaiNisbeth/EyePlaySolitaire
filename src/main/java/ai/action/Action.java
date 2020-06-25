package ai.action;

import ai.state.State;
import gui.gamescene.aiinterface.IActionPrompter;

import java.util.Collection;

public interface Action {
    Collection<State> getResults(State state);
    void prompt(IActionPrompter prompter, State state);
}
