package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class TableauToFoundation implements Action {

    private Card card;
    private int tableauIndex;

    public TableauToFoundation(Card card, int tableauIndex){
        this.card = card;
        this.tableauIndex = tableauIndex;
    }

    @Override
    public Collection<State> getResult(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        Consumer<Tableau> removeCardFromTableau = t -> t.remove(tableauIndex);
        tableau = Producer.produceTableau(tableau, removeCardFromTableau);

        Consumer<Foundation> addCardToFoundation = f -> f.add(card);
        foundation = Producer.produceFoundation(foundation, addCardToFoundation);

        RemainingCards remainingCards = state.getRemainingCards();
        for(Card card : remainingCards){
            RemainingCards copy = remainingCards.copy();
            copy.removeCard(card);
            results.add(new State(stock, tableau, foundation, copy));
        }

        return results;
    }

    @Override
    public String toString() {
        return "TableauToFoundation{" +
                "tableauIndex=" + tableauIndex +
                ", card=" + card +
                '}';
    }
}
