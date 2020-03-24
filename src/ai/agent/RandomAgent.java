package ai.agent;

import ai.state.ActionFinder;
import ai.state.State;
import ai.action.Action;
import ai.heuristic.Heuristic;

import java.util.List;

public class RandomAgent implements Agent {

    @Override
    public Action getAction(State state) {
        ActionFinder finder = new ActionFinder();
        List<Action> actionList = finder.getActions(state);
        if(actionList.isEmpty()) return null;
        int count = actionList.size();
        int choice = (int) (Math.random() * count);
        return actionList.get(choice);
    }
}
