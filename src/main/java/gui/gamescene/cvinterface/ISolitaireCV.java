package gui.gamescene.cvinterface;

import gui.gamescene.gamestate.GameState;
import javafx.scene.image.Image;


public interface ISolitaireCV {


    /**
     * Start registering information from camera, and analyze
     * the input.
     */
    void start();


    // TODO: Remove pause / unpause mechanism if it's left unused
    /**
     *  Pauses the computer vision from analyzing detections into GameState objects
     *  thus stopping it from outputting any new GameStates to the update listener.
     *  It will still update the image.     *
     */
    void pause();


    /**
     *  Unpasuses the computer vision making it start outputting GameStates again.
     */
    void unpause();

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


    /**
     *  Add a callback to fire once the Computer Vision terminates.
     */
    void setFinishedCallback(FinishedCallback callback);


    interface ImageUpdateListener{
        void onImageUpdate(Image image);
    }

    interface GameStateUpdateListener{
        void onGameStateUpdate(GameState newState);
    }

    interface FinishedCallback {
        void onFinish(boolean error);
    }
}
