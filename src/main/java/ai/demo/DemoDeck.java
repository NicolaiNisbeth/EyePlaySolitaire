package ai.demo;

import ai.state.Card;

public class DemoDeck {

    private int head = 0;
    private Card[] cards = new Card[52];

    public DemoDeck() {
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 4; j++) {
                cards[j*13+i-1] = new Card(i, j);
            }
        }
        shuffle();
    }

    private void shuffle() {
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < cards.length; i++) {
                int destination = (int) (Math.random() * cards.length);
                Card temp = cards[destination];
                cards[destination] = cards[i];
                cards[i] = temp;
            }
        }
    }

    public Card draw(){
        return cards[head++];
    }

}
