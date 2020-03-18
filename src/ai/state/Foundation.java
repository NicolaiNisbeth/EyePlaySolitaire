package ai.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Foundation {
    private List<Stack<Card>> stacks = new ArrayList<>();
    private int sum;

    public Foundation(){
        stacks.add(new Stack<>());
        stacks.add(new Stack<>());
        stacks.add(new Stack<>());
        stacks.add(new Stack<>());
    }

    public void add(Card card){
        sum++;
        stacks.get(card.getSuit()).add(card);
    }

    public int getSum() {
        return sum;
    }

    public int getLargestDifference(){
        return -1;
    }
}
