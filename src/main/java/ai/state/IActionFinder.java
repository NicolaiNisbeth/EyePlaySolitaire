package ai.state;

import ai.action.Action;

import java.util.List;

public interface IActionFinder {
    List<Action> getActions(State state);
    void addStockToFoundationActions(List<Card> options, Foundation foundation, List<Action> actions);
    void addStockToTableauActions(List<Card> options, Tableau tableau, List<Action> actions);
    void addTableauToFoundationActions(Tableau tableau, Foundation foundation, List<Action> actions);
    void addTableauToTableauActions(Tableau tableau, List<Action> actions);
}
