package ai.agent;

import ai.action.Action;
import ai.action.TableauToFoundation;
import ai.action.TableauToTableau;
import ai.state.ActionFinder52;
import ai.state.Card;
import ai.state.IActionFinder;
import ai.state.State;

import java.util.*;

public class IfAgent implements Agent {
    private IActionFinder actionFinder = new ActionFinder52();

    @Override
    public Action getAction(State state) {

        List<Action> actions = actionFinder.getActions(state);

        // TODO: return list of actions that satisfies the condition
        Action easier = kingToEmptyTableau(state, actions);
        Action easy = easyFoundationAction(state, actions);
        Action medium = exposeHiddenCards(state, actions);
        Action hard = exposeTableauIfKingCanMove(state, actions);
        Action harder = redOrBlack(state, actions);
        //Action hardest = findOptimalAction();

        // TODO: traverse action list and decide optimal action

        return null;
    }

    private Action redOrBlack(State state, List<Action> actions) {


        return null;
    }

    private Action exposeTableauIfKingCanMove(State state, List<Action> actions) {
        boolean kingCanMove = false;
        for (Stack<Card> stack : state.getTableau().getStacks()){
            for(int i = stack.size()-1; i >= 0; i--){
                Card card = stack.get(i);
                if (card != null && card.getValue() == 13){
                    kingCanMove = true;
                    break;
                }
            }
            if (kingCanMove) break;
        }

        if (kingCanMove){
            for (Action action : actions){
                if (action instanceof TableauToTableau){
                    TableauToTableau tToT = (TableauToTableau) action;
                    Stack<Card> stack = state.getTableau().getStacks().get(tToT.getFrom());
                    Card cardToMove = tToT.getCard();
                    for(int i = stack.size()-1; i >= 0; i--){
                        Card cardInStack = stack.get(i);
                        if (cardToMove.equals(cardInStack) && i == stack.size()-1){
                            return action;
                        }
                    }
                }
                else if (action instanceof TableauToFoundation){
                    TableauToFoundation tToF = (TableauToFoundation) action;
                    Stack<Card> stack = state.getTableau().getStacks().get(tToF.getTableauIndex());
                    Card cardToMove = tToF.getCard();
                    for(int i = stack.size()-1; i >= 0; i--){
                        Card cardInStack = stack.get(i);
                        if (cardToMove.equals(cardInStack) && i == stack.size()-1){
                            return action;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Action exposeHiddenCards(State state, List<Action> actions) {
        HashMap<Action, Integer> map = new HashMap<>();

        for (Action action : actions){
            if (action instanceof TableauToTableau){
                TableauToTableau tToT = (TableauToTableau) action;
                Stack<Card> stack = state.getTableau().getStacks().get(tToT.getFrom());
                Card cardToMove = tToT.getCard();
                for(int i = stack.size()-1; i > 0; i--){
                    Card cardBelow = stack.get(i-1);
                    if (stack.get(i).equals(cardToMove) && cardBelow == null){
                        int hiddenCards = stack.size()-1-i;
                        map.put(action, hiddenCards);
                    }
                }
            }
            else if (action instanceof TableauToFoundation){
                TableauToFoundation tToF = (TableauToFoundation) action;
                Stack<Card> stack = state.getTableau().getStacks().get(tToF.getTableauIndex());
                Card cardToMove = tToF.getCard();
                for(int i = stack.size()-1; i > 0; i--){
                    Card cardBelow = stack.get(i-1);
                    if (stack.get(i).equals(cardToMove) && cardBelow == null){
                        int hiddenCards = stack.size()-1-i;
                        map.put(action, hiddenCards);
                    }
                }
            }
        }

        int maxHidden = Integer.MIN_VALUE;
        Action maxAction = null;
        for (Map.Entry<Action, Integer> entry : map.entrySet()){
            int hiddenCards = entry.getValue();
            if (hiddenCards > maxHidden){
                maxHidden = hiddenCards;
                maxAction = entry.getKey();
            }
        }

        return maxAction;
    }

    private Action kingToEmptyTableau(State state, List<Action> actions) {
        for (Action action : actions){
            if (action instanceof TableauToTableau){
                TableauToTableau tToT = (TableauToTableau) action;
                Card card = tToT.getCard();
                int size = state.getTableau().getStacks().get(tToT.getTo()).size();
                if (card.getValue() == 13 && size == 0){
                    return action;
                }
            }
        }
        return null;
    }

    private Action easyFoundationAction(State state, List<Action> actions){
        int minValue = Integer.MAX_VALUE;
        for (Stack<Card> stack : state.getFoundation().getStacks()){
            int value = stack.isEmpty() ? 0 : stack.peek().getValue();
            if (value < minValue){
                minValue = value;
            }
        }

        for (Action action : actions){
            if (action instanceof TableauToFoundation){
                TableauToFoundation tToF = (TableauToFoundation) action;
                Card card = tToF.getCard();
                if (card.getValue() <= minValue+2)
                    return action;
            }
        }
        return null;
    }

}
