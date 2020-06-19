package ai.agent;

import ai.action.*;
import ai.demo.SolitaireAI;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder52;
import ai.state.Card;
import ai.state.State;
import ai.state.Stock;
import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.gamestate.GameState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MCTSGuiAgent implements ISolitaireAI {

    private final int milliseconds;
    private final Heuristic heuristic;
    private final BiCycleHandler handler;
    private Node root;

    @Override
    public void computeAction(GameState gameState, IGamePrompter prompter) throws IllegalGameStateException {
        throw new NotImplementedException();
    }

    @Override
    public void startActionComputation(GameState gameState) throws IllegalGameStateException {
        calculating = true;
        final State state = SolitaireAI.stateConverter(gameState);
        currentState = state;
        calculator = new Thread(() -> calculateAction(state));
        calculator.start();
    }

    @Override
    public void endActionComputation(IGamePrompter prompter) {
        calculating = false;
        if(calculator == null || currentState == null) {
            throw new IllegalStateException("Must call startActionComputation first");
        }
        while(calculator.isAlive());
        if(bestAction == null){
            prompter.gameLost();
        } else if(bestAction instanceof TableauToTableau) {
            TableauToTableau action = (TableauToTableau) bestAction;
            action.prompt(prompter, currentState);
        } else if(bestAction instanceof TableauToFoundation) {
            TableauToFoundation action = (TableauToFoundation) bestAction;
            action.prompt(prompter, currentState);
        } else if(bestAction instanceof StockToFoundation) {
            StockToFoundation action = (StockToFoundation) bestAction;
            action.prompt(prompter, currentState);
        } else if (bestAction instanceof StockToTableau) {
            StockToTableau action = (StockToTableau) bestAction;
            action.prompt(prompter, currentState);
        }
        //throw new IllegalStateException("break");
    }

    private Thread calculator;
    private State currentState;
    private boolean calculating = true;
    private Action bestAction = null;

    private static class Node {
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
    private final ActionFinder52 actionFinder;

    public MCTSGuiAgent(int milliseconds, Heuristic heuristic) {
        this.handler = new BiCycleHandler(heuristic);
        this.heuristic = heuristic;
        this.milliseconds = milliseconds;
        actionFinder = new ActionFinder52();
    }

    public void calculateAction(State state) {
        root = findRoot(state);

        boolean isLoop = handler.isLoop(state);
        if(isLoop) {
            bestAction = handler.getOutOfLoop(state);
            return;
        }

        do {
            Node selection = root;
            while(selection.children.size() != 0){
                selection = selectNext(selection);
            }
            expand(selection);
            for (Node child : selection.children) {
                int outcome = simulate(child);
                backpropagate(child, outcome);
            }
        } while(calculating);

        int max = Integer.MIN_VALUE;
        Node chosen = null;
        for(Node child : root.children){
            if(child.visited > max){
                chosen = child;
                max = child.visited;
            }
        }
        bestAction = chosen == null ? null : chosen.action;
    }

    private Node findRoot(State state) {
        if(root == null)
            return new Node(state, null, null);
        Node newRoot = null;
        List<Node> children = root.children;
        for(Node child : children) {
            if(child.state.equals(state))
                newRoot = child;
        }
        if(newRoot == null) {
            System.out.println("AAAA?");
            return new Node(state, null, null);
        }
        newRoot.parent = null;
        return newRoot;
    }

    private Node selectNext(Node node) {
        if(node.children.size() == 0)
            throw new IllegalStateException("Only nodes with children (it's my fetish)");

        Node selection = null;
        for (Node child : node.children) {
            if (selection == null || ucb(node, child) > ucb(node, selection))
                selection = child;
        }
        return selection;
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
        List<Action> actions = actionFinder.getActions(node.state);
        for (Action action : actions)
            for (State state : action.getResults(node.state))
                node.children.add(new Node(state, node, action));
    }

    private int simulate(Node node) {
        State state = node.state;
        Action action = null;
        BiCycleHandler simulHandler = new BiCycleHandler(heuristic);
        do{
            boolean isLoop = simulHandler.isLoop(state);
            if(isLoop) {
                action = simulHandler.getOutOfLoop(state);
            } else {
                List<Action> actions = actionFinder.getActions(state);
                int random = (int)(Math.random() * actions.size());
                action = actions.isEmpty() ? null : actions.get(random);
            }
            if(action != null){
                state = getRandom(action.getResults(state));
            }
        } while (action != null);

        //return state.getFoundation().getCount();
        return state.isGoal() ? WIN : LOSS; // Can change to foundation size heuristic
    }

    //TODO : average foundation instead of sum
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


