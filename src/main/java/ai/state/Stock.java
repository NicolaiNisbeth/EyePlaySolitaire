package ai.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Stock {

    private Card[] cards;
    private int head = 2;

    public Stock(Card... cards) {
        this.cards = cards;
    }

    public void removeCard(Card toRemove){
        Card[] newCards = new Card[cards.length-1];
        int i = 0;

        for(Card card : cards){
            if(card.equals(toRemove))
                head = i-1;
            else
                newCards[i++] = card;
        }

        if(head == -1)
            head = 0;

        cards = newCards;
    }

    public List<Card> showOptions() {
        List<Card> availableCards = new ArrayList<>();
        if(cards.length == 0)
            return availableCards;
        int index = head;

        do {
            availableCards.add(cards[index]);
            index = (index + 3) % cards.length;
        } while (index != head);

        return availableCards;
    }

    public Card[] getCards() {
        return cards;
    }


    public int getCardIndex(Card card){
        for (int i = 0; i < cards.length; i++) if (cards[i].equals(card))
            return i;

        return -1;
    }

    @Override
    public String toString() {
        return "Stockpile{" +
                "cards=" + Arrays.toString(cards) +
                ", head=" + head +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        boolean same = true;
        for (int i = 0; i < cards.length; i++) {
            if (!cards[i].equals(stock.cards[i])) {
                same = false;
                break;
            }
        }
        return head == stock.head && same;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(head);
        result = 31 * result + Arrays.hashCode(cards);
        return result;
    }
}
