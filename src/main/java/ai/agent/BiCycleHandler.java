package ai.agent;

import ai.action.Action;
import ai.heuristic.Heuristic;
import ai.state.ActionFinder52;
import ai.state.State;

import java.util.*;

public class BiCycleHandler {

    private final Heuristic heuristic;
    private HashSet<State> visited = new HashSet<>();
    private List<Action> exitPath;
    private boolean navigatingLoop;
    private ActionFinder52 actionFinder = new ActionFinder52();
    private int head;

    public BiCycleHandler(Heuristic heuristic){
        this.heuristic = heuristic;
    }

    public boolean isLoop(State state) {
        boolean seen = visited.contains(state);
        if (navigatingLoop) {
            return true;
        } else {
            visited.add(state);
            return seen;
        }
    }

    public Action getOutOfLoop(State state) {
        if (!navigatingLoop) {
            exitPath = findExitPath(state);
            navigatingLoop = true;
            head = 0;
        }
        if(exitPath == null){
            navigatingLoop = false;
            return null;
        }
        Action nextAction = exitPath.get(head++);
        if (head == exitPath.size()) {
            navigatingLoop = false;
        }
        return nextAction;
    }

    private List<Action> findExitPath(State state) {
        HashSet<State> explored = new HashSet<>();
        List<List<Action>> exitPaths = new ArrayList<>();
        exploreFrom(state, explored, new ArrayList<>(), exitPaths);
        return findBestPath(exitPaths, state);
        //return exitPaths.stream().min(Comparator.comparingInt(List::size)).orElse(null);
    }

    private List<Action> findBestPath(List<List<Action>> exitPaths, State state) {
        double max = Integer.MIN_VALUE;
        List<Action> maxPath = null;
        for (List<Action> path : exitPaths) {
            State finalState = findFinalState(state, path, 0);
            Collection<State> results = path.get(path.size()-1).getResults(finalState);
            double sum = 0;
            for(State result : results) {
                sum += heuristic.evaluate(result);
            }
            double score = sum / results.size();
            if(score > max){
                max = score;
                maxPath = path;
            }
        }
        return maxPath;
    }

    private State findFinalState(State state, List<Action> path, int head) {
        if(head == path.size() - 1) {
            return state;
        } else {
            Action action = path.get(head);
            Collection<State> results = action.getResults(state);
            if(results.size() != 1) throw new IllegalStateException("WTF");
            State result = results.iterator().next();
            return findFinalState(result, path, head+1);
        }
    }

    private void exploreFrom(State state, HashSet<State> explored, ArrayList<Action> path, List<List<Action>> exitPaths) {
        if (explored.contains(state)) {
            return;
        }
        explored.add(state);
        boolean exitNode = !visited.contains(state);
        if (exitNode) {
            exitPaths.add(path);
            return;
        }
        for (Action action : actionFinder.getActions(state)) {
            for(State result : action.getResults(state)) {
                ArrayList<Action> newPath = new ArrayList<>(path);
                newPath.add(action);
                exploreFrom(result, explored, newPath, exitPaths);
            }
        }
    }

}
