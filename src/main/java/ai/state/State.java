package ai.state;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class State {

    private Stock stock;
    private Tableau tableau;
    private Foundation foundation;
    private RemainingCards remainingCards;

    public State(Stock stock, Tableau tableau, Foundation foundation, RemainingCards remainingCards){
        this.stock = stock;
        this.tableau = tableau;
        this.foundation = foundation;
        this.remainingCards = remainingCards;
    }

    public Stock getStock() {
        return stock;
    }

    public Tableau getTableau() {
        return tableau;
    }

    public Foundation getFoundation() {
        return foundation;
    }

    public RemainingCards getRemainingCards() {
        return remainingCards;
    }

    @Override
    public String toString() {
        return "State{" +
                "stock=" + stock +
                ", tableau=" + tableau +
                ", foundation=" + foundation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return stock.equals(state.stock) &&
                tableau.equals(state.tableau) &&
                foundation.equals(state.foundation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, tableau, foundation);
    }

    public boolean isGoal() {
        return foundation.getCount() == 52;
    }
}
