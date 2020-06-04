package ai.state;

public class Card implements Comparable<Card>{

    public static final int CLUB = 0, DIAMOND = 1, HEART = 2, SPADE = 3;
    public static final int BLACK = 0, RED = 1;

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

    public int getColour(){
        return suit == HEART || suit == DIAMOND ? RED : BLACK;
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", suit=" + suit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value &&
                suit == card.suit;
    }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(
                suit * 13 + value,
                other.suit * 13 + other.value
        );
    }
}
