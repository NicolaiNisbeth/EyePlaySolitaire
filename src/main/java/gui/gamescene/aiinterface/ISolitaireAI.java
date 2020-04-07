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
    void computeAction(GameState gameState, IGamePrompter prompter) throws IllegalGameStateException;


    class IllegalGameStateException extends Exception { }
}
