package gui.gamescene;

import java.util.Collection;


/**
 *  Data class to hold the state of a game of Solitaire.
 *  Consists of a different lists of card, the type of
 *  which is up to the user creating the GameState object.
 */
public class GameState {

    private Collection stock;
    private Collection flipped;
    private Collection<Collection<Card>> tableaus;
    private Collection<Collection<Card>> foundations;


    /**
     * Creates a new game state from a series of arbitrary Collection
     * objects of {@link Card} objects.
     * No game logic is tested when creating the game state - this
     * is up to the user.
     *
     * @param stock     The stack of cards you may 'flip'
     * @param flipped   The currently "flipped" cards from the stack
     * @param tableaus  The (usually 7) stacks of cards you may move cards between
     * @param foundations The (usually 4) stacks you aim to get all cards into
     */
    public GameState(Collection stock, Collection flipped,
                     Collection<Collection<Card>> tableaus, Collection<Collection<Card>> foundations) {
        this.stock = stock;
        this.flipped = flipped;
        this.tableaus = tableaus;
        this.foundations = foundations;
    }

    public Collection getStock() {
        return stock;
    }

    public Collection getFlipped() {
        return flipped;
    }

    public Collection<Collection<Card>> getTableaus() {
        return tableaus;
    }

    public Collection<Collection<Card>> getFoundations() {
        return foundations;
    }
}
