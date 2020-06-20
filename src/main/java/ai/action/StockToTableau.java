package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import gui.gamescene.aiinterface.IGamePrompter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

public class StockToTableau implements Action {

    private final Card card;
    private final int tableauIndex;

    public StockToTableau(Card card, int tableauIndex) {
        this.card = card;
        this.tableauIndex = tableauIndex;
    }

    @Override
    public Collection<State> getResults(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Consumer<Stock> removeCardFromStock = s -> s.removeCard(card);
        stock = Producer.produceStock(stock, removeCardFromStock);

        Tableau tableau = state.getTableau();
        Consumer<Tableau> addCardToTableau = t -> t.add(card, tableauIndex);
        tableau = Producer.produceTableau(tableau, addCardToTableau);

        results.add(new State(stock, tableau, state.getFoundation(), state.getRemainingCards()));
        return results;
    }

    public Card getCard(){
        return card;
    }

    @Override
    public void prompt(IGamePrompter prompter, State state) {
        Stock stock = state.getStock();
        int index = stock.getCardIndex(card);
        //prompter.promptStockToTableau(index, tableauIndex);
        prompter.promptStockToTableau(card, tableauIndex);
    }

    @Override
    public String toString() {
        return "StockToTableau{" +
                "card=" + card +
                ", tableauIndex=" + tableauIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockToTableau that = (StockToTableau) o;
        return tableauIndex == that.tableauIndex &&
                Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, tableauIndex);
    }
}
