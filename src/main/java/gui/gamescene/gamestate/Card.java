package gui.gamescene.gamestate;


import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.text.NumberFormat;
import java.util.*;

import javafx.scene.effect.DropShadow;

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


    public Card copy(){
        return new Card(suit, value);
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


    @Override
    public boolean equals(Object other) {
        if( other == null )           return false;
        if( this == other )           return true;
        if( !(other instanceof Card) ) return false;
        Card otherCard = (Card) other;
        return this.suit == otherCard.suit && this.value == otherCard.value;
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


    /**
     * Generates a string representation of the card in a user friendly format.
     * Example H1 becomes 'Ace of Hearts' and C12 becomes Queen of Clubs
     */
    public String toStringPretty(){
        return valueAsName(value) + " of " + suit.toString();
    }


    public static String valueAsName(int value){
        if(value == 1) return "Ace";
        if(value == 2) return "Two";
        if(value == 3) return "Three";
        if(value == 4) return "Four ";
        if(value == 5) return "Five";
        if(value == 6) return "Six";
        if(value == 7) return "Seven";
        if(value == 8) return "Eight";
        if(value == 9) return "Nine";
        if(value == 10) return "Ten";
        if(value == 11) return "Jack";
        if(value == 12) return "Queen";
        if(value == 13) return "King";

        return "Unknown";
    }


    public enum Suit{
        UNKNOWN,
        DIAMONDS,
        CLUBS,
        HEARTS,
        SPADES;
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }


    /**
     * Creates a list of all Cards (meaning a full deck).
     * @param shuffle Whether or not to shuffle the deck
     */
    public static List<Card> createDeck(boolean shuffle){
        List<Card> deck = new LinkedList<>();
        for( Suit suit : Suit.values() ){
            if( suit == Suit.UNKNOWN) continue;
            for( int i=1; i<=13; i++){
                deck.add(new Card(suit, i));
            }
        }

        if( shuffle )
            Collections.shuffle(deck);

        return deck;
    }
}
