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

    public void removeCard(Card card){
        int index = binarySearch(0, remainingCards.length-1, card);
        Card[] updatedCards = new Card[remainingCards.length - 1];
        for (int i = 0, j = 0; i < remainingCards.length; i++, j++) {
            if(i == index)
                j--;
            else
                updatedCards[j] = remainingCards[i];
        }
        remainingCards = updatedCards;
    }

    private int binarySearch(int left, int right, Card target) {
        int middle = left + (right - 1) / 2;
        if(remainingCards[middle] == target)
            return middle;
        if(remainingCards[middle].compareTo(target) > 0)
            return binarySearch(left, middle - 1, target);
        return binarySearch(middle + 1, right, target);
    }
}
