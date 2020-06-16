package cv;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;


/**
 * Output files from test data generator is broken, but salvagable,
 * so this code fixes those files.
 * (also a tad more readable)
 */
public class DataFixer {

    public static void main(String[] args) throws IOException {
        File directoryPath = new File("src/main/test/cv/testdata/broken/");

        for( File file : directoryPath.listFiles() ){
            try{
                fixFile(file);
            }catch(IOException e){
                e.printStackTrace();
            }

        }

    }

    private static void fixFile(File file) throws IOException {
        System.out.printf("Fixing file: '%s'\n", file.getAbsolutePath());

        FileReader reader = new FileReader(file);
        int input;
        String data = "";
        while ((input = reader.read()) != -1)
            data += (char) input;
        reader.close();


        // Read in data
        JSONObject testData = new JSONObject(data);
        JSONObject expectedResults = testData.getJSONObject("expectedResults");

        // Fix foundations
        JSONArray fixedFoundations = fixList(expectedResults.getJSONArray("foundations"));

        // Fix foundations
        JSONArray fixedDrawn = fixList(expectedResults.getJSONArray("drawn"));

        // Update the object
        expectedResults.remove("foundations");
        expectedResults.put("foundations", fixedFoundations);

        expectedResults.remove("drawn");
        expectedResults.put("drawn", fixedDrawn);

        testData.remove("expectedResults");
        testData.put("expectedResults", expectedResults);


        File fixedFile = new File("src/main/test/cv/testdata/" + file.getName());
        if (fixedFile.exists()) throw new IOException("File has already been fixed!");
        PrintWriter writer = new PrintWriter(new FileOutputStream(fixedFile));
        writer.write(testData.toString(4));
        writer.close();

        System.out.println("==============================================================");
        System.out.println("FIXED FILE");
        System.out.println(testData.toString(4));

    }


    public Detections readDetections(){

    }

    private static JSONArray fixList(JSONArray brokenList){

        JSONArray fixedList = new JSONArray();
        for(Object cardObj : brokenList ){
            JSONObject cardJson = (JSONObject) cardObj;
            String identifier = cardJson.getString("suit").substring(0,1).toUpperCase();
            identifier += cardJson.getInt("value");
            fixedList.put(identifier);
        }

        return fixedList;
    }

}
