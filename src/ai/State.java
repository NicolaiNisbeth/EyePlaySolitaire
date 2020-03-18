package ai;

import ai.action.Action;

import java.util.List;
import java.util.Stack;

public class State {

    //Bunken
    Stockpile stockpile;

    //Byggestabler
    CardPile[] cardPiles;

    //Grundbunker
    Stack<Card>[] foundations;

    public State(Stockpile stockpile, CardPile[] cardPiles, Stack<Card>[] foundations){
        this.stockpile = stockpile;
        this.cardPiles = cardPiles;
        this.foundations = foundations;
    }

    public List<Action> getActions() {
        return null;
    }


}
