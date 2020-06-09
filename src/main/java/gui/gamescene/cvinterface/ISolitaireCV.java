package gui.gamescene.cvinterface;

import gui.gamescene.gamestate.GameState;
import javafx.scene.image.Image;


public interface ISolitaireCV {

    /**
     * Start registering information from camera, and analyze
     * the input.
     */
    void start();

    /**
     * Add an listener, to listen for an update on the image
     * which the computer vision uses.
     */
    void setImageUpdateListener(ImageUpdateListener imageUpdateListener);

    /**
     * Add an listener, to listen for new GameState to be
     * detected by the computer vision.
     */
    void setGameStateUpdateListener(GameStateUpdateListener gameStateUpdateListener);


    interface ImageUpdateListener{
        void onImageUpdate(Image image);
    }

    interface GameStateUpdateListener{
        void onGameStateUpdate(GameState newState);
    }
}
