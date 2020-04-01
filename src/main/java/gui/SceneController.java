package gui;

import gui.gamescene.GameScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;


public class SceneController extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Eye Play Solitaire ");
        stage.show();
        stage.setScene(new GameScene());
        this.stage = stage;
    }

    /** Sets the current scene of the window. The scene controls
     * what is displayed and control the different logic components. */public void setScene( Scene scene) {
        stage.setScene(scene);
    }

    // Main method HAVE to be in same class as the extender of Application
    public static void main(String[] args) {
        //TODO: Move this to OpenCV module at some point
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        launch();
        /* The window launch takes over the main thread ,
        *  so any code below, won't be run before window is closed. */
    }

}
