package ai;

public class Deck {

    private int head = 0;
    private Card[] cards = new Card[52];

    public Deck() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                cards[j*13+i] = new Card(i, j);
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
