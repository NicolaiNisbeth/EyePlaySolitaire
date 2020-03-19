package ai.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tableau {
    private List<Stack<Card>> stacks = new ArrayList<>();
    private int sum = 0;

    public Tableau(Card[][] cards) {
        for (Card[] cardArray : cards) {
            Stack<Card> stack = new Stack<>();
            for (Card card : cardArray) {
                if (card != null)
                    sum += card.getValue();
                stack.push(card);
            }
            stacks.add(stack);
        }
    }

    public void add(Card card, int index) {
        if (card != null)
            sum += card.getValue();
        stacks.get(index).push(card);
    }

    public Card remove(int index) {
        Card card = stacks.get(index).pop();
        if(card != null)
            sum += card.getValue();
        return card;
    }

    public int getSum() {
        return sum;
    }

    public List<Stack<Card>> getStacks(){
        return stacks;
    }

}
