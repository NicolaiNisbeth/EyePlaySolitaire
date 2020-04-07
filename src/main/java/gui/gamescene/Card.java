package gui.gamescene;


import java.text.NumberFormat;

/** Class to represent a playing card with a suit and a value */
public class Card {

    private Suit suit;
    private int value; // Ranges from 1 (ace) to 13 (king)

    /**
     * Create a new card from a suit (heart, diamond, spades or clubs), and
     * a value between 1 (ace) - 13 (king). You may also give the value 0
     * the card is unknown.
     * The card defaults to not hidden.
     *
     * @throws IllegalArgumentException If the value is not within the correct interval
     * */
    public Card(Suit suit, int value){
        this.suit = suit;
        setValue(value);

        // Ensure that its' correctly set as unknown card
        if( this.suit == Suit.UNKNOWN || this.value == 0 ){
            this.suit = Suit.UNKNOWN;
            this.value = 0;
        }
    }

    /**
     * Create a card which is unknown (suit == UNKNOWN and
     * value == 0).
     */
    public static Card createUnknown(){
        return new Card(Suit.UNKNOWN, 0);
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
        if( value < 0  || value > 13 ){
            throw new IllegalArgumentException("Card value must be between 0 and 13");
        }
        this.value = value;
    }


    /**
     * Tests whether the card is unknown (suit == UNKNOWN and value == 0)
     */
    public boolean isUnknown() {
        return suit == Suit.UNKNOWN && value == 0;
    }


    public boolean equals(Card other) {
        if( other == null )           return false;
        if( this == other )           return true;
        return this.suit == other.suit && this.value == other.value;
    }

    @Override
    public String toString() {
        return "Card{ " +
                "suit=" + suit +
                ", value=" + value +
                " }";
    }

    /**
     * Generate String representation of card in the format
     * [SUITLETTER][VALUE]. Queen of Hearts would be H12,
     * and Ace of Spades S01.
     * Unknown card is represented as '???'
     */
    public String toStringShort(){
        if( isUnknown() ) return "???";
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(2);
        return suit.toString().substring(0, 1).toUpperCase() + format.format(value);
    }

    public enum Suit{
        UNKNOWN,
        HEARTS,
        DIAMONDS,
        SPADES,
        CLUBS;


        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
