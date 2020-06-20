package cv;

import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameStateTester {


    public static void testDetection(String fileName) throws IOException {

        // Load test file
        File file = new File("src/test/java/cv/testdata/"+fileName+".json");
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

        // Get test data
        JSONArray jsonDetections = testData.getJSONArray("detections");
        List<Detection> detections = new LinkedList<>();
        for(Object detection : jsonDetections ){
            detections.add(Detection.fromJSON((JSONObject) detection));
        }
        int width = testData.getInt("width");
        int height = testData.getInt("height");

        // Start analyzer
        GameStateAnalyzer analyzer = new GameStateAnalyzer(width, height,4);
        GameState detectedState = analyzer.analyzeDetectionsTest(detections);

        assertTrue("Game states does not match", expectedState.equals(detectedState));
    }




    private static GameState jsonToExpectedState(JSONObject testResults){
        // Get expected test results
        List<List<Card>> tableaus = new ArrayList<>();
        for(Object tableauObj : testResults.getJSONArray("tableaus")){
            tableaus.add(toCardList((JSONArray) tableauObj));
        }

        // Decode Foundations
        List<List<Card>> foundations = new ArrayList<>();
        List<Card> foundationsTop = toCardList(testResults.getJSONArray("foundations"));
        for(int i=0; i<4; i++){
            foundations.add(new ArrayList<>());
            Card foundationTop = foundationsTop.get(i);
            if( foundationTop != null ){
                foundations.get(i).add(foundationTop);
            }
        }

        List<Card> flipped = toCardList(testResults.getJSONArray("drawn"));
        GameState expectedState = new GameState(new ArrayList<>(), flipped, tableaus, foundations );

        return expectedState;
    }


    private static  List<Card> toCardList(JSONArray jsonlist){
        List<Card> cardList = new ArrayList<>();
        for(Object cardIdentifierObj : jsonlist.toList()){
            String cardIdentifier = (String) cardIdentifierObj;
            Card card = null;
            if(cardIdentifier.length() > 0){
                card = new Card(
                        getSuiteFromLetter(cardIdentifier.substring(0,1)),
                        Integer.parseInt(cardIdentifier.substring(1))
                );
            }
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


    static class UpdateListenerStub implements ISolitaireCV.GameStateUpdateListener {

        boolean receivedGameState = false;
        GameState expectedState = null;

        @Override
        public void onGameStateUpdate(GameState detectedState) {
            assertEquals("Game states does not match", expectedState, detectedState);
            receivedGameState = true;
        }
    }

}
