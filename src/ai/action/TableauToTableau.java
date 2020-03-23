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
import java.util.Stack;
import java.util.function.Consumer;

public class TableauToTableau implements Action {

    private int from;
    private int to;
    private Card card;

    public TableauToTableau(int from, int to, Card card){
        this.from = from;
        this.to = to;
        this.card = card;
    }

    @Override
    public Collection<State> getResult(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        final Stack<Card> movedCards = new Stack<>();
        Consumer<Tableau> takeCardsFromTableau = t -> {
            Card removedCard = null;
            while(removedCard != card && !t.getStacks().get(from).isEmpty()){
                removedCard = t.remove(from);
                movedCards.push(removedCard);
            }
        };
        Consumer<Tableau> addCardsToTableau = t -> movedCards.forEach(c -> t.add(c, to));

        tableau = Producer.produceTableau(tableau, takeCardsFromTableau);
        tableau = Producer.produceTableau(tableau, addCardsToTableau);

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
        return "TableauToTableau{" +
                "from=" + from +
                ", to=" + to +
                ", card=" + card +
                '}';
    }
}
