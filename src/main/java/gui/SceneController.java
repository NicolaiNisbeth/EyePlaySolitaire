package gui;

import gui.gamescene.GameScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController extends Application {

    public Stage stage;


    @Override
    public void start(Stage stage) {


        stage.setTitle("Hangman Online Profile Manager");
        stage.show();
        stage.setScene(new GameScene());

        this.stage = stage;
    }


    public void setScene( Scene scene) {
        stage.setScene(scene);
    }





    // Main method HAVE to be in same class as the extender of Application
    public static void main(String[] args) {
        launch();
        /* The window launch takes over the main thread ,
        *  so an code below, won't be run before window is closed. */
    }

}
