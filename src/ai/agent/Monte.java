package ai.agent;

import ai.action.Action;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Monte implements Agent {

    private static final int WIN = 1, LOSS = 0;
    private Node root;
    private int iterations;
    private ActionFinder actionFinder = new ActionFinder();

    private static class Node {
        int depth;
        Action action;
        int visits;
        int wins;
        List<Node> children = new ArrayList<>();
        Node parent;
        State state;
        public Node(State state, Node parent, Action action) {
            this.state = state;
            this.parent = parent;
            this.action = action;
        }
    }

    public Monte(int iterations){
        this.iterations = iterations;
    }

    @Override
    public Action getAction(State state) {
        root = findInTree(state);
        root.parent = null;
        while(root.visits < iterations){
            Node selected = selectFrom(root, 0);
            expand(selected);
            for(Node child : selected.children){
                int result = simulate(child);
                backpropagate(result, child);
            }
        }
        int max = Integer.MIN_VALUE;
        Node chosen = null;
        for(Node child : root.children){
            if(child.visits > max){
                chosen = child;
                max = child.visits;
            }
        }
        return chosen == null ? null : chosen.action;
    }

    private Node findInTree(State state) {
        if(root == null)
            return new Node(state, null, null);
        for(Node child : root.children)
            if(child.state.equals(state))
                return child;
        throw new IllegalStateException("Agent did not think state was reachable from previous state");
    }

    private Node selectFrom(Node node, int depth) {
        if (node.children.size() == 0){
            node.depth = depth;
            return node;
        }

        Node selection = null;
        for (Node child : node.children) {
            if (selection == null)
                selection = child;
            if(depth % 2 == 0 && ucb(node, child) > ucb(node, selection))
                selection = child;
            if(depth % 2 == 1 && ucb(node, child) < ucb(node, selection))
                selection = child;
        }

        return selectFrom(selection, ++depth);
    }

    private double ucb(Node parent, Node node) {
        if (node.visits == 0)
            return Double.MAX_VALUE;
        double winrate = (double) node.wins / node.visits;
        double exploration = Math.sqrt(2 * Math.log(parent.visits) / node.visits);
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
        HashSet<Action> repetitions = new HashSet<>();
        while (actions.size() > 0) {
            List<Action> actualActions = new ArrayList<>();
            for(Action action : actions){
                if(repetitions.contains(action))
                    continue;
                repetitions.add(action);
                actualActions.add(action);
            }
            if(actualActions.isEmpty())
                return LOSS;
            int random = (int) (Math.random() * actualActions.size());
            Action action = actualActions.get(random);
            //System.out.println(action);
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

    private void backpropagate(int result, Node node) {
        while(node != null){
            if(result == WIN)
                node.wins++;
            node.visits++;
            node = node.parent;
        }
    }

}
