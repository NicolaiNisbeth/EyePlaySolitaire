package gui;

import gui.setupscene.SetupScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *  The first scene, which allows you chose custom settings when starting
 *  the program.
 */
public class SceneController extends Application {
    private static SceneController sceneController;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        sceneController = this;
        stage.setTitle("Eye Play Solitaire ");
        stage.show();
        stage.setOnCloseRequest((arg) -> Platform.exit()); // Close all windows on exit
        this.stage = stage;

        setScene(new SetupScene());
    }

    /** Sets the current scene of the window. The scene controls
     * what is displayed and control the different logic components. */
    public void setScene( Scene scene) {
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public static SceneController get() {
        return sceneController;
    }

    // Main method HAVE to be in same class as the extender of Application
    public static void main(String[] args) {
        launch();
        /* The window launch takes over the main thread ,
        *  so any code below, won't be run before window is closed. */
    }

}
