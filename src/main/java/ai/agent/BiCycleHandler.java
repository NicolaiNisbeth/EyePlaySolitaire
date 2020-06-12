package ai.agent;

import ai.action.Action;
import ai.state.ActionFinder52;
import ai.state.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class BiCycleHandler {

    private HashSet<State> visited = new HashSet<>();
    private List<Action> exitPath;
    private boolean navigatingLoop;
    private ActionFinder52 actionFinder = new ActionFinder52();
    private int head;

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
        return exitPaths.stream().min(Comparator.comparingInt(List::size)).orElse(null);
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
