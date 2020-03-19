package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.function.Consumer;

public class StockToTableau implements Action {

    private Card card;
    private int tableauIndex;

    public StockToTableau(Card card, int tableauIndex) {
        this.card = card;
        this.tableauIndex = tableauIndex;
    }

    @Override
    public State getResult(State state) {
        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        Consumer<Stock> removeCardFromStock = s -> s.removeCard(card);
        stock = Producer.produceStock(stock, removeCardFromStock);

        Consumer<Tableau> addCardToTableau = t -> t.add(card, tableauIndex);
        tableau = Producer.produceTableau(tableau, addCardToTableau);

        return new State(stock, tableau, foundation);
    }

    @Override
    public String toString() {
        return "StockToTableau{" +
                "card=" + card +
                ", tableauIndex=" + tableauIndex +
                '}';
    }
}
