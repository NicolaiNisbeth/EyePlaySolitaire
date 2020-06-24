package ai.agent;

import ai.heuristic.Heuristic;
import ai.state.ActionFinder51;
import ai.state.ActionFinder52;
import ai.state.State;
import ai.action.Action;

import java.util.List;

public class RandomAgent implements Agent {

    private ActionFinder52 finder = new ActionFinder52();
    private BiCycleHandler handler;

    public RandomAgent(Heuristic heuristic){
        handler = new BiCycleHandler(heuristic);
    }

    @Override
    public Action getAction(State state) {
        if(handler.isLoop(state)){
            return handler.getOutOfLoop(state);
        }
        List<Action> actionList = finder.getActions(state);
        if(actionList.isEmpty()) return null;
        int count = actionList.size();
        int choice = (int) (Math.random() * count);
        return actionList.get(choice);
    }
}
