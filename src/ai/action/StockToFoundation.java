package ai.action;

import ai.state.Card;
import ai.state.State;
import ai.state.Stock;

import java.util.ArrayList;
import java.util.Stack;

public class StockToFoundation implements Action {

    private int index;

    public StockToFoundation(int index) {
        this.index = index;
    }

    @Override
    public State getResult(State state) {
        /*
    Stock stock = state.getStock().deepCopy();
        Card card = stock.takeCard(index);
        ArrayList<Stack<Card>> foundations = state.shallowCopyFoundations();
        Stack<Card> foundation = shallowCopyFoundation(foundations.get(card.getSuit()));
        foundation.push(card);
        foundations.remove(foundations.get(card.getSuit()));
        foundations.add(foundation);
        return new State(stock, state.getCardPiles(), foundations);
    */
        return null;
    }

    private Stack<Card> shallowCopyFoundation(Stack<Card> cards) {
        Stack<Card> newStack = new Stack<>();
        newStack.addAll(cards);
        return newStack;
    }
}
