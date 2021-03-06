package gui.gamescene.gamestate;

import gui.gamescene.gamestate.Card.Suit;
import java.util.ArrayList;
import java.util.Random;


/**
 * >> TEMPORARY CLASS <<
 * Random State generator for testing
 */
public class GameStateGenerator {

    /**
     * Generate game state from random seed
     * The state does not adhere to the game rules
     * (the cards in the different piles are completely random)
     */
    public static GameState generateGameState() {
        return generateGameState(System.currentTimeMillis());
    }

    /**
     * Generate game state from specific seed
     * The state does not adhere to the game rules
     * (the cards in the different piles are completely random)
     */
    public static GameState generateGameState(long seed) {
        Random rand = new Random(seed);

        // Generate Deck
        ArrayList<Card> Cards = new ArrayList<>();
        for( Suit suit : Suit.values() ){
            if( suit == Suit.UNKNOWN ) continue;
            for( int i=1; i<=13; i++ ){
                Cards.add(new Card(suit, i));
            }
        }

        GameState game = new GameState();

        // Add 3 cards to flipped:
        game.addToFlipped(Cards.remove(rand.nextInt(Cards.size())));
        game.addToFlipped(Cards.remove(rand.nextInt(Cards.size())));
        game.addToFlipped(Cards.remove(rand.nextInt(Cards.size())));

        while( Cards.size() > 15 ){
            int i = rand.nextInt(3);

            // Pick and remove random card
            Card Card = Cards.remove(rand.nextInt(Cards.size()));

            // Small chance of the card being replaced by unknown
            if( rand.nextInt(8) == 1 )
                Card = Card.createUnknown();

            // Add to random tableau
            if( i < 2 ) {
                int tableauIndex = rand.nextInt(7);
                if (game.getTableaus().get(tableauIndex).size() < 13) {
                    game.addToTableau(tableauIndex, Card);
                    continue;
                }
            }

            // Add to random foundation
            int foundationIndex = rand.nextInt(4);
            if (game.getFoundations().get(foundationIndex).size() < 13) {
                game.addToFoundations(foundationIndex, Card);
                continue;
            }

            // Couldn't add card (readd it to the stack)
            Cards.add(Card);
        }

        for( Card Card : Cards) game.addToStock(Card);

        return game;
    }
}
