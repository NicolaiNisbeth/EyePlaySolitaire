package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import gui.gamescene.aiinterface.IGamePrompter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

        final List<Card> movedCards = new ArrayList<>();
        Consumer<Tableau> takeCardsFromTableau = t -> {
            Card removedCard = null;
            while(!card.equals(removedCard) && !t.getStacks().get(from).isEmpty()){
                removedCard = t.remove(from);
                movedCards.add(removedCard);
            }
        };
        Consumer<Tableau> addCardsToTableau = t -> {
            for (int i = movedCards.size()-1; i >= 0; i--){
                t.add(movedCards.get(i), to);
            }
        };

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
    public void prompt(IGamePrompter prompter, State state) {
        prompter.promptTableauToTableau(from, to);
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
