package cv;

import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static gui.gamescene.cvinterface.ISolitaireCV.*;
import static org.junit.Assert.*;

public class GameStateAnalyzerTest {


    public static void main(String[] args) throws IOException {
        // Load test file
        File file = new File("src/main/test/cv/testdata/mar1.json");
        FileReader reader = new FileReader(file);
        int input;
        String data = "";
        while( (input = reader.read()) != -1 )
            data += (char) input;
        reader.close();

        // Read in data
        JSONObject testSuite = new JSONObject(data);
        JSONObject testData = testSuite.getJSONObject("detections");
        JSONObject testResults = testSuite.getJSONObject("expectedResults");

        // Build expected state
        GameState expectedState = jsonToExpectedState(testResults);

        System.out.println(expectedState);



    }


    void testDetection(String fileName) throws IOException {

        // Load test file
        File file = new File("testdata/"+fileName);
        FileReader reader = new FileReader(file);
        int input;
        String data = "";
        while( (input = reader.read()) != -1 )
            data += input;
        reader.close();

        // Read in data
        JSONObject testSuite = new JSONObject(data);
        JSONObject testData = testSuite.getJSONObject("detections");
        JSONObject testResults = testSuite.getJSONObject("expectedResults");

        // Build expected state
        GameState expectedState = jsonToExpectedState(testResults);

        // Get test data
        JSONArray jsonDetections = testData.getJSONArray("detections");
        List<Detection> detections = new LinkedList<>();
        for(Object detection : jsonDetections ){
            detections.add(Detection.fromJSON((JSONObject) detection));
        }
        int width = testData.getInt("width");
        int height = testData.getInt("height");

        // Listener which receives the detected game state
        UpdateListenerStub updateListener = new UpdateListenerStub();
        updateListener.expectedState = expectedState;

        // Start analyzer
        GameStateAnalyzer analyzer = new GameStateAnalyzer(width, height);
        analyzer.setUpdateListener(updateListener);
        analyzer.analyzeDetections(detections, width, height);

        // Assert that a game state was received
        assertTrue(updateListener.receivedGameState);
    }




    private static GameState jsonToExpectedState(JSONObject testResults){
        // Get expected test results
        List<List<Card>> tableaus = new ArrayList<>();
        for(Object tableauObj : testResults.getJSONArray("tableaus")){
            tableaus.add(toCardList((JSONArray) tableauObj));
        }

        List<List<Card>> foundations = new ArrayList<>();
        List<Card> foundationsTop = toCardList(testResults.getJSONArray("foundations"));
        for(Card foundationTop : foundationsTop ){
            List<Card> foundation = new ArrayList<>();
            foundation.add(foundationTop);
            foundations.add(foundation);
        }

        List<Card> flipped = toCardList(testResults.getJSONArray("drawn"));
        GameState expectedState = new GameState(new ArrayList<>(), flipped, tableaus, foundations );

        return expectedState;
    }


    private static  List<Card> toCardList(JSONArray jsonlist){
        List<Card> cardList = new ArrayList<>();
        for(Object cardIdentifierObj : jsonlist.toList()){
            String cardIdentifier = (String) cardIdentifierObj;
            Card card = new Card(
                    getSuiteFromLetter(cardIdentifier.substring(0,1)),
                    Integer.parseInt(cardIdentifier.substring(1))
            );
            cardList.add(card);
        }
        return cardList;
    }

    private static Card.Suit getSuiteFromLetter(String letter){
        for(Card.Suit suit : Card.Suit.values() ){
            if( suit.toString().substring(0,1).toUpperCase().equals(letter))
                return suit;
        }
        throw new NullPointerException("Couldnt find suit with starting letter " +letter);
    }

    class UpdateListenerStub implements GameStateUpdateListener {

        boolean receivedGameState = false;
        GameState expectedState = null;

        @Override
        public void onGameStateUpdate(GameState detectedState) {
            assertEquals(expectedState, detectedState);
            receivedGameState = true;
        }
    }

}
