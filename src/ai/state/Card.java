package ai.state;

public class Card {

    public static final int CLUB = 0, DIAMOND = 1, HEART = 2, SPADE = 3;

    private int value;
    private int suit;

    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public int getSuit() {
        return suit;
    }

    public Card copy() {
        return new Card(value, suit);
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", suit=" + suit +
                '}';
    }
}
