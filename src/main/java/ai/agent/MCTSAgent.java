package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.heuristic.OptionsKnowledgeFoundation;
import ai.state.ActionFinder51;
import ai.state.State;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MCTSAgent implements Agent {

    private final int seconds;
    private Heuristic heuristic;

    private class Tree {
        Node root;

        public Tree(State state) {
            root = new Node(state, null, null);
        }
    }

    private class Node {
        State state;
        Action action;
        Node parent;
        LinkedList<Node> children;
        int visited;
        int wins;

        public Node(State state, Node parent, Action action) {
            this.state = state;
            this.parent = parent;
            this.action = action;
            this.visited = 0;
            this.wins = 0;
            this.children = new LinkedList<>();
        }
    }

    private static final int WIN = 1, LOSS = 0;
    private ActionFinder51 actionFinder;

    public MCTSAgent(int seconds, Heuristic heuristic) {
        this.heuristic = heuristic;
        this.seconds = seconds;
        actionFinder = new ActionFinder51();
    }

    @Override
    public Action getAction(State state) {
        Tree tree = new Tree(state);

        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() < start + seconds * 1000){
            Node selection = selectFrom(tree.root);
            expand(selection);
            for (Node child : selection.children) {
                int outcome = simulate(child);
                backpropagate(child, outcome);
            }
        }

        int max = Integer.MIN_VALUE;
        Node chosen = null;
        for(Node child : tree.root.children){
            if(child.visited > max){
                chosen = child;
                max = child.visited;
            }
        }
        return chosen == null ? null : chosen.action;
    }

    private Node selectFrom(Node node) {
        if (node.children.size() == 0)
            return node;

        Node selection = null;
        for (Node child : node.children) {
            if (selection == null || ucb(node, child) > ucb(node, selection))
                selection = child;
        }

        return selectFrom(selection);
    }

    private double ucb(Node parent, Node node) {
        if (node.visited == 0)
            return Double.MAX_VALUE;
        double winrate = (double) node.wins / node.visited;
        double exploration = Math.sqrt(2 * Math.log(parent.visited) / node.visited);
        return winrate + exploration;
    }

    private void expand(Node node) {
        Iterable<Action> actions = actionFinder.getActions(node.state);
        for (Action action : actions)
            for (State state : action.getResults(node.state))
                node.children.add(new Node(state, node, action));
    }

    private int simulate(Node node) {
        State state = node.state;
        ExpectimaxAgent simulator = new ExpectimaxAgent(0, heuristic);
        Action action = null;
        do{
            action = simulator.getAction(state);
            if(action != null){
                state = getRandom(action.getResults(state));
            }
        } while (action != null);

        return state.isGoal() ? WIN : LOSS;
    }

    private void backpropagate(Node node, int result) {
        do {
            if (result == WIN)
                node.wins++;
            node.visited++;
            node = node.parent;
        } while (node != null);
    }

    private static State getRandom(Collection<State> collection) {
        return collection
                .stream()
                .skip((int)(Math.random() * collection.size()))
                .findFirst()
                .orElse(null);
    }

}


