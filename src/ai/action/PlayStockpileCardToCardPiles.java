package ai.action;

import ai.Card;
import ai.CardPile;
import ai.State;
import ai.Stockpile;

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
