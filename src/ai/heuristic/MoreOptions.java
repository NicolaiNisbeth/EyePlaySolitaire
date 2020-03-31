package ai.heuristic;

import ai.state.ActionFinder51;
import ai.state.State;

public class MoreOptions implements Heuristic {

    private static final int TARGET_ACTION_COUNT = 15;

    @Override
    public double evaluate(State state) {
        ActionFinder51 actionFinder = new ActionFinder51();
        int actionCount = actionFinder.getActions(state).size();
        if(actionCount == 0 && !state.isGoal())
            return -1;
        return (double) actionCount / TARGET_ACTION_COUNT;
    }
}
