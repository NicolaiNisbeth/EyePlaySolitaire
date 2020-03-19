package ai.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            if(card == toRemove)
                head = i-1;
            else
                newCards[i++] = card;
        }

        cards = newCards;
    }

    private List<Card> showOptions() {
        List<Card> availableCards = new ArrayList<>();
        int index = head;

        do {
            availableCards.add(cards[index]);
            index = (index + 3) % cards.length;
        } while (index != head);

        return availableCards;
    }

    @Override
    public String toString() {
        return "Stockpile{" +
                "cards=" + Arrays.toString(cards) +
                ", head=" + head +
                '}';
    }
}
