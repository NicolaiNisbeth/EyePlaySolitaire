package ai.heuristic;

import ai.state.*;

public class Cocktail implements Heuristic {

    private IActionFinder actionFinder = new ActionFinder52();

    private final double w1;
    private final double w2;
    private final double w3;
    private final double w4;
    private final double w5;
    private final double w6;
    private final double w7;
    private final double w8;
    private final double w9;
    private TableauDelta h1 = new TableauDelta();
    private MoreOptions h2 = new MoreOptions();
    private MaxCardGroupSize h3 = new MaxCardGroupSize();
    private LessUnknowns h4 = new LessUnknowns();
    private HasNextInChain h5 = new HasNextInChain();
    private FoundationSize h6 = new FoundationSize();
    private CardGroupsShouldBeUneven h7 = new CardGroupsShouldBeUneven();
    private BinaryKingAvailableForEmptySpace h8 = new BinaryKingAvailableForEmptySpace();
    private BinaryFoundationDelta h9 = new BinaryFoundationDelta();

    public Cocktail(double w1, double w2, double w3, double w4, double w5, double w6, double w7, double w8, double w9){
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;
        this.w5 = w5;
        this.w6 = w6;
        this.w7 = w7;
        this.w8 = w8;
        this.w9 = w9;
    }

    @Override
    public double evaluate(State state) {
        double e1=0, e2=0, e3=0, e4=0, e5=0, e6=0, e7=0, e8=0, e9=0, e10=0;
//e1 = w1 * h1.evaluate(state);
        e2 = w2 * h2.evaluate(state);
//e3 = w3 * h3.evaluate(state);
        e4 = w4 * h4.evaluate(state) * 8;
        e5 = w5 * h5.evaluate(state)/2;
        e6 = w6 * h6.evaluate(state);
//e7 = w7 * h7.evaluate(state)/10;
//e8 = w8 * h8.evaluate(state);
        e9 = w9 * h9.evaluate(state);
//e10 = state.isGoal() ? 0 : -500_000_000;
        return e1 + e2 + e3 + e4 + e5 + e6 + e7 + e8 + e9 + e10;


    }
}
