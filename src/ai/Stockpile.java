package ai;

public class Stockpile {

    private Card[] cards;
    private int head = 2;

    private Stockpile(int head, Card... cards) {
        this.cards = cards;
        this.head = head;
    }

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

    public Stockpile deepCopy(){
        Card[] cards = new Card[this.cards.length];
        for (int i = 0; i < this.cards.length; i++) {
            Card oldCard = this.cards[i];
            cards[i] = new Card(oldCard.getValue(), oldCard.getSuit());
        }
        return new Stockpile(head, cards);
    }

    public Stockpile shallowCopy() {
        return new Stockpile(cards);
    }


}
