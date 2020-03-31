package gui.gamescene.cameracomponent;

import gui.gamescene.IComponent;
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

    private Pane imagePane = new Pane();

    public void updateImage(Image image){
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        imagePane.setBackground(background);
    }

    @Override
    public Node getNode() {
        return imagePane;
    }
}
