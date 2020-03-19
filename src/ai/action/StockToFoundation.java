package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.function.Consumer;

public class StockToFoundation implements Action {

    private Card card;

    public StockToFoundation(Card card) {
        this.card = card;
    }

    @Override
    public State getResult(State state) {
        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        Consumer<Stock> removeCardFromStock = s -> s.removeCard(card);
        stock = Producer.produceStock(stock, removeCardFromStock);

        Consumer<Foundation> addCardToFoundation = f -> f.add(card);
        foundation = Producer.produceFoundation(foundation, addCardToFoundation);

        return new State(stock, tableau, foundation);
    }

    @Override
    public String toString() {
        return "StockToFoundation{" +
                "card=" + card +
                '}';
    }
}
