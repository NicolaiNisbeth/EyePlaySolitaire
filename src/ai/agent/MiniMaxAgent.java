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
    private long counter = 0;

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
            min = Math.min(min, max(result, depth, alpha, beta));
            beta = Math.min(min, beta);

            // alpha-cut
            if(min <= alpha)
                break;
        }
        return min;
    }

    private double max(State state, int depth, double alpha, double beta) {
        if(depth >= maxDepth){
            counter++;
            return heuristic.evaluate(state);
        }

        Collection<Action> actions = actionFinder.getActions(state);
        double maxValue = Double.MIN_VALUE;
        for(Action action : actions){
            maxValue = Math.max(maxValue, min(state, action, depth+1, alpha, beta));
            alpha = Math.max(maxValue, alpha);

            if(maxValue >= beta)
                break;
        }
        return maxValue;
    }

    public long getCounter() {
        return counter;
    }
}
