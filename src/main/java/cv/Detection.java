package cv;

import gui.gamescene.gamestate.Card;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a detection of a card label (corners) by the computer vision client.
 */
public class Detection {

    // The card the label belongs to
    private Card card;

    // Confidence level (0 to 1.0)
    private double confidence;

    // Center coordinates of the label
    private double x;
    private double y;

    private double width;
    private double height;

    // You shouldn't create a detection except from json
    private Detection() { }


    /**
     * Constructs a Detection from a JSON object in the format:
     * {
     *     "card" : { "suit" : "D", "value" 13 },
     *     "confidence" : 0.85,
     *     "x" :  123.12, "y" : 123,12,
     *     "width" : 123.12, "height" : 123.12
     * }
     */
    static Detection fromJSON(JSONObject json){
        Detection detection = new Detection();

        // Decode the card
        JSONObject jsonCard = json.getJSONObject("card");
        String jsonSuit = jsonCard.getString("suit");
        Card.Suit suit = Card.Suit.UNKNOWN;
        if( jsonSuit.equals("C")) suit = Card.Suit.CLUBS;
        if( jsonSuit.equals("D")) suit = Card.Suit.DIAMONDS;
        if( jsonSuit.equals("H")) suit = Card.Suit.HEARTS;
        if( jsonSuit.equals("S")) suit = Card.Suit.SPADES;
        detection.card = new Card(suit, jsonCard.getInt("value"));

        // Decode other values
        detection.confidence = json.getDouble("confidence");
        detection.x = json.getDouble("x");
        detection.y = json.getDouble("y");
        detection.width = json.getDouble("width");
        detection.height = json.getDouble("height");

        return detection;
    }

    public Card getCard() {
        return card;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Detection{ " +
                "card=" + card +
                ", confidence=" + confidence +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                " }";
    }


    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\willi\\IdeaProjects\\EyePlaySolitaire1\\src\\main\\java\\cv\\test3.txt");

        FileReader reader = new FileReader(file);

        int input;
        String text = "";
        while( (input = reader.read()) >= 0){
            if( input != '\n' && input != '\r')
                text += (char) input;
        }

        List<Detection> detectionList = new ArrayList();

        JSONObject object = new JSONObject(text).getJSONObject("detections");
        int width =  object.getInt("width");
        int height = object.getInt("height");

        for(Object detection : object.getJSONArray("detections")){

            Detection currentDetection = Detection.fromJSON((JSONObject) detection);
            detectionList.add(currentDetection);
        }

        GameStateAnalyzer gameStateAnalyzer = new GameStateAnalyzer(width,height,2);

        gameStateAnalyzer.DividedDetections(detectionList);
        gameStateAnalyzer.DetectedComputervisionError();

        }




}
