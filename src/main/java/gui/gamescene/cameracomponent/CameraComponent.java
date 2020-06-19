package gui.gamescene.cameracomponent;

import gui.gamescene.IComponent;
import gui.util.Loader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


/**
 * GUI Componeent to display an arbitrary image (used to display
 * computer vision image).
 *
 * Design note:
 * Image is dislayed by settings background of a Pane rather than using
 * ImageView in order to make it auto resizable easily.
 * Also, we're using Background classes to allow for images with an
 * URL (as is the case with CV).
 * */
public class CameraComponent extends ImageView implements IComponent {

    private StackPane container = new StackPane();
    private Pane imagePane = new Pane();
    private Text infoText;
    private Text fpsText;

    private Loader loader;

    private long fpsTimeStamp = 0;

    public CameraComponent(){
        container.getChildren().add(imagePane);

        loader = new Loader(80, 80, 16);
        loader.setVisible(false);
        container.getChildren().add(loader);

        infoText = new Text();
        infoText.setFont(new Font(16));
        infoText.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(infoText);


        fpsText = new Text();
        fpsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        fpsText.setFill(Color.ORANGE);
        StackPane.setMargin(fpsText, new Insets(10, 0, 0,0));
        StackPane.setAlignment(fpsText, Pos.TOP_LEFT);
        container.getChildren().add(fpsText);

    }

    public void startLoading(String loadingText) {
        hideAll();
        loader.setLoadingText(loadingText);
        loader.setVisible(true);
    }

    public void showError(String errorMessage){
        hideAll();
        infoText.setText("Error: " + errorMessage);
        infoText.setVisible(true);
    }

    public void updateImage(Image image){
        hideAll();
        imagePane.setVisible(true);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        imagePane.setBackground(background);
        updateFps();
    }


    private void updateFps(){
        long currentTime = System.currentTimeMillis();
        if( fpsTimeStamp != 0 ){
            double fps = 1000.0 / (currentTime-fpsTimeStamp);
            fpsText.setText(String.format("FPS: %.1f", fps));
        }
        fpsTimeStamp = currentTime;
    }


    private void hideAll(){
        infoText.setVisible(false);
        imagePane.setVisible(false);
        loader.setVisible(false);
    }

    @Override
    public Node getNode() {
        return container;
    }
}
