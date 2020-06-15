package ai.state;

import ai.action.Action;

import java.util.*;

public class CycleHandler {
    private int head;
    private IActionFinder actionFinder;
    private HashSet<State> loopStates;
    private HashSet<State> visitedStates = new HashSet<>(); // maybe move responsibility to agent

    public CycleHandler(IActionFinder actionFinder) {
        this.actionFinder = actionFinder;
    }

    public List<Action> getAllExitActions(State root) {
        head = 0;
        loopStates = new HashSet<>();
        List<Action> exitActions = new ArrayList<>();
        computeAllExitActions(root, loopStates, exitActions);
        return exitActions;
    }

    public Action getNextActionOnExitPath(State root, Action maxAction) {
        List<List<Action>> exitPaths = new ArrayList<>();
        dfs(root, maxAction, exitPaths, new ArrayList<>(), new HashSet<>());
        List<Action> fewestActions = null;
        int min = Integer.MAX_VALUE;
        for (List<Action> actions : exitPaths){
            if (actions.size() < min){
                fewestActions = actions;
                min = actions.size();
            }
        }
        return fewestActions != null ? fewestActions.get(head++) : null;
    }

    private void computeAllExitActions(State state, HashSet<State> loopStates, List<Action> exitPoints) {
        if (!visitedStates.contains(state) || loopStates.contains(state))
            return;

        loopStates.add(state);
        for (Action action : actionFinder.getActions(state)){
            Collection<State> results = action.getResults(state);
            if (results.size() > 1)
                exitPoints.add(action);
            else {
                State result = results.iterator().next();
                if (!visitedStates.contains(result))
                    exitPoints.add(action);
                else {
                    if (!loopStates.contains(result))
                        computeAllExitActions(result, loopStates, exitPoints);
                }
            }
        }
    }

    private void dfs(State state, Action maxExitPoint, List<List<Action>> exitPaths, List<Action> path, HashSet<State> visited) {
        visited.add(state);
        for (Action action : actionFinder.getActions(state)){
            path.add(action);
            if (action.equals(maxExitPoint)){
                exitPaths.add(new ArrayList<>(path));
                return;
            }
            Collection<State> results = action.getResults(state);
            for (State result : results){
                if (loopStates.contains(result) && !visited.contains(result))
                    dfs(result, maxExitPoint, exitPaths, path, visited);
            }
            path.remove(action);
        }
    }

    public boolean isCycle(State root) {
        return visitedStates.contains(root);
    }

    public void markVisited(State root) {
        visitedStates.add(root);
    }
}
