package cv;

import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\willi\\IdeaProjects\\EyePlaySolitaire1\\src\\main\\java\\cv\\test4.txt");

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

        GameStateAnalyzer gameStateAnalyzer = new GameStateAnalyzer(width,height,4);

        // First set of detections
        gameStateAnalyzer.analyzeDetectionsTest(detectionList);

        // Second set of detections
        gameStateAnalyzer.analyzeDetectionsTest(detectionList);

        // Third set of detections
        gameStateAnalyzer.analyzeDetectionsTest(detectionList);
    }
}
