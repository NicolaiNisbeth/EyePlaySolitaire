package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.Collection;

/**
 * https://en.wikipedia.org/wiki/Expectiminimax
 * https://github.com/DanijelAskov/expectiminimax-backgammon/blob/master/src/main/java/askov/schoolprojects/ai/expectiminimaxbackgammon/gamelogic/player/ExpectiminimaxPlayer.java
 */
public class ExpectimaxAgent implements Agent {
    private ActionFinder actionFinder = new ActionFinder();
    private int depthLimit;
    private Heuristic heuristic;

    public ExpectimaxAgent(int depthLimit, Heuristic heuristic){
        this.depthLimit = depthLimit;
        this.heuristic = heuristic;
    }

    @Override
    public Action getAction(State root) {
        double maxValue = 0;
        Action maxAction = null;
        for(Action action : actionFinder.getActions(root)){
            double value = actionValue(root, action, 0);
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
            double value = stateValue(result, depth + 1);
            sum += value;
        }
        return sum / results.size();
    }

    private double stateValue(State state, int depth) {
        if(depth > depthLimit)
            return heuristic.evaluate(state);

        Collection<Action> actions = actionFinder.getActions(state);
        double maxValue = 0;
        for(Action action : actions){
            double value = actionValue(state, action, depth);
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue;
    }

}
