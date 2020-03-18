package ai.agent;

import ai.state.State;
import ai.action.Action;
import ai.heuristic.Heuristic;

import java.util.List;

public class RandomAgent implements Agent {

    @Override
    public Action getAction(State state) {
        List<Action> actionList = state.getActions();
        int count = actionList.size();
        int choice = (int) (Math.random() * count);
        return actionList.get(choice);
    }

    @Override
    public void setHeuristic(Heuristic heuristic) {

    }
}
