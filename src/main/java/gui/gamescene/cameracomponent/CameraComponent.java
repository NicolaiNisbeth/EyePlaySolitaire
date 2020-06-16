package gui.gamescene.cameracomponent;

import gui.gamescene.IComponent;
import gui.util.Loader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


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

    private Loader loader;

    public CameraComponent(){
        loader = new Loader(80, 80, 16);
        loader.setVisible(false);
        container.getChildren().add(loader);
    }

    public void startLoading(String loadingText){
        loader.setLoadingText(loadingText);
        loader.setVisible(true);
    }

    public void stopLoading(){
        loader.setVisible(false);
    }

    public void updateImage(Image image){
        stopLoading();

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        imagePane.setBackground(background);
    }

    @Override
    public Node getNode() {
        return container;
    }
}
