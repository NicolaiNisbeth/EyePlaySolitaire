package cv;

import gui.gamescene.cvinterface.ISolitaireCV.GameStateUpdateListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Analyzes detections acquired from the Computer Vision client, and determines
 * whether or not the a new, valid game state exists.
 * If a new game state exists it notifies the given GameStateUpdateListener.
 */
class GameStateAnalyzer {

    private List<Detection> flipped;
    private List<List<Detection>> tableaus;
    private List<List<Detection>> foundations;

    private double offset_W = 0.15;
    private double stock_W = 0.3;
    private double stock_H = 0.27;
    private double tableau_W = 0.1;
    private double tableau_H = 0.63;

    private int width;
    private int height;


    /**
     * Creates a new game state where the List objects
     * are initialized as ArrayLists.
     */
    public GameStateAnalyzer(int width, int height) {

        this.flipped = new ArrayList<>();

        this.tableaus = new ArrayList<>();
        for( int i=0; i<7; i++){
            tableaus.add(new ArrayList<>());
        }

        this.foundations = new ArrayList<>();
        for( int i=0; i<4; i++){
            foundations.add(new ArrayList<>());
        }

        this.width = width;
        this.height = height;
    }

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

    void analyzeDetections2(List<Detection> detections){


        // Get stock detections;
        System.out.println("Divid detections into sections ");
        for (Detection detection:detections) {

            //Get stock detections
            if(width*offset_W < detection.getX() && width*offset_W + width*stock_W > detection.getX() && height*stock_H > detection.getY()){
                flipped.add(detection);
            }

            //Get foundations
            if(width*offset_W + width*stock_W < detection.getX() && height*stock_H > detection.getY()){
                for( int i = 0; i < 4; i++){
                    if( width*offset_W + width*stock_W + width*tableau_W*i < detection.getX() &&
                        width*offset_W + width*stock_W + width*tableau_W*(i+1) > detection.getX()){
                            foundations.get(i).add(detection);
                    }
                }
            }
            //Get tableaus
            if(height*stock_H < detection.getY()){
                for( int i = 0; i < 7; i++){
                    if( width*offset_W + width*tableau_W*i < detection.getX() &&
                        width*offset_W + width*tableau_W*(i+1) > detection.getX()){
                            tableaus.get(i).add(detection);
                    }
                }
            }
        }

        // Get stock detections;
        System.out.println("Check detection in sections  ");

        System.out.println("Detections in stock");
        for (Detection detection:flipped) {
            System.out.println(detection);
        }
        for( int i = 0; i < 4; i++){
            System.out.println("Detections in foundations "+(i+1));
            for (Detection detection:foundations.get(i)) {
                System.out.println(detection);
            }
        }

        for( int i = 0; i < 7; i++){
            System.out.println("Detections in tableaus "+(i+1));
            for (Detection detection:tableaus.get(i)) {
                System.out.println(detection);
            }
        }


    }

}
