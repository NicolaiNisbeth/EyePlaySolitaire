package ai.heuristic;

import ai.state.ActionFinder52;
import ai.state.State;

public class MoreOptions implements Heuristic {

    private static final int TARGET_ACTION_COUNT = 15;

    @Override
    public double evaluate(State state) {
        ActionFinder52 actionFinder = new ActionFinder52();
        int actionCount = actionFinder.getActions(state).size();
        if(state.isGoal()) return 1;
        return (double) actionCount / TARGET_ACTION_COUNT;
    }
}
