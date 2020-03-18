package ai.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stock {

    private Card[] cards;
    private int head = 2;

    private Stock(int head, Card... cards) {
        this.cards = cards;
        this.head = head;
    }

    public Stock(Card... cards) {
        this.cards = cards;
    }

    public Card takeCard(int index) {
        Card card = cards[index];
        removeCard(index);
        head = index - 1;
        if(head == -1)
            head = 2;
        return card;
    }

    private void removeCard(int toRemove) {
        if(toRemove < 0)
            throw new IllegalArgumentException("Index may not be negative");

        Card[] newCards = new Card[cards.length-1];
        for (int i = 0; i < cards.length; i++) {
            if(i > toRemove){
                newCards[i-1] = cards[i];
            } else {
                newCards[i] = cards[i];
            }
        }
        cards = newCards;
    }

    private List<Integer> findStockpilePlayableIndexes() {
        List<Integer> indices = new ArrayList<>();
        int index = getHead();
        int size = getCards().length;
        while(!indices.contains(index)){
            indices.add(index);
            index = (index + 3) % size;
        }
        return indices;
    }

    public void goNext() {
        head = (head + 3) % cards.length;
    }

    public int getHead() {
        return head;
    }

    public Card[] getCards() {
        return cards;
    }

    public Stock deepCopy(){
        Card[] cards = new Card[this.cards.length];
        for (int i = 0; i < this.cards.length; i++) {
            Card oldCard = this.cards[i];
            cards[i] = new Card(oldCard.getValue(), oldCard.getSuit());
        }
        return new Stock(head, cards);
    }

    public Stock shallowCopy() {
        return new Stock(cards);
    }

    @Override
    public String toString() {
        return "Stockpile{" +
                "cards=" + Arrays.toString(cards) +
                ", head=" + head +
                '}';
    }
}
