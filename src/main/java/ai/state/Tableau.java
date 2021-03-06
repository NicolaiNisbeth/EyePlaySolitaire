package ai.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
            sum -= card.getValue();
        return card;
    }

    @Override
    public String toString() {
        return "Tableau{" +
                "stacks=" + stacks +
                ", sum=" + sum +
                '}';
    }

    public int getSum() {
        return sum;
    }

    public List<Stack<Card>> getStacks(){
        return stacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tableau tableau = (Tableau) o;
        boolean flag = true;
        for (int i = 0; i < stacks.size(); i++) {
            Stack<Card> me = stacks.get(i);
            Stack<Card> you = tableau.stacks.get(i);
            for(Card card : me){
                if(!you.contains(card)){
                    flag = false;
                    break;
                }
            }
        }
        return flag && sum == tableau.sum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stacks, sum);
    }
}
