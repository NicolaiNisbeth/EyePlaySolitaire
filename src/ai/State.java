package ai;

import ai.action.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class State {

    //Bunken
    private Stockpile stockpile;

    //Byggestabler
    private CardPile[] cardPiles;

    //Grundbunker
    private ArrayList<Stack<Card>> foundations;

    public State(Stockpile stockpile, CardPile[] cardPiles, ArrayList<Stack<Card>> foundations){
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

    public CardPile[] shallowCopyCardPiles() {
        return Arrays.copyOf(cardPiles, cardPiles.length);
    }

    public ArrayList<Stack<Card>> getFoundations() {
        return foundations;
    }

    public ArrayList<Stack<Card>> shallowCopyFoundations() {
        return new ArrayList<>(foundations);
    }

    @Override
    public String toString() {
        return "State{" +
                "stockpile=" + stockpile +
                ", cardPiles=" + Arrays.toString(cardPiles) +
                ", foundations=" + foundations +
                '}';
    }
}
