package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import gui.gamescene.aiinterface.IGamePrompter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

public class TableauToFoundation implements Action {

    private Card card;
    private int tableauIndex;

    public TableauToFoundation(Card card, int tableauIndex){
        this.card = card;
        this.tableauIndex = tableauIndex;
    }

    @Override
    public Collection<State> getResults(State state) {
        Collection<State> results = new HashSet<>();

        Stock stock = state.getStock();
        Tableau tableau = state.getTableau();
        Foundation foundation = state.getFoundation();

        Consumer<Tableau> removeCardFromTableau = t -> t.remove(tableauIndex);
        tableau = Producer.produceTableau(tableau, removeCardFromTableau);

        Consumer<Foundation> addCardToFoundation = f -> f.add(card);
        foundation = Producer.produceFoundation(foundation, addCardToFoundation);

        RemainingCards remainingCards = state.getRemainingCards();

        Stack<Card> alteredStack = tableau.getStacks().get(tableauIndex);
        if(alteredStack.isEmpty()){
            results.add(new State(stock, tableau, foundation, remainingCards));
            return results;
        }

        Card check = tableau.getStacks().get(tableauIndex).peek();
        if(check == null){
            for(Card card : remainingCards){
                if(card == null)
                    throw new IllegalStateException();
                Consumer<Tableau> flipCard = t -> {
                    t.remove(tableauIndex);
                    t.add(card, tableauIndex);
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
        prompter.promptTableauToFoundation(tableauIndex, card.getSuit());
    }

    @Override
    public String toString() {
        return "TableauToFoundation{" +
                "tableauIndex=" + tableauIndex +
                ", card=" + card +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableauToFoundation that = (TableauToFoundation) o;
        return tableauIndex == that.tableauIndex &&
                Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, tableauIndex);
    }

    public Card getCard() {
        return card;
    }

    public int getTableauIndex() {
        return tableauIndex;
    }
}
