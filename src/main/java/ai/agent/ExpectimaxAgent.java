package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder51;
import ai.state.ActionFinder52;
import ai.state.IActionFinder;
import ai.state.State;

import java.util.Collection;
import java.util.List;

/**
 * https://en.wikipedia.org/wiki/Expectiminimax
 * https://github.com/DanijelAskov/expectiminimax-backgammon/blob/master/src/main/java/askov/schoolprojects/ai/expectiminimaxbackgammon/gamelogic/player/ExpectiminimaxPlayer.java
 */
public class ExpectimaxAgent implements Agent {
    private IActionFinder actionFinder = new ActionFinder52();
    private int maxDepth;
    private Heuristic heuristic;
    private long counter = 0;

    public ExpectimaxAgent(int maxDepth, Heuristic heuristic){
        this.maxDepth = maxDepth;
        this.heuristic = heuristic;
    }

    @Override
    public Action getAction(State root) {
        double maxValue = Double.MIN_VALUE;
        Action maxAction = null;
        List<Action> actions = actionFinder.getActions(root);
        for(Action action : actions){
            double value = actionValue(root, action, 1);
            if(value > maxValue){
                maxValue = value;
                maxAction = action;
            }
        }
        return maxAction;
    }

    private double actionValue(State state, Action action, int depth) {
        double sum = 0;
        Collection<State> results = action.getResults(state);
        for(State result : results){
            double value = stateValue(result, depth);
            sum += value;
        }
        return sum / results.size();
    }

    private double stateValue(State state, int depth) {
        Collection<Action> actions = actionFinder.getActions(state);
        if(depth >= maxDepth || actions.isEmpty()){
            counter++;
            return heuristic.evaluate(state);
        }

        double maxValue = Double.MIN_VALUE;
        for(Action action : actions){
            double value = actionValue(state, action, depth+1);
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue;
    }

    public long getCounter() {
        return counter;
    }
}