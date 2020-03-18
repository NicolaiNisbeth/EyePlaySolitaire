package ai.state;

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
}
