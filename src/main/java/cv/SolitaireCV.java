package cv;

import cv.communication.ClientStarter;
import cv.communication.Message;
import cv.communication.Server;
import gui.gamescene.cvinterface.ISolitaireCV;
import org.json.JSONObject;

import java.io.IOException;

public class SolitaireCV implements ISolitaireCV, Server.ClientConnectCallback {

    private Server server;

    public SolitaireCV() throws IOException {
        server = new Server(
                (msg) -> System.out.println("Received message: " + msg),
                this);
        int port = server.start();
        System.out.println("Started server on port " + port);

        ClientStarter clientStarter = new ClientStarter();
        clientStarter.setStandardOutputListener((msg) -> System.out.println("Message: " + msg));
        clientStarter.setErrorOutputListener((msg) -> System.out.println("Error: " + msg));
        clientStarter.setProcessFinishedCallback((exitCode) -> System.out.println("Client process finished with exit code " + exitCode));
        clientStarter.start(port);

    }


    @Override
    public void addImageUpdateListener() {

    }

    @Override
    public void addGameStateUpdateListener() {

    }


    public static void main(String[] args) throws IOException {
        SolitaireCV solitaireCV = new SolitaireCV();
    }

    @Override
    public void onClientConnect() {
        try {
            server.sendMessage(new Message(1, new JSONObject("{\"msg\" : \"Hello client!\"}")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
