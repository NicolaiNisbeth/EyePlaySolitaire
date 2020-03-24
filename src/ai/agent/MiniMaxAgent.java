package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.Collection;

public class MiniMaxAgent implements Agent {
    private ActionFinder actionFinder = new ActionFinder();
    private int maxDepth;
    private Heuristic heuristic;

    public MiniMaxAgent(int maxDepth, Heuristic heuristic){
        this.maxDepth = maxDepth;
        this.heuristic = heuristic;
    }

    @Override
    public Action getAction(State root) {
        double maxValue = 0;
        Action maxAction = null;
        for(Action action : actionFinder.getActions(root)){
            double value = min(root, action, 1, Double.MIN_VALUE, Double.MAX_VALUE);
            if(value > maxValue){
                maxValue = value;
                maxAction = action;
            }
        }
        return maxAction;
    }

    private double min(State state, Action action, int depth, double alpha, double beta) {
        double min = Double.MAX_VALUE;
        Collection<State> results = action.getResults(state);
        for(State result : results){
            double value = max(result, depth, alpha, beta);
            if(value < min)
                min = value;
        }
        return min;
    }

    private double max(State state, int depth, double alpha, double beta) {
        if(depth >= maxDepth)
            return heuristic.evaluate(state);

        Collection<Action> actions = actionFinder.getActions(state);
        double maxValue = 0;
        for(Action action : actions){
            double value = min(state, action, depth+1, alpha, beta);
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue;
    }

}
