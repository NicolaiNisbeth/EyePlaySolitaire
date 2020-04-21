package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import gui.gamescene.aiinterface.IGamePrompter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

public class StockToFoundation implements Action {

    private Card card;

    public StockToFoundation(Card card) {
        this.card = card;
    }

    @Override
    public Collection<State> getResults(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Consumer<Stock> removeCardFromStock = s -> s.removeCard(card);
        stock = Producer.produceStock(stock, removeCardFromStock);

        Foundation foundation = state.getFoundation();
        Consumer<Foundation> addCardToFoundation = f -> f.add(card);
        foundation = Producer.produceFoundation(foundation, addCardToFoundation);

        results.add(new State(stock, state.getTableau(), foundation, state.getRemainingCards()));
        return results;
    }

    @Override
    public void prompt(IGamePrompter prompter, State state) {
        Stock stock = state.getStock();
        int index = stock.getCardIndex(card);
        prompter.promptStockToFoundation(index, card.getSuit());
    }

    @Override
    public String toString() {
        return "StockToFoundation{" +
                "card=" + card +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockToFoundation that = (StockToFoundation) o;
        return Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card);
    }
}
