package ai.agent;

import ai.action.Action;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.Collection;
import java.util.Iterator;

public class Simulator implements Agent {


    private int simulations;

    public Simulator(int simulations){
        this.simulations = simulations;
    }

    @Override
    public Action getAction(State state) {
        ActionFinder finder = new ActionFinder();
        Collection<Action> actions = finder.getActions(state);
        int max = Integer.MIN_VALUE;
        Action selected = null;
        for(Action action : actions){
            Collection<State> states = action.getResults(state);
            int wins = 0;
            for (int i = 0; i < simulations; i++) {
                while(true){
                    State randomState = random(states);
                    actions = finder.getActions(randomState);
                    if(actions.isEmpty()){
                        if(randomState.isGoal()) wins++;
                        break;
                    }
                    Action randomAction = random(actions);
                    states = randomAction.getResults(randomState);
                }
            }
            if(wins > max){
                max = wins;
                selected = action;
            }
        }
        return selected;
    }

    private <T> T random(Collection<T> collection) {
        int size = collection.size();
        int random = (int)(Math.random() * size);
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < random; i++) {
            iterator.next();
        }
        return iterator.next();
    }

}
