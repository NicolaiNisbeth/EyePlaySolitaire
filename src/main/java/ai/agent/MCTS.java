package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.State;

import java.util.ArrayList;
import java.util.List;

public class MCTS implements Agent {

    private Node root;

    private final int milliseconds;
    private final BiCycleHandler handler;

    public MCTS(int milliseconds, Heuristic heuristic) {
        this.milliseconds = milliseconds;
        this.handler = new BiCycleHandler(heuristic);
    }

    @Override
    public Action getAction(State state) {
        return null;
    }

    private static class Node {

        State state;
        Action action;
        Node parent;
        List<Node> children = new ArrayList<>();

        int visits;
        int wins;

        public Node(State state, Node parent, Action action) {
            this.state = state;
            this.parent = parent;
            this.action = action;
        }

    }




}
