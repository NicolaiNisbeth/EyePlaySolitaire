package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.function.Consumer;

public class TableauToFoundation implements Action {

    private int tableauIndex;
    private Card card;

    public TableauToFoundation(int tableauIndex, Card card){
        this.tableauIndex = tableauIndex;
        this.card = card;
    }

    @Override
    public State getResult(State state) {
        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        Consumer<Tableau> removeCardFromTableau = t -> t.remove(tableauIndex);
        tableau = Producer.produceTableau(tableau, removeCardFromTableau);

        Consumer<Foundation> addCardToFoundation = f -> f.add(card);
        foundation = Producer.produceFoundation(foundation, addCardToFoundation);

        return new State(stock, tableau, foundation);
    }

    @Override
    public String toString() {
        return "TableauToFoundation{" +
                "tableauIndex=" + tableauIndex +
                ", card=" + card +
                '}';
    }
}
