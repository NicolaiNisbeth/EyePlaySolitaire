package ai;

import ai.action.Action;

import java.util.List;
import java.util.Stack;

public class State {

    //Bunken
    private Stockpile stockpile;

    //Byggestabler
    private CardPile[] cardPiles;

    //Grundbunker
    private Stack<Card>[] foundations;

    public State(Stockpile stockpile, CardPile[] cardPiles, Stack<Card>[] foundations){
        this.stockpile = stockpile;
        this.cardPiles = cardPiles;
        this.foundations = foundations;
    }

    public List<Action> getActions() {
        return null;
    }


    public Stockpile getStockpile() {
        return stockpile;
    }

    public CardPile[] getCardPiles() {
        return cardPiles;
    }

    public Stack<Card>[] getFoundations() {
        return foundations;
    }
}
