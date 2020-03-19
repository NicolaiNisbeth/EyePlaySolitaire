package ai.state;

import java.util.Arrays;

public class Deck {

    private Card[] remainingCards;

    public Deck(){
        remainingCards = new Card[52];
        for (int i = 0; i < 13; i++){
            for (int j = 0; j < 4; j++){
                remainingCards[j*13+i] = new Card(i, j);
            }
        }
        Arrays.sort(remainingCards);
    }

    public Card[] getRemainingCards(){
        return this.remainingCards;
    }

    public void removeCard(Card toRemove){
        Card[] newCards = new Card[remainingCards.length-1];
        int i = 0;

        for(Card card : remainingCards)
            if(card != toRemove)
                newCards[i++] = card;

        remainingCards = newCards;
    }

    private int binarySearch(int left, int right, Card target) {
        if(right == 0) return -1;

        int middle = left + (right - 1) / 2;
        if(remainingCards[middle] == target)
            return middle;
        if(remainingCards[middle].compareTo(target) > 0)
            return binarySearch(left, middle - 1, target);
        return binarySearch(middle + 1, right, target);
    }
}
