package ai.state;

import java.util.List;
import java.util.Stack;

public class CopyState {
    public static State copyValues(State state){
        Card[] stockCards = state.getStock().getCards();
        Card[] newStockCards = new Card[stockCards.length];
        for (int i = 0; i < stockCards.length; i++) {
            newStockCards[i] = stockCards[i];
        }

        Stock newStock = new Stock(newStockCards);

        List<Stack<Card>> fStacks = state.getFoundation().getStacks();
        Foundation newFoundation = new Foundation();
        for(Stack<Card> stack : fStacks) {
            for (int i = 0; i < stack.size(); i++) {
                Card card = stack.get(i);
                newFoundation.add(card);
            }
        }

        List<Stack<Card>> tableauCards = state.getTableau().getStacks();
        Card[][] newBoard = new Card[tableauCards.size()][];
        for (int i = 0; i < tableauCards.size(); i++) {
            Stack<Card> stack = tableauCards.get(i);
            newBoard[i] = new Card[stack.size()];
            for (int j = 0; j < stack.size(); j++) {
                newBoard[i][j] = stack.get(j);
            }
        }

        Tableau newTableau = new Tableau(newBoard);
        return new State(newStock, newTableau, newFoundation, state.getRemainingCards());
    }
}
