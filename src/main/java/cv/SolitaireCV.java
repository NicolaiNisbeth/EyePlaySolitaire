package cv;

import cv.communication.ClientStarter;
import cv.communication.Message;
import cv.communication.Server;
import gui.gamescene.cvinterface.ISolitaireCV;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;


public class SolitaireCV implements ISolitaireCV, Server.ClientConnectCallback, Server.MessageListener, Server.ErrorListener {

    private Server server;
    private ImageUpdateListener imageUpdateListener;
    private GameStateAnalyzer gameStateAnalyzer = new GameStateAnalyzer(416,416,2);
    private boolean paused = true;
    private ErrorListener errorListener;
    private InitializedCallback initializeCallback;


    @Override
    public void initialize(InitializedCallback callback) {
        this.initializeCallback = callback;
        try {
            // Start the server
            server = new Server(this, this);
            int port = 0;
            port = server.start();
            System.out.println("Started computer vision server on port " + port);

            // Start the client
            System.out.println("Starting client");
            ClientStarter clientStarter = new ClientStarter();
            clientStarter.setStandardOutputListener(this::printClientMessage);
            clientStarter.setErrorOutputListener(this::printClientMessage);
            clientStarter.setProcessFinishedCallback(this::clientFinished);
            clientStarter.start(port);

        } catch (IOException e) {
            e.printStackTrace();
            onError("Error when starting computer vision ('"+e.getMessage()+"')");
        }
    }


    @Override
    public void start() {
        paused = false;

        try{
            // Signal Client to start detections
            server.sendMessage(new Message(200, null));
        }catch(IOException e){
            e.printStackTrace();
            errorListener.onError("Server error when sending start message to client");
        }
    }


    @Override
    public void pause() {
        paused = true;
        try{
            // Signal Client to pause detections
            server.sendMessage(new Message(201, null));
        }catch(IOException e){
            e.printStackTrace();
            errorListener.onError("Server error when sending stop message to client");
        }
    }


    @Override
    public void setImageUpdateListener(ImageUpdateListener imageUpdateListener) {
        this.imageUpdateListener = imageUpdateListener;
    }


    @Override
    public void setGameStateUpdateListener(GameStateUpdateListener gameStateUpdateListener) {
        gameStateAnalyzer.setUpdateListener(gameStateUpdateListener);
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }


    @Override
    public void onClientConnect() { }


    // Handles incoming messages from client
    @Override
    public void onServerMessage(Message message) {

        try{
            switch(message.getCode()){
                case 100:
                    // Client is ready
                    System.out.println("CV client is ready!");
                    server.sendMessage(new Message(202, null));
                    initializeCallback.onComputerVisionInitialized();
                    break;
                case 101: // New Detections
                    decodeDetections(message.getData());
                    break;
                case 102: // New Image
                    JSONObject data = message.getData();
                    if( data.length() == 0 ){
                        server.sendMessage(new Message(202, null));
                    }else{
                        decodeImageMessage(data.getString("image"), data.getInt("width"), data.getInt("height"));
                    }
                    break;

                default:
                    System.out.printf("CV: Recieved message with unknown code %d from client\n", message.getCode());

            }
        }catch(IOException e){
            e.printStackTrace();
            errorListener.onError("Server error occured when sending message to client");
        }

    }


    /*  Decodes an image encoded as base64 into a JavaFX image and notify
    *   the ImageUpdateListener that a new image has been received */
    private void decodeImageMessage(String imageStringData, int width, int height){

        try {
            // Convert from base64 string to byte array
            byte[] imageData = Base64.getDecoder().decode(imageStringData);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(imageData, 0, targetPixels, 0, imageData.length);

            // Convert to JavaFX image and notify
            Image fxImage = SwingFXUtils.toFXImage(image, null);
            if( imageUpdateListener != null )
                imageUpdateListener.onImageUpdate(fxImage);

            // Signal to client that image was received
            server.sendMessage(new Message(202, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void decodeDetections(JSONObject data){
        JSONArray jsonDetections = data.getJSONArray("detections");
        List<Detection> detections = new LinkedList<>();
        for(Object detection : jsonDetections ){
            detections.add(Detection.fromJSON((JSONObject) detection));
        }
        gameStateAnalyzer.analyzeDetections(detections);
    }


    @Override
    public void onServerError(String errorMessage) {
        onError(errorMessage);
    }


    private void clientFinished(int exitCode){
        switch(exitCode){
            case 101:
                onError("Client could not find .weight file");
                break;
            default:
                onError("Computer vision client terminated with unknown status code " + exitCode);
        }
    }


    private void onError(String errorMessage){
        System.out.println("Computer vision error: " + errorMessage);
        if(errorListener != null)
            errorListener.onError(errorMessage);
    }

    private void printClientMessage(String message){
        System.out.println("\u001B[90m" + message + "\u001B[0m");
    }

}
