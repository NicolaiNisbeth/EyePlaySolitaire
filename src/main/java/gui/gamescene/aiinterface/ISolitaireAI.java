package gui.gamescene.aiinterface;


import gui.gamescene.gamestate.GameState;

public interface ISolitaireAI {


    /**
     * Compute the best action based on the given GameState, and
     * prompt the action using the given prompter.
     *
     * @param gameState State of the current game. May containt unknown cards.
     * @param prompter Prompter which implements the prompting of a given action.
     *                 Called by the AI, once the best action has been computed.
     *
     * @throws IllegalGameStateException If the given GameState does not conform to the Game Rules.
     */
    @Deprecated
    void computeAction(GameState gameState, IActionPrompter prompter) throws IllegalGameStateException;



    /**
     * Start computation of the the best action based on the given GameState
     *
     * @param gameState State of the current game. May containt unknown cards.
     *
     * @throws IllegalGameStateException If the given GameState does not conform to the Game Rules.
     */
    void startActionComputation(GameState gameState) throws IllegalGameStateException;


    /**
     * Terminates the started computation, causing the best action to be prompted
     * via the given IActionPrompter
     *
     * @param prompter Prompter which implements the prompting of a given action.
     *
     * @throws IllegalStateException    Thrown if computation has not been started yet using the startActionComputation
     */
    void endActionComputation(IActionPrompter prompter);


    class IllegalGameStateException extends Exception { }
}
