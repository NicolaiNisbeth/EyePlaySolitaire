package ai.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Foundation {
    private List<Stack<Card>> stacks = new ArrayList<>();
    private int[] sizes;
    private int sum;

    public Foundation(){
        int suits = 4;
        sizes = new int[suits];

        for (int i = 0; i < suits; i++)
            stacks.add(new Stack<>());

    }

    public void add(Card card){
        int index = card.getSuit();
        stacks.get(index).add(card);
        sizes[index]++;
        sum++;
    }

    public int getSum() {
        return sum;
    }

    public int getLargestDifference(){
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for(int size : sizes){
            if(size < min)
                min = size;
            if(size > max)
                max = size;
        }
        return max - min;
    }
}
