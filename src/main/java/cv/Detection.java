package cv;

import gui.gamescene.gamestate.Card;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Detection {

    private Card card;
    private double confidence;
    private double x;
    private double y;
    private double width;
    private double height;


    public static Detection fromJSON(JSONObject json){
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

        File file = new File("C:\\Users\\malte\\IdeaProjects\\EyePlaySolitaire\\src\\main\\java\\cv\\detections.test");

        FileReader reader = new FileReader(file);

        int input;
        String text = "";
        while( (input = reader.read()) >= 0){
            if( input != '\n' && input != '\r')
                text += (char) input;
        }

        JSONObject object = new JSONObject(text);

        for(Object detection : object.getJSONArray("detections"))
            System.out.println(Detection.fromJSON((JSONObject) detection));
            }
}
