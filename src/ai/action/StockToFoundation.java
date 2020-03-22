package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class StockToFoundation implements Action {

    private Card card;

    public StockToFoundation(Card card) {
        this.card = card;
    }

    @Override
    public Collection<State> getResult(State state) {
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
    public String toString() {
        return "StockToFoundation{" +
                "card=" + card +
                '}';
    }
}
