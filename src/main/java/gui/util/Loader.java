package gui.util;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 *  Loading view to display a loading gif a long with
 *  a loading text.
 */
public class Loader extends VBox {

    private static Image IMAGE = new Image("images/loadingspinner.gif", false);

    private static ImageView spinner;
    private static Text loadingText;

    public Loader(int width, int height, int fontSize){
        spinner = new ImageView(IMAGE);
        spinner.setFitWidth(width);
        spinner.setFitHeight(height);
        getChildren().add(spinner);

        loadingText = new Text();
        loadingText.setFont(new Font(fontSize));
        loadingText.setTextAlignment(TextAlignment.CENTER);
        getChildren().add(loadingText);

        this.setAlignment(Pos.CENTER);
    }

    public void setLoadingText(String text){
        loadingText.setText(text);
    }

}
