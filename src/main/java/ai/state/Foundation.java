package ai.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Foundation {
    private List<Stack<Card>> stacks = new ArrayList<>();
    private int[] sizes = new int[4];
    private int sum;

    public Foundation(){
        int suits = 4;

        for (int i = 0; i < suits; i++)
            stacks.add(new Stack<>());

    }

    public Foundation(List<Stack<Card>> stacks){
        this.stacks = stacks;
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

    public int getCount() {
        return sum;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foundation that = (Foundation) o;
        return sum == that.sum &&
                Objects.equals(stacks, that.stacks) &&
                Arrays.equals(sizes, that.sizes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(stacks, sum);
        result = 31 * result + Arrays.hashCode(sizes);
        return result;
    }
}
