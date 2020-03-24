package ai.state;

import ai.action.Action;
import ai.action.StockToFoundation;
import ai.action.StockToTableau;
import ai.action.TableauToFoundation;
import ai.action.TableauToTableau;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

public class ActionFinder {

    public List<Action> getActions(State state) {
        List<Action> actions = new ArrayList<>();
        addStockActions(state, actions);
        addTableauActions(state, actions);
        return actions;
    }

    private void addStockActions(State state, List<Action> actions) {
        List<Card> options = state.getStock().showOptions();
        Foundation foundation = state.getFoundation();
        addStockToFoundationActions(options, foundation, actions);
        Tableau tableau = state.getTableau();
        addStockToTableauActions(options, tableau, actions);
    }

    private void addStockToFoundationActions(List<Card> options, Foundation foundation, List<Action> actions) {
        for (Card card : options) {
            if(fitsOnFoundation(card, foundation))
                actions.add(new StockToFoundation(card));
        }
    }

    private void addStockToTableauActions(List<Card> options, Tableau tableau, List<Action> actions) {
        List<Stack<Card>> stacks = tableau.getStacks();
        for (int i = 0; i < stacks.size(); i++) {
            Stack<Card> stack = stacks.get(i);
            if(stack.isEmpty()) continue;
            Card target = stack.peek();
            for(Card card : options){
                boolean kingToEmptyStack = (target == null && card.getValue() == 13);
                boolean descendingValueAndDifferentColour =
                        target != null &&
                        card.getValue() == target.getValue()-1 &&
                        card.getColour() != target.getColour();
                boolean fits = kingToEmptyStack || descendingValueAndDifferentColour;
                if(fits)
                    actions.add(new StockToTableau(card, i));
            }
        }
    }

    private void addTableauActions(State state, List<Action> actions) {
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();
        addTableauToFoundationActions(tableau, foundation, actions);
        addTableauToTableauActions(tableau, actions);
    }

    private void addTableauToFoundationActions(Tableau tableau, Foundation foundation, List<Action> actions) {
        List<Stack<Card>> stacks = tableau.getStacks();
        for (int i = 0; i < stacks.size(); i++) {
            Stack<Card> stack = stacks.get(i);
            if(stack.isEmpty()) continue;
            Card card = stack.peek();
            if(fitsOnFoundation(card, foundation))
                actions.add(new TableauToFoundation(card, i));
        }
    }

    private void addTableauToTableauActions(Tableau tableau, List<Action> actions) {
        List<Stack<Card>> stacks = tableau.getStacks();
        List<Card> targets = new ArrayList<>();
        for(Stack<Card> stack : stacks) {
            if(stack.isEmpty())
                targets.add(null);
            else
                targets.add(stack.peek());
        }

        for (int i = 0; i < stacks.size(); i++) {
            Stack<Card> source = stacks.get(i);

            List<Card> cardList = new ArrayList<>();
            while(source.size() > 0 && source.peek() != null)
                cardList.add(source.pop());
            for (int j = cardList.size()-1; j >= 0; j--)
                source.push(cardList.get(j));

            for(Card card : cardList){
                for (int j = 0; j < targets.size(); j++) {
                    if(j == i) continue;
                    Card target = targets.get(j);
                    if(fitsOnTableauCard(card, target, source, stacks.get(j)))
                        actions.add(new TableauToTableau(i, j, card));
                }
            }

        }
    }

    private boolean fitsOnTableauCard(Card card, Card target, Stack<Card> sourceStack, Stack<Card> targetStack) {
        if(target == null && card.getValue() == 13){
            boolean flag = false;
            List<Card> cardList = new ArrayList<>();
            for (int i = 0; i < sourceStack.size(); i++){
                Card s = sourceStack.pop();
                cardList.add(s);
                if (s.equals(card) & !sourceStack.isEmpty()){
                    flag = true;
                    break;
                }
            }

            for (Card c : cardList)
                sourceStack.push(c);

            if (flag)
                return true;

        }
        if(target == null)
            return false;
        int maxValueInSource = sourceStack
                .stream()
                .filter(Objects::nonNull)
                .mapToInt(Card::getValue)
                .max()
                .orElseThrow(NoSuchElementException::new);

        int minValueInTarget = targetStack
                .stream()
                .filter(Objects::nonNull)
                .mapToInt(Card::getValue)
                .min()
                .orElseThrow(NoSuchElementException::new);

        if(maxValueInSource >= minValueInTarget)
            return false;

        return card.getValue() == target.getValue() - 1 && card.getColour() != target.getColour();
    }


    private boolean fitsOnFoundation(Card card, Foundation foundation){
        int suit = card.getSuit();
        if(foundation.getStacks().get(suit).isEmpty()){
            return card.getValue() == 1;
        }
        Card top = foundation.peek(suit);
        int topValue = top.getValue();
        return card.getValue() == topValue + 1;
    }

}
