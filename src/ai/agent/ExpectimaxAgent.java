package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder;
import ai.state.State;

/**
 * https://en.wikipedia.org/wiki/Expectiminimax
 * https://github.com/DanijelAskov/expectiminimax-backgammon/blob/master/src/main/java/askov/schoolprojects/ai/expectiminimaxbackgammon/gamelogic/player/ExpectiminimaxPlayer.java
 */
public class ExpectimaxAgent implements Agent {
    private ActionFinder actionFinder = new ActionFinder();

    @Override
    public Action getAction(State state) {
        int maxValue = Integer.MIN_VALUE;
        Action maxAction = null;

        for(Action action : actionFinder.getActions(state)){
            /*
            State child = action.getResult(state);
            int value = expectiminimax(child);
            if(value > maxValue){
                maxValue = value;
                maxAction = action;
            }
             */
        }

        return maxAction;
    }

    private int expectiminimax(State state) {
        int depthLimit = 3;
        return max(state, depthLimit);
    }

    private int max(State state, int depthLimit) {
        if (actionFinder.getActions(state).isEmpty() || depthLimit == 0)
            return -1; // TODO calculate heuristic value

        int value = Integer.MIN_VALUE;
        for(Action action : actionFinder.getActions(state)){
            /*
            State child = action.getResult(state);
            value = Math.max(value, chance(child, depthLimit-1));

             */
        }
        return value;
    }

    private int chance(State state, int depthLimit) {
        if (actionFinder.getActions(state).isEmpty() || depthLimit == 0)
            return -1; // TODO calculate heuristic value

        int value = 0;
        for(Action action : actionFinder.getActions(state)){
            /*
            State child = action.getResult(state);
            value += (probability(child) * max(child, depthLimit-1));

             */
        }

        return value;
    }

    private int probability(State child) {
        // TODO: probability of cards given state
        return -1;
    }


    @Override
    public void setHeuristic(Heuristic heuristic) {

    }
}
