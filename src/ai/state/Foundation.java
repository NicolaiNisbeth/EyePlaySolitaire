package ai.state;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Card peek(int index){
        return stacks.get(index).peek();
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

    public List<Stack<Card>> getStacks() {
        return stacks;
    }

    public void setStacks(List<Stack<Card>> stacks) {
        this.stacks = stacks;
    }

    public void setSizes(int[] sizes) {
        this.sizes = sizes;
    }

    public int[] getSizes() {
        return sizes;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Foundation{" +
                "stacks=" + stacks +
                ", sizes=" + Arrays.toString(sizes) +
                ", sum=" + sum +
                '}';
    }
}
