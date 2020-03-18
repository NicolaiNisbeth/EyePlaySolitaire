package ai.demo;

import ai.action.Action;
import ai.state.Card;
import ai.state.CardPile;
import ai.state.State;
import ai.state.Stock;

import java.util.ArrayList;
import java.util.Stack;

public class Demo {

    private static DemoDeck deck;

    public static void main(String[] args) {
        State state = createDummyState();
        System.out.println(state);
        for(Action action : state.getActions()){
            System.out.println(action);
        }
        if(state.getActions().size() > 0){
            State newS = state.getActions().get(0).getResult(state);
            System.out.println(newS);
        }
    }

    private static State createDummyState() {
        deck = new DemoDeck();
        CardPile[] cardPiles = new CardPile[7];
        cardPiles[0] = new CardPile(deck.draw());
        cardPiles[1] = new CardPile(null, deck.draw());
        cardPiles[2] = new CardPile(null, null, deck.draw());
        cardPiles[3] = new CardPile(null, null, null, deck.draw());
        cardPiles[4] = new CardPile(null, null, null, null, deck.draw());
        cardPiles[5] = new CardPile(null, null, null, null, null, deck.draw());
        cardPiles[6] = new CardPile(null, null, null, null, null, null, deck.draw());
        Card[] stockpileCards = new Card[24];
        for (int i = 0; i < stockpileCards.length; i++) {
            stockpileCards[i] = deck.draw();
        }
        Stock stock = new Stock(stockpileCards);
        ArrayList<Stack<Card>> foundation = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            foundation.add(new Stack<>());
        }
        return new State(stock, cardPiles, foundation);
    }
}
