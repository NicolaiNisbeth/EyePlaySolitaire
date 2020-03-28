package gui.gamescene.gamecomponent;

import gui.gamescene.GameState;
import gui.gamescene.IComponent;

public interface IGameComponent extends IComponent {

    void updateGameState(GameState gameState);
    //void promptGameAction(GameAction gameAction);

}
