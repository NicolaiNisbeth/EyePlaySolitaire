package ai;

public class Stockpile {

    private Card[] cards;
    private int head = 2;

    public Stockpile(Card... cards) {
        this.cards = cards;
    }

    public Card takeCard(int index) {
        Card card = cards[index];
        removeCard(index);
        head--;
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

    public int getHead() {
        return Math.abs(head % 3);
    }

    public Card[] getCards() {
        return cards;
    }
}
