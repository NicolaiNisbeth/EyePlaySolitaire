package gui.gamescene.gamestate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *  Data class to hold the state of a game of Solitaire.
 *  Consists of a different lists of card, the type of
 *  which is up to the user creating the GameState object.
 */
public class GameState {

    private List<UICard> stock;
    private List<UICard> flipped;
    private List<List<UICard>> tableaus;
    private List<List<UICard>> foundations;


    /**
     * Creates a new game state where the List objects
     * are initialized as ArrayLists.
     */
    public GameState() {
        this.stock = new ArrayList<>();
        this.flipped = new ArrayList<>();

        this.tableaus = new ArrayList<>();
        for( int i=0; i<7; i++){
            tableaus.add(new ArrayList<>());
        }

        this.foundations = new ArrayList<>();
        for( int i=0; i<4; i++){
            foundations.add(new ArrayList<>());
        }
    }


    /**
     * Creates a new game state from a series of arbitrary List
     * objects of {@link UICard} objects. The lists may be empty
     * and cards may be added later.
     *
     * No game logic is tested when creating the game state - this
     * is up to the user.
     *
     * @param stock     The stack of cards you may 'flip'
     * @param flipped   The currently "flipped" cards from the stack
     * @param tableaus  The (usually 7) stacks of cards you may move cards between
     * @param foundations The (usually 4) stacks you aim to get all cards into
     */
    public GameState(List<UICard> stock, List<UICard> flipped,
                     List<List<UICard>> tableaus, List<List<UICard>> foundations) {
        this.stock = stock;
        this.flipped = flipped;
        this.tableaus = tableaus;
        this.foundations = foundations;
    }

    public void addToStock(UICard... UICard) {
        stock.addAll(Arrays.asList(UICard));
    }

    public void addToFlipped(UICard UICard) {
        flipped.add(UICard);
    }

    /**
     * Add a card to a tableau with a given index.
     * Index ranges from 0 (left most) to 6 (right most)
     */
    public void addToTableau(int tableauIndex, UICard UICard) {
        tableaus.get(tableauIndex).add(UICard);
    }

    /**
     * Add a card to a foundation pile with a given index.
     * Index ranges from 0 (left most) to 3 (right most)
     */
    public void addToFoundations(int foundationIndex, UICard UICard) {
        foundations.get(foundationIndex).add(UICard);
    }

    public List<UICard> getStock() {
        return stock;
    }

    public List<UICard> getFlipped() {
        return flipped;
    }

    public List<List<UICard>> getTableaus() {
        return tableaus;
    }

    public List<List<UICard>> getFoundations() {
        return foundations;
    }




    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("GameState { ");

        // Stock
        if( stock == null || stock.size() == 0){
            str.append("\nstock: empty");
        }else{
            str.append("\nstock: ");
            for( UICard UICard : stock ){
                str.append( UICard.toStringShort()).append(" ");
            }
        }

        // Flipped cards
        if( flipped == null || flipped.size() == 0){
            str.append("\nflipped: empty");
        }else{
            str.append("\nflipped: ");
            for( UICard UICard : flipped ){
                str.append( UICard.toStringShort()).append(" ");
            }
        }


        for(int i=0; i<7; i++){
            List<UICard> tableau = tableaus.get(i);
            if( tableau == null || tableau.size() == 0){
                str.append("\n\ttableau ").append(i).append(": empty");
            }else{
                str.append("\n\ttableau ").append(i).append(": ");
                for( UICard UICard : tableau ){
                    str.append( UICard.toStringShort()).append(" ");
                }
            }
        }

        for(int i=0; i<4; i++){
            List<UICard> foundation = foundations.get(i);
            if( foundation == null || foundation.size() == 0){
                str.append("\n\tfoundation ").append(i).append(": empty");
            }else{
                str.append("\n\tfoundation ").append(i).append(": ");
                for( UICard UICard : foundation ){
                    str.append( UICard.toStringShort()).append(" ");
                }
            }
        }

        str.append("\n}");
        return str.toString();
    }
}
