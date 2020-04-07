package gui.gamescene.aiinterface;


/**
 *  Prompts the user to take a particular game action (move cards).
 *
 *  The prompter, does not check any game logic, as this is up to the
 *  caller.
 */
public interface IGamePrompter {

    /**
     *  Prompt the user to traverse the entire stock. */
    void promptTraverseStock();

    /**
     *  Prompts the user to move the card from the top of the source tableau
     *  to target tableau.
     *
     * @param sourceTableauIndex Ranges from 0-6, where 0 is the leftmost tableau.
     * @param targetTableauIndex Ranges from 0-6, where 0 is the leftmost tableau.
     */
    void promptTableauToTableau(int sourceTableauIndex, int targetTableauIndex);

    /**
     * Prompts the user move the a card from the stock to one of the tableaus.
     *
     * @param stockIndex Index of the card within the stock, which is to be moved,
     *                   where 0 is top card at the top of the stack
     * @param tableauIndex Index of the tableau to move the card to. Ranges from 0-6, where 0 is the leftmost tableau.
     */
    void promptStockToTableau(int stockIndex, int tableauIndex);


    /**
     * Prompts the user to move a card from a tableau to one of the foundations.
     *
     * @param tableauIndex Index of the tableau to move the card from. Ranges from 0-6, where 0 is the leftmost tableau.
     * @param foundationIndex Index of the foundation to move the card to. Ranges from 0-3 where 0, is the leftmost foundation.
     */
    void promptTableauToFoundation(int tableauIndex, int foundationIndex);

    /**
     * Prompts the user to move a card from the stock to one of the foundations
     *
     * @param stockIndex Index of the card within the stock, which is to be moved,
     *                   where 0 is top card at the top of the stack
     * @param foundationIndex Index of the foundation to move the card to. Ranges from 0-3 where 0, is the leftmost foundation.
     */
    void promptStockToFoundation(int stockIndex, int foundationIndex);


    /**
     * Tells the prompter, that no move can be made to solve the solitaire.
     */
    void gameLost();
}
