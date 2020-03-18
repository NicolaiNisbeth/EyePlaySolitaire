package ai.action;

import ai.state.Card;
import ai.state.CardPile;
import ai.state.State;
import ai.state.Stock;

public class StockToTableau implements Action {

    private int index;
    private int cardPileIndex;

    public StockToTableau(int stockpileIndex, int cardPileIndex) {
        this.index = stockpileIndex;
        this.cardPileIndex = cardPileIndex;
    }

    @Override
    public State getResult(State state) {
        Stock stock = state.getStock().deepCopy();
        Card card = stock.takeCard(index);
        CardPile[] cardPiles = state.shallowCopyCardPiles();
        CardPile newCardPile = cardPiles[cardPileIndex].deepCopy();
        newCardPile.addLast(card);
        cardPiles[cardPileIndex] = newCardPile;
        return new State(stock, cardPiles, state.getFoundations());
    }

    @Override
    public String toString() {
        return "PlayStockpileCardToCardPiles{" +
                "index=" + index +
                ", cardPileIndex=" + cardPileIndex +
                '}';
    }
}
