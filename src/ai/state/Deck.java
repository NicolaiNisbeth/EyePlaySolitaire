package ai.state;

public class Deck {

    public static final Card[] cards = new Card[52];
    static {
        for (int suit = 0; suit < 4; suit++) {
            for (int value = 0; value < 13; value++) {
                cards[suit * 13 + value] = new Card(value, suit);
            }
        }
    }

    public static Card getCard(int value, int suit){
        return cards[suit * 13 + value];
    }

}
