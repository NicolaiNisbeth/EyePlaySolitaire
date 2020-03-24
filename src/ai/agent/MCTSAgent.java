package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MCTSAgent implements Agent {

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
    private ActionFinder actionFinder;

    public MCTSAgent() {
        actionFinder = new ActionFinder();
    }

    @Override
    public Action getAction(State state) {
        Tree tree = new Tree(state);

        long start = System.currentTimeMillis();
        long time = 0, goal = 1000 * 15;
        while(time < goal){
            Node selection = selectFrom(tree.root);
            expand(selection);
            for (Node child : selection.children) {
                int outcome = simulate(child);
                backpropagate(child, outcome);
            }
            time = System.currentTimeMillis() - start;
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
        List<Action> actions = actionFinder.getActions(state);
        HashMap<Action, Integer> repetitions = new HashMap<>();
        while (actions.size() > 0) {
            int random = (int) (Math.random() * actions.size());
            Action action = actions.get(random);
            //System.out.println(action);
            Integer count = repetitions.get(action);
            if(count == null){
                repetitions.put(action, 1);
            } else {
                if(count >= 100)
                    return LOSS;
                repetitions.put(action, ++count);
            }
            Collection<State> results = action.getResults(state);
            random = (int) (Math.random() * results.size());
            Iterator<State> resultIterator = results.iterator();
            for (int i = 0; i <= random; i++) {
                state = resultIterator.next();
            }
            actions = actionFinder.getActions(state);
        }
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

}


