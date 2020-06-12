package ai.agent;

import ai.action.Action;
import ai.action.StockToTableau;
import ai.action.TableauToFoundation;
import ai.action.TableauToTableau;
import ai.state.*;

import java.util.*;

public class IfAgent implements Agent {
    private IActionFinder actionFinder = new ActionFinder52();
    HashSet<State> visitedStates = new HashSet<>();

    @Override
    public Action getAction(State state) {

        List<Action> actions = actionFinder.getActions(state);

        List<Action> easier = kingToEmptyTableau(state, actions);
        List<Action> easy = easyTableauToFoundationAction(state, actions);
        List<Action> medium = exposeHiddenCards(state, actions);
        List<Action> hard = exposeTableauIfKingCanMove(state, actions);
        List<Action> harder = redOrBlack(state, actions);
        //Action hardest = findOptimalAction();

        List<Action> allActions = new ArrayList<>();
        allActions.addAll(easier);
        allActions.addAll(easy);
        allActions.addAll(medium);
        allActions.addAll(hard);
        allActions.addAll(harder);

        Action maxAction = null;

        for(Action action : allActions){
            Collection<State> results = action.getResults(state);
            boolean seen = false;
            for(State result : results) {
                if(visitedStates.contains(result)){
                    seen = true;
                    break;
                }
            }
            if(!seen) {
                maxAction = action;
                break;
            }
        }
        visitedStates.add(state);
        return maxAction;
    }

    private List<Action> redOrBlack(State state, List<Action> actions) {
        List<Action> results = new ArrayList<>();
        List<Action> blackKingActions = new ArrayList<>();
        List<Action> redKingActions = new ArrayList<>();
        for(Action action : actions) {
            boolean kingAction = false;
            Card kingCard = null;
            if(action instanceof StockToTableau) {
                StockToTableau cast = (StockToTableau) action;
                boolean king = cast.getCard().getValue() == 13;
                if(king){
                    kingAction = true;
                    kingCard = cast.getCard();
                }
            }
            if(action instanceof TableauToTableau) {
                TableauToTableau cast = (TableauToTableau) action;
                boolean king = cast.getCard().getValue() == 13;
                if(king){
                    kingAction = true;
                    kingCard = cast.getCard();
                }
            }
            if(kingAction){
                if(kingCard.getColour() == Card.RED){
                    redKingActions.add(action);
                } else {
                    blackKingActions.add(action);
                }
            }
        }

        int blacks = 0, reds = 0;
        List<Stack<Card>> lol = state.getTableau().getStacks();
        for(Stack<Card> stack : lol){
            Card topCard = null;
            int i = 0;
            while(topCard == null && i < stack.size()){
                topCard = stack.get(i++);
            }
            if(topCard == null) continue;
            if(topCard.getValue() == 13) continue;
            for(Card card : stack) {
                if(card.getValue() == 11) {
                    if(card.getColour() == Card.RED)
                        reds++;
                    else
                        blacks++;
                }
            }
        }

        if(blacks > reds){
            results.addAll(blackKingActions);
            results.addAll(redKingActions);
        } else {
            results.addAll(redKingActions);
            results.addAll(blackKingActions);
        }
        return results;
    }

    private List<Action> exposeTableauIfKingCanMove(State state, List<Action> actions) {
        List<Action> results = new ArrayList<>();
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
                    boolean cardIsTopOfStack =  cardToMove.equals(stack.get(0));
                    if(cardIsTopOfStack){
                        results.add(action);
                    }
                }
                else if (action instanceof TableauToFoundation){
                    TableauToFoundation tToF = (TableauToFoundation) action;
                    Stack<Card> stack = state.getTableau().getStacks().get(tToF.getTableauIndex());
                    Card cardToMove = tToF.getCard();
                    boolean cardIsTopOfStack =  cardToMove.equals(stack.get(0));
                    if(cardIsTopOfStack){
                        results.add(action);
                    }
                }
            }
        }
        return results;
    }

    private List<Action> exposeHiddenCards(State state, List<Action> actions) {
        List<Action> results = new ArrayList<>();
        HashMap<Action, Integer> map = new HashMap<>();

        for (Action action : actions){
            if (action instanceof TableauToTableau){
                TableauToTableau tToT = (TableauToTableau) action;
                Stack<Card> stack = state.getTableau().getStacks().get(tToT.getFrom());
                Card cardToMove = tToT.getCard();
                for(int i = stack.size()-1; i > 0; i--){
                    Card cardBelow = stack.get(i-1);
                    if (stack.get(i).equals(cardToMove) && cardBelow == null){
                        map.put(action, i);
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
                        map.put(action, i);
                    }
                }
            }
        }

        int index = 10;
        while(index-- > 0){
            for (Map.Entry<Action, Integer> entry : map.entrySet()) {
                if (entry.getValue() == index) {
                    results.add(entry.getKey());
                }
            }
        }

        return results;
    }

    private List<Action> kingToEmptyTableau(State state, List<Action> actions) {
        List<Action> results = new ArrayList<>();
        for (Action action : actions){
            if (action instanceof TableauToTableau){
                TableauToTableau tToT = (TableauToTableau) action;
                Card card = tToT.getCard();
                int size = state.getTableau().getStacks().get(tToT.getTo()).size();
                if (card.getValue() == 13 && size == 0){
                    results.add(action);
                }
            }
        }
        return results;
    }

    private List<Action> easyTableauToFoundationAction(State state, List<Action> actions){
        List<Action> results = new ArrayList<>();
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
                    results.add(action);
            }
        }
        return results;
    }

}
