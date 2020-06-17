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

    private List<Card> stock;
    private List<Card> flipped;
    private List<List<Card>> tableaus;
    private List<List<Card>> foundations;

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
     * objects of {@link Card} objects. The lists may be empty
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
    public GameState(List<Card> stock, List<Card> flipped,
                     List<List<Card>> tableaus, List<List<Card>> foundations) {
        this.stock = stock;
        this.flipped = flipped;
        this.tableaus = tableaus;
        this.foundations = foundations;
    }

    public void addToStock(Card... Card) {
        stock.addAll(Arrays.asList(Card));
    }

    public void addToFlipped(Card Card) {
        flipped.add(Card);
    }

    /**
     * Add a card to a tableau with a given index.
     * Index ranges from 0 (left most) to 6 (right most)
     */
    public void addToTableau(int tableauIndex, Card Card) {
        tableaus.get(tableauIndex).add(Card);
    }

    /**
     * Add a card to a foundation pile with a given index.
     * Index ranges from 0 (left most) to 3 (right most)
     */
    public void addToFoundations(int foundationIndex, Card Card) {
        foundations.get(foundationIndex).add(Card);
    }

    public List<Card> getStock() {
        return stock;
    }

    public List<Card> getFlipped() {
        return flipped;
    }

    public List<List<Card>> getTableaus() {
        return tableaus;
    }

    public List<List<Card>> getFoundations() {
        return foundations;
    }

    public void setStock(List<Card> stock) {
        this.stock = stock;
    }

    public void setFlipped(List<Card> flipped) {
        this.flipped = flipped;
    }

    public void setTableaus(List<Card> tableau, int index) {
        this.tableaus.set(index,tableau);
    }

    public void setFoundations(List<Card> foundation, int index) {
        this.tableaus.set(index, foundation);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("GameState { ");

        // Stock
        if( stock == null || stock.size() == 0){
            str.append("\n\tstock: empty");
        }else{
            str.append("\n\tstock: ");
            for( Card Card : stock ){
                str.append( Card.toStringShort()).append(" ");
            }
        }

        // Flipped cards
        if( flipped == null || flipped.size() == 0){
            str.append("\n\tflipped: empty");
        }else{
            str.append("\n\tflipped: ");
            for( Card Card : flipped ){
                str.append( Card.toStringShort()).append(" ");
            }
        }


        for(int i=0; i<7; i++){
            List<Card> tableau = tableaus.get(i);
            if( tableau == null || tableau.size() == 0){
                str.append("\n\ttableau ").append(i).append(": empty");
            }else{
                str.append("\n\ttableau ").append(i).append(": ");
                for( Card Card : tableau ){
                    str.append( Card.toStringShort()).append(" ");
                }
            }
        }

        for(int i=0; i<4; i++){
            List<Card> foundation = foundations.get(i);
            if( foundation == null || foundation.size() == 0){
                str.append("\n\tfoundation ").append(i).append(": empty");
            }else{
                str.append("\n\tfoundation ").append(i).append(": ");
                for( Card Card : foundation ){
                    str.append( Card.toStringShort()).append(" ");
                }
            }
        }

        str.append("\n}");
        return str.toString();
    }


    /**
     * Checks that this GameState is equal to another GameState by checking
     * that all lists (tableaus, flippped, stock, foundation) contains the
     * same cards (not same objects but card values - see Card.equals()).
     * */
    public boolean equals(GameState gameState){
        if( this == gameState ) return true;

        // Check tableaus are equal
        for( int i=0; i<7; i++){
            List<Card> tableau = tableaus.get(i);
            List<Card> otherTableau = gameState.getTableaus().get(i);
            if( tableau.size() != otherTableau.size())
                return false;
            for( int j=0; j<tableau.size(); j++){
                if( !tableau.get(j).equals(otherTableau.get(j)) )
                    return false;
            }
        }

        // Check Foundations are equal
        for( int i=0; i<4; i++){
            List<Card> foundation = foundations.get(i);
            List<Card> otherFoundation = gameState.getFoundations().get(i);
            if( foundation.size() != otherFoundation.size())
                return false;
            for( int j=0; j<foundation.size(); j++){
                if( !foundation.get(j).equals(otherFoundation.get(j)) )
                    return false;
            }
        }

        // Check flipped are equal
        if( flipped.size() != gameState.flipped.size() )
            return false;
        for(int i=0; i<flipped.size(); i++){
            if( !flipped.get(i).equals(gameState.flipped.get(i)) )
                return false;
        }

        // Check stock
        if( stock.size() != gameState.stock.size() )
            return false;
        for( int i=0; i<stock.size(); i++ ){
            if( !stock.get(i).equals(gameState.stock.get(i)) )
                return false;
        }

        return true;
    }


}
