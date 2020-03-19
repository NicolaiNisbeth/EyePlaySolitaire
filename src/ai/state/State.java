package ai.state;

import java.util.Objects;

public class State {

    private Stock stock;
    private Tableau tableau;
    private Foundation foundation;

    public State(Stock stock, Tableau tableau, Foundation foundation){
        this.stock = stock;
        this.tableau = tableau;
        this.foundation = foundation;
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
        return Objects.equals(stock, state.stock) &&
                Objects.equals(tableau, state.tableau) &&
                Objects.equals(foundation, state.foundation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, tableau, foundation);
    }
}
