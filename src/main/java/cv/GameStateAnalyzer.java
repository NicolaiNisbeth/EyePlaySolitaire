package cv;

import gui.gamescene.cvinterface.ISolitaireCV.GameStateUpdateListener;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;

import java.util.ArrayList;
import java.util.List;


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
    private double tableau_H = 0.63;

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
    public void analyzeDetections(List<Detection> detections){
        DividedDetections(detections);
        //DetectedComputervisionError();
        SaveCurrentDetectionsAsGameState();
        if(!CurrentGameStateEqualsPrevious() && changedGameStateDetected){
            System.out.println("Updating gamestate");
            gameStates.get(0).toString();
            changedGameStateDetected = false;
            //updateListener.onGameStateUpdate(gameStates.get(0));
        }
    }

    public void analyzeDetectionsTest(List<Detection> detections){
        DividedDetections(detections);
        //DetectedComputervisionError();
        SaveCurrentDetectionsAsGameState();
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
    }

    private void PrintDetectionInSections(){
        System.out.println("Check detection in sections  ");
        System.out.println("Detections in stock");
        for (Detection detection:flipped) {
            System.out.println(detection);
        }
        for( int i = 0; i < 4; i++){
            System.out.println("Detections in foundation "+(i+1));
            for (Detection detection:foundations.get(i)) {
                System.out.println(detection);
            }
        }

        for( int i = 0; i < 7; i++){
            System.out.println("Detections in tableau "+(i+1));
            for (Detection detection:tableaus.get(i)) {
                System.out.println(detection);
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

        gameState.setFlipped(GetCardsInSection(flipped));

        for( int i = 0; i < 4; i++){
            gameState.setFoundations(GetCardsInSection(foundations.get(i)), i);
        }

        for( int i = 0; i < 7; i++){
            gameState.setTableaus(GetCardsInSection(tableaus.get(i)), i);
        }
        return gameState;
    }

    void DetectedComputervisionError(){

        if(CheckForMatchingErrorInSections()){
            System.out.println("MatchingError found");
        }
    }

    private boolean CheckForMatchingErrorInSections(){

        System.out.println("Check For Matching Error In Sections");

        if(FindMatchingPairError(flipped)){
            System.out.println("Error in flipped");
            return true;
        }

        for( int i = 0; i < 4; i++){
            if(FindMatchingPairError(foundations.get(i))){
                System.out.println("Error in foundations "+(i+1));
                return true;
            }
        }

        for( int i = 0; i < 7; i++){
            if(FindMatchingPairError(tableaus.get(i))){
                System.out.println("Error in tableau "+(i+1));
                return true;
            }
        }
        return false;
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

    private List<Detection> SortDetections(List<Detection> detections){

        detections.sort((o1, o2) -> {
            if (o1.getY() < o2.getY()) {
                return 0;
            } else {
                return 1;
            }
        });
        return detections;
    }

    private List<Card> SortedCardList(List<Detection> detections){
        List<Card> cards = new ArrayList<>();
        for (Detection detection:detections) {
            cards.add(detection.getCard());
        }
        return cards;
    }

    private List<Card> GetCardsInSection(List<Detection> detections){

        List<Detection> allUniqueDetections = new ArrayList<>();
        while (detections.size() != 0){
            Detection temp = detections.get(0);
            List<Detection> pairs = FindMatchingDetections(detections,temp);
            temp = GetDetectionInLeftCorner(pairs);
            allUniqueDetections.add(temp);
            detections = RemoveMatchingDetectionsFromList(detections, temp.getCard());
        }
        return SortedCardList(SortDetections(allUniqueDetections));
    }



    private Detection GetDetectionInLeftCorner(List<Detection> detections){
        Detection leftCorner = detections.get(0);
        for(int i = 1; i < detections.size(); i++){
            if(detections.get(i).getX() < leftCorner.getX() ||detections.get(i).getY() < leftCorner.getY()){
                leftCorner = detections.get(i);
            }
        }
        return leftCorner;
    }


    private boolean FindMatchingPairError(List<Detection> detections){
        for (Detection detection:detections) {
            String currentCard = detection.getCard().toString();
            int cardDetected = 0;
            for (Detection current:detections) {
                if(currentCard.equals(current.getCard().toString())){
                    cardDetected++;
                }
            }
            if(cardDetected < 2){
                System.out.println(currentCard+ " is only detected ones");
                return true;
            }
        }
        return false;
    }

    public void PrintCardsInSection(){

        System.out.println("Cards in stock");
        for (Card card :GetCardsInSection(flipped)) {
            System.out.println(card);
        }
        for( int i = 0; i < 4; i++){
            System.out.println("Cards in foundation "+(i+1));
            for (Card card:GetCardsInSection(foundations.get(i))) {
                System.out.println(card);
            }
        }

        for( int i = 0; i < 7; i++){
            System.out.println("Cards in tableau "+(i+1));
            for (Card card:GetCardsInSection(tableaus.get(i))) {
                System.out.println(card);
            }
        }
    }

    private boolean CurrentGameStateEqualsPrevious(){

        System.out.println("Comparing GameStates");

        if(gameStates.size() < 2){
            return true;
        }
        GameState current = gameStates.get(0);
        System.out.println(current.toString());
        for(int i = 1; i < gameStates.size(); i++){
            if(!current.equals(gameStates.get(i))){
                changedGameStateDetected = true;
                return false;
            }
        }
        return true;
    }
}
