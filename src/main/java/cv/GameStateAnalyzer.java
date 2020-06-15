package cv;

import gui.gamescene.cvinterface.ISolitaireCV.GameStateUpdateListener;
import java.util.List;


/**
 * Analyzes detections acquired from the Computer Vision client, and determines
 * whether or not the a new, valid game state exists.
 * If a new game state exists it notifies the given GameStateUpdateListener.
 */
class GameStateAnalyzer {

    // The update listener to be called when a new game state has been
    // successfully detected
    private GameStateUpdateListener updateListener = null;


    void setUpdateListener(GameStateUpdateListener updateListener) {
        this.updateListener = updateListener;
    }


    /**
     * Analyzes detections acquired from the Computer Vision client, and determines
     * whether or not the a new, valid game state exists.
     * If a new game state exists it notifies the given GameStateUpdateListener.
     *
     * @param detections    A list of detections from the Computer Vision client. Things to note about these
     *                      detections are:
     *                          - The detections are >NOT< sorted, meaning there are definitely duplicates
     *                          - Coordinates are center of the detection (not the card, but the card label)
     *                          - Coordinates are coordinates within widthxheight resolution
     *
     * @param width         Width of the detections coordinate system
     * @param height        Height of the detections coordinate system
     */
    void analyzeDetections(List<Detection> detections, int width, int height){

    }


}
