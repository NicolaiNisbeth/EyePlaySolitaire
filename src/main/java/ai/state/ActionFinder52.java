package ai.state;

import ai.action.Action;
import ai.action.StockToFoundation;
import ai.action.StockToTableau;
import ai.action.TableauToFoundation;
import ai.action.TableauToTableau;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActionFinder52 implements IActionFinder {

    @Override
    public List<Action> getActions(State state) {
        List<Action> actions = new ArrayList<>();

        List<Card> options = state.getStock().showOptions();
        Foundation foundation = state.getFoundation();
        Tableau tableau = state.getTableau();

        addStockToFoundationActions(options, foundation, actions);
        addStockToTableauActions(options, tableau, actions);
        addTableauToFoundationActions(tableau, foundation, actions);
        addTableauToTableauActions(tableau, actions);
        return actions;
    }

    @Override
    public void addStockToFoundationActions(List<Card> options, Foundation foundation, List<Action> actions) {
        for (Card option : options){
            for (int i=0; i<foundation.getStacks().size(); i++){
                Stack<Card> stack = foundation.getStacks().get(i);
                int requiredValue = stack.isEmpty() ? 1 : stack.peek().getValue() + 1;
                if (option.getValue() == requiredValue && option.getSuit() == i)
                    actions.add(new StockToFoundation(option));
            }
        }
    }

    @Override
    public void addStockToTableauActions(List<Card> options, Tableau tableau, List<Action> actions) {
        for (Card option : options){
            for (int i=0; i<tableau.getStacks().size(); i++){
                Stack<Card> stack = tableau.getStacks().get(i);
                int requiredValue = stack.isEmpty() ? 13 : stack.peek().getValue() - 1;
                int requiredColor = stack.isEmpty() ? option.getColour() : stack.peek().getColour() == Card.RED ? Card.BLACK : Card.RED;
                if (option.getValue() == requiredValue && option.getColour() == requiredColor){
                    actions.add(new StockToTableau(option, i));
                }
            }
        }
    }

    @Override
    public void addTableauToFoundationActions(Tableau tableau, Foundation foundation, List<Action> actions) {
        List<Pair<Card, Integer>> options = new ArrayList<>();
        for (int i=0; i<tableau.getStacks().size(); i++){
            Stack<Card> stack = tableau.getStacks().get(i);
            if (stack.isEmpty()) continue;
            Card card = stack.peek();
            if (card == null) throw new IllegalStateException();
            options.add(new Pair<>(card, i));
        }

        for (Pair<Card, Integer> option : options){
            for (int i=0; i<foundation.getStacks().size(); i++){
                Stack<Card> stack = foundation.getStacks().get(i);
                int requiredValue = stack.isEmpty() ? 1 : stack.peek().getValue() + 1;
                Card card = option.getKey();
                if (card.getValue() == requiredValue && card.getSuit() == i)
                    actions.add(new TableauToFoundation(card, option.getValue()));
            }
        }
    }

    @Override
    public void addTableauToTableauActions(Tableau tableau, List<Action> actions) {
        for (int i=0; i<tableau.getStacks().size(); i++){
            Stack<Card> stack = tableau.getStacks().get(i);
            for (Card card : stack){
                if (card == null) continue;
                for (int j = 0; j < tableau.getStacks().size(); j++) {
                    //if(i == j) continue;
                    if (tableau.getStacks().get(j).isEmpty()){
                        if (card.getValue() == 13 && stack.contains(null)){
                            actions.add(new TableauToTableau(i, j, card));
                        }
                    }
                    else {
                        Card target = tableau.getStacks().get(j).peek();
                        if (card.getColour() != target.getColour() && card.getValue() == target.getValue()-1){
                            actions.add(new TableauToTableau(i, j, card));
                        }
                    }
                }
            }
        }
    }
}
