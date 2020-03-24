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
import java.util.Objects;
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
    public Collection<State> getResults(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        /*
        Consumer<Tableau> moveCards = t -> {
            Stack<Card> movedCards = new Stack<>();
            Stack<Card> source = t.getStacks().get(from);
            Stack<Card> target = t.getStacks().get(to);
            do {
                Card removedCard = source.pop();
                target.push(removedCard);
            } while(removedCard != card)
        }
         */

        final Stack<Card> movedCards = new Stack<>();
        Consumer<Tableau> takeCardsFromTableau = t -> {
            Card removedCard = null;
            while(!card.equals(removedCard) && !t.getStacks().get(from).isEmpty()){
                removedCard = t.remove(from);
                movedCards.push(removedCard);
            }
        };
        Consumer<Tableau> addCardsToTableau = t -> movedCards.forEach(c -> t.add(c, to));

        tableau = Producer.produceTableau(tableau, takeCardsFromTableau);
        tableau = Producer.produceTableau(tableau, addCardsToTableau);

        RemainingCards remainingCards = state.getRemainingCards();

        Stack<Card> alteredStack = tableau.getStacks().get(from);
        if(alteredStack.isEmpty()){
            results.add(new State(stock, tableau, foundation, remainingCards));
            return results;
        }

        Card check = tableau.getStacks().get(from).peek();
        if(check == null){
            for(Card card : remainingCards){
                if(card == null)
                    throw new IllegalStateException();
                Consumer<Tableau> flipCard = t -> {
                    t.remove(from);
                    t.add(card, from);
                };
                Tableau randomTableau = Producer.produceTableau(tableau, flipCard);
                RemainingCards copy = remainingCards.copy();
                copy.removeCard(card);
                results.add(new State(stock, randomTableau, foundation, copy));
            }
            return results;
        }

        results.add(new State(stock, tableau, foundation, remainingCards));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableauToTableau that = (TableauToTableau) o;
        return from == that.from &&
                to == that.to &&
                Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, card);
    }
}
