package gui.gamescene.gamecomponent;

import gui.gamescene.gamestate.GameState;
import gui.gamescene.IComponent;


public interface IGameComponent extends IComponent {
    void updateGameState(GameState gameState);
}
