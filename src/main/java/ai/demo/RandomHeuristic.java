package ai.demo;

import ai.heuristic.Heuristic;
import ai.state.State;

public class RandomHeuristic implements Heuristic {
    @Override
    public double evaluate(State state) {
        return Math.random();
    }
}
