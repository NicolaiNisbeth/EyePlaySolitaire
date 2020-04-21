package ai.state;

import java.util.HashSet;
import java.util.Set;

public class Deck {

    public static final Card[] cards = new Card[52];
    public static final Set<Card> cardSet = new HashSet<>();
    static {
        for (int suit = 0; suit < 4; suit++) {
            for (int value = 1; value <= 13; value++) {
                Card card = new Card(value, suit);
                cards[suit * 13 + value - 1] = card;
                cardSet.add(card);
            }
        }
    }

    public static Card getCard(int value, int suit){
        return cards[suit * 13 + value - 1];
    }
}
