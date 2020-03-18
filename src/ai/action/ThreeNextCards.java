package ai.action;

import ai.State;
import ai.Stockpile;

public class ThreeNextCards implements Action {
    @Override
    public State getResult(State state) {
        Stockpile stockpile = state.getStockpile().shallowCopy();
        stockpile.goNext();
        return new State(stockpile, state.getCardPiles(), state.getFoundations());
    }
}
