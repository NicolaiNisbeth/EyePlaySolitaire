package gui.gamescene;


/** Class to represent a playing card with a suit and a value */
public class Card {

    private Suit suit;
    private int value; // Ranges from 1 (ace) to 13 (king)
    private boolean hidden = false;

    /**
     * Create a new card from a suit (heart, diamond, spades or clubs), and
     * a value between 1 (ace) - 13 (king).
     * The card defaults to not hidden.
     *
     * @throws IllegalArgumentException If the value is not within the correct interval
     * */
    public Card(Suit suit, int value){
        this.suit = suit;
        setValue(value);
    }

    /**
     * @return The card's suit as a {@link Card.Suit} enum
     * */
    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    /**
     * @return The value of the card, which is between 1 (ace) and 13 (king).
     *
     * */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the card. The value must be between 1 (ace) and 13 (king).
     *
     * @throws IllegalArgumentException If the value is not within the correct interva
     * */
    public void setValue(int value) {
        if( value < 1  || value > 13 ){
            throw new IllegalArgumentException("Card value must be between 1 and 13");
        }
        this.value = value;
    }

    /**
     * Checks whether or not the card is hidden.
     *
     * Any logic that may be affected whether or not the card is hidden,
     * is defined externally (not within this class).
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets whether or not the card should be hidden.
     *
     * Any logic that may be affected whether or not the card is hidden,
     * is defined externally (not within this class).
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean equals(Card other) {
        if( other == null )           return false;
        if( this == other )           return true;
        return this.suit == other.suit && this.value == other.value;
    }

    @Override
    public String toString() {
        return "Card{" +
                "suit=" + suit +
                ", value=" + value +
                ", hidden=" + hidden +
                '}';
    }

    public enum Suit{
        HEART,
        DIAMOND,
        SPADE,
        CLUBS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
