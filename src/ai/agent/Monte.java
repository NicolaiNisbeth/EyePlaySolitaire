package ai.agent;

import ai.action.Action;
import ai.state.ActionFinder;
import ai.state.State;

import java.util.Collection;
import java.util.List;

public class Monte implements Agent {

    private Node root;
    private int iterations;
    private ActionFinder actionFinder = new ActionFinder();

    private static class Node {
        int visits;
        int wins;
        List<Node> children;
        List<State> states;
    }

    public Monte(int iterations){
        this.iterations = iterations;
    }

    @Override
    public Action getAction(State state) {
        root = findInTree(state);
        while(root.visits < iterations){
            Node selected = selectFrom(root);
            expand(selected);
        }
        return null;
    }

    private Node findInTree(State state) {
        if(root == null)
            return new Node();
        for(Node child : root.children)
            if(child.states.contains(state))
                return child;
        throw new IllegalStateException("Agent did not think state was reachable from previous state");
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
        if (node.visits == 0)
            return Double.MAX_VALUE;
        double winrate = (double) node.wins / node.visits;
        double exploration = Math.sqrt(2 * Math.log(parent.visits) / node.visits);
        return winrate + exploration;
    }

    /*
    Iterable<Action> actions = actionFinder.getActions(node.state);
        for (Action action : actions)
            for (State state : action.getResults(node.state))
                node.children.add(new Node(state, node, action));
     */
    private void expand(Node node) {
        int random = (int)(Math.random() * node.children.size());
        State state = node.states.get(random);
        Iterable<Action> actions = actionFinder.getActions(state);
        for(Action action : actions){
            //node.children.add()
        }
    }

    private Node findInChildren(State state) {
        return null;
    }

}
