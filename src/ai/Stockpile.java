package ai;

import java.util.Arrays;

public class Stockpile {

    private Card[] cards;
    private int head = 2;

    public Stockpile(Card... cards) {
        this.cards = cards;
    }

    public Card takeCard(int index) {
        Card card = cards[index];
        removeCard(index);
        head = Math.abs(--head % 3);
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

    public void goNext() {
        head = (head + 3) % cards.length;
    }

    public int getHead() {
        return head;
    }

    public Card[] getCards() {
        return cards;
    }

    public Stockpile copy(){
        Card[] cards = Arrays.copyOf(this.cards, this.cards.length);
        return new Stockpile(cards);
    }
}
