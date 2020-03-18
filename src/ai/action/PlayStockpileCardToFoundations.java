package ai.action;

import ai.state.Card;
import ai.state.State;
import ai.state.Stockpile;

import java.util.ArrayList;
import java.util.Stack;

public class PlayStockpileCardToFoundations implements Action {

    private int index;

    public PlayStockpileCardToFoundations(int index) {
        this.index = index;
    }

    @Override
    public State getResult(State state) {
        Stockpile stockpile = state.getStockpile().deepCopy();
        Card card = stockpile.takeCard(index);
        ArrayList<Stack<Card>> foundations = state.shallowCopyFoundations();
        Stack<Card> foundation = shallowCopyFoundation(foundations.get(card.getSuit()));
        foundation.push(card);
        foundations.remove(foundations.get(card.getSuit()));
        foundations.add(foundation);
        return new State(stockpile, state.getCardPiles(), foundations);
    }

    private Stack<Card> shallowCopyFoundation(Stack<Card> cards) {
        Stack<Card> newStack = new Stack<>();
        newStack.addAll(cards);
        return newStack;
    }
}
