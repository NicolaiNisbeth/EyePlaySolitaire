package ai.action;

import ai.state.Card;
import ai.state.CardPile;
import ai.state.State;
import ai.state.Stockpile;

import java.util.Stack;

public class PlayStockpileCardToCardPiles implements Action {

    private int index;
    private int cardPileIndex;

    public PlayStockpileCardToCardPiles(int stockpileIndex, int cardPileIndex) {
        this.index = stockpileIndex;
        this.cardPileIndex = cardPileIndex;
    }

    @Override
    public State getResult(State state) {
        Stockpile stockpile = state.getStockpile().deepCopy();
        Card card = stockpile.takeCard(index);
        CardPile[] cardPiles = state.shallowCopyCardPiles();
        CardPile newCardPile = cardPiles[cardPileIndex].deepCopy();
        newCardPile.addLast(card);
        cardPiles[cardPileIndex] = newCardPile;
        return new State(stockpile, cardPiles, state.getFoundations());
    }

    @Override
    public String toString() {
        return "PlayStockpileCardToCardPiles{" +
                "index=" + index +
                ", cardPileIndex=" + cardPileIndex +
                '}';
    }
}
