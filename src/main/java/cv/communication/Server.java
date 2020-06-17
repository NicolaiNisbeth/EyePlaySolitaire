package cv.communication;


import cv.Detection;
import cv.GameStateAnalyzer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port = 0;

    private MessageListener messageListener;
    private ClientConnectCallback clientConnectCallback;
    private ErrorListener errorListener;

    private ServerSocket server;
    private Socket client;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;

    private Thread clientThread;

    /**
     * Defines a new server with a set of listeners. The server won't be started
     * automatically.
     *
     * @param messageListener Listener to be notified when a message is received from the client
     * @param clientConnectCallback Called when the client connects to the server (only called once)
     */
    public Server( MessageListener messageListener, ClientConnectCallback clientConnectCallback  ) {
        this.messageListener = messageListener;
        this.clientConnectCallback = clientConnectCallback;
    }


    /**
     * Start the Server with a random port number
     *
     * @return The port number on which the server is listening
     * @throws IOException If an error occurs while starting the server
     */
    public int start() throws IOException {
        server = new ServerSocket(0);
        port = server.getLocalPort();
        awaitClient();
        return port;
    }

    public void stop(){

    }


    // Listens for Client connection on a seperate client thread
    private void awaitClient() {
        clientThread = new Thread( () -> {
            // IO Exception may occur
            try{
                setupClientCommunication(server.accept());
            }catch(IOException e){
                e.printStackTrace();
                notifyError("Error occured when awaiting client: " +e.getMessage());
            }
        });
        clientThread.start();
    }


    // Setup communication with newly connected client
    private void setupClientCommunication(Socket newClient){
        client = newClient;

        // Setup IO streams
        try{
            outputStream = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            inputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
            notifyError("Error occured when setting up IO streams for client communication: " +e.getMessage());
        }

        // Notify callback
        if( clientConnectCallback != null )
            clientConnectCallback.onClientConnect();

        // We're still on background thread, so we don't have to worry
        // about this method blocking
        listenForMessages();
    }


    /* Starts a blocking, endless while loop to listen for incoming message
        on the connection inputsream. */
    private void listenForMessages(){
        while(true){
            String input = "";
            try {
                input = inputStream.readLine();
                if (input == null) break;
                JSONObject jsonMessage = new JSONObject(input);
                Message message = Message.fromJSON(jsonMessage);
                if (messageListener != null) {
                    messageListener.onServerMessage(message);
                }
            }catch(SocketException e){
                notifyError("Error occured when listening for messages from client: " + e.getMessage());
                break;
            }catch(IOException e) {
                e.printStackTrace();
                notifyError("Error occured when listening for messages from client: " + e.getMessage());
            }catch(JSONException e){
                e.printStackTrace();
                notifyError("Error occured when processing message '" + input + "' from client: " + e.getMessage());
            }
        }
    }

    /**
     * Sends a message to the Client.
     */
    public void sendMessage(Message message) throws IllegalStateException, IOException {
        if( client == null || client.isClosed() )
            throw new IllegalStateException("No client has connected to the server yet");

        // Write the data
        for( char c : message.toJSON().toString().toCharArray() )
            outputStream.write(c);
        outputStream.newLine();
        outputStream.flush();
    }


    /**
     * @return The port number on which the server has started, or 0 if the server hasn't been started yet
     */
    public int getPort() {
        return port;
    }


    /** Set an error listener which will be notified for any asynchronous
     *  errors which may occur
     */
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }


    // Notifies the error listeners than an error has occured
    private void notifyError(String errorMessage){
        if( errorListener != null )
            errorListener.onServerError(errorMessage);
    }


    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    // Functional interfaces

    /** Called when a Client connects to the server */
    public interface ClientConnectCallback {
        void onClientConnect();
    }

    /** Listens for messages received from the client,
     *  connected to the Server */
    public interface MessageListener {
        void onServerMessage(Message message) throws IOException;
    }

    /** Listens for errors happening within the Server */
    public interface ErrorListener {
        void onServerError(String errorMessage);
    }
}
