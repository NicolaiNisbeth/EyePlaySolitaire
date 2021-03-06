package cv;

import gui.gamescene.cvinterface.ISolitaireCV.GameStateUpdateListener;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;

import java.util.*;


/**
 * Analyzes detections acquired from the Computer Vision client, and determines
 * whether or not the a new, valid game state exists.
 * If a new game state exists it notifies the given GameStateUpdateListener.
 */
public class GameStateAnalyzer {
    private List<Card> stock;
    private List<Detection> flipped;
    private List<List<Detection>> tableaus;
    private List<List<Detection>> foundations;
    private List<GameState> gameStates;

    private double offset_W = 0.15;
    private double stock_W = 0.3;
    private double stock_H = 0.27;
    private double tableau_W = 0.1;

    private int width;
    private int height;
    private int savedGameStates;
    private boolean changedGameStateDetected = false;

    /**
     * Creates a new game state where the List objects
     * are initialized as ArrayLists.
     */
    public GameStateAnalyzer(int width, int height, int savedDetection) {

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
        this.savedGameStates = savedDetection;
        this.gameStates = new ArrayList<>();
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
     */
    void analyzeDetections(List<Detection> detections){
        DividedDetections(detections);
        SaveCurrentDetectionsAsGameState();
        if(CurrentGameStateEqualsPrevious() && changedGameStateDetected){
            int latest = gameStates.size()-1;
            GameState latestGameState = gameStates.get(latest);
            changedGameStateDetected = false;
            updateListener.onGameStateUpdate(latestGameState);
        }
    }



    public GameState analyzeDetectionsTest(List<Detection> detections){
        DividedDetections(detections);
        //DetectedComputervisionError();
        SaveCurrentDetectionsAsGameState();
        return gameStates.get(gameStates.size()-1);
    }


    private void ClearSections(){
        flipped.clear();

        for( int i = 0; i < 4; i++){
            foundations.get(i).clear();
        }

        for( int i = 0; i < 7; i++){
            tableaus.get(i).clear();
        }

    }

    private void DividedDetections(List<Detection> detections){

        //Clear sections
        ClearSections();

        //System.out.println("Divided detections into sections ");
        for (Detection detection:detections) {

            //Get stock detections
            if( width*offset_W < detection.getX() &&
                width*offset_W + width*stock_W > detection.getX() &&
                height*stock_H > detection.getY()){
                    flipped.add(detection);
            }

            //Get foundations
            if(width*offset_W + width*stock_W < detection.getX() && height*stock_H > detection.getY()){
                for( int i = 0; i < 4; i++){
                    if( width*offset_W + width*stock_W + width*(tableau_W*i) < detection.getX() &&
                        width*offset_W + width*stock_W + width*(tableau_W*(i+1)) > detection.getX()){
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
    }


    private void SaveCurrentDetectionsAsGameState(){
        if(savedGameStates < gameStates.size()){
            gameStates.remove(0);
        }
        GameState currentGameState = createGameState();
        gameStates.add(currentGameState);
    }

    private GameState createGameState(){

        GameState gameState = new GameState();

        gameState.setFlipped(GetCardsInSection(flipped, true));

        for( int i = 0; i < 4; i++){
            gameState.setFoundations(GetCardsInSection(foundations.get(i),false), i);
        }

        for( int i = 0; i < 7; i++){
            gameState.setTableaus(GetCardsInSection(tableaus.get(i), false), i);
        }
        return gameState;
    }


    private List<Detection>FindMatchingDetections(List<Detection> detections,Detection detection ){
        List<Detection> pairs = new ArrayList<>();
        for (Detection current:detections) {
            if(detection.getCard().toString().equals(current.getCard().toString())){
                pairs.add(current);
            }
        }
        return pairs;
    }

    private List<Detection> RemoveMatchingDetectionsFromList(List<Detection> detections, Card card){

        List<Detection> updatedList = new ArrayList<>();
        for (Detection detection:detections) {
            if(!detection.getCard().toString().equals(card.toString())){
                updatedList.add(detection);
            }
        }
        return updatedList;
    }


    private List<Card> SortedCardList(List<Detection> detections){
        List<Card> cards = new ArrayList<>();
        for (Detection detection:detections) {
            cards.add(detection.getCard());
        }
        return cards;
    }

    private List<Card> GetCardsInSection(List<Detection> detections, boolean horizontal){

        List<Detection> allUniqueDetections = new ArrayList<>();
        while (detections.size() != 0){
            Detection temp = detections.get(0);
            List<Detection> pairs = FindMatchingDetections(detections,temp);
            temp = GetDetectionInLeftCorner(pairs);
            allUniqueDetections.add(temp);
            detections = RemoveMatchingDetectionsFromList(detections, temp.getCard());
        }

        if(horizontal){
            Collections.sort(allUniqueDetections, new Comparator<Detection>() {
                @Override
                public int compare(Detection lhs, Detection rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getX() < rhs.getX() ? -1 : (lhs.getX() > rhs.getX()) ? 1 : 0;
                }
            });
            return SortedCardList(allUniqueDetections);
        }else{
            Collections.sort(allUniqueDetections, new Comparator<Detection>() {
                @Override
                public int compare(Detection lhs, Detection rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getY() < rhs.getY() ? -1 : (lhs.getY() > rhs.getY()) ? 1 : 0;
                }
            });
            return SortedCardList(allUniqueDetections);
        }
    }

    private Detection GetDetectionInLeftCorner(List<Detection> detections){
        Detection leftCorner = detections.get(0);
        for(int i = 1; i < detections.size(); i++){
            if(detections.get(i).getX() < leftCorner.getX() || detections.get(i).getY() < leftCorner.getY()){
                leftCorner = detections.get(i);
            }
        }
        return leftCorner;
    }


    private boolean CurrentGameStateEqualsPrevious(){

        System.out.println("Comparing GameStates");

        if(gameStates.size() < 2){
            return true;
        }
        GameState current = gameStates.get(gameStates.size()-1);
        System.out.println("Detected game state: " + current);
        for(int i = 0; i < gameStates.size()-1; i++){
            if(!current.equals(gameStates.get(i))){
                changedGameStateDetected = true;
                return false;
            }
        }
        return true;
    }
}
