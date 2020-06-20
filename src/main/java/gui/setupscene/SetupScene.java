package gui.setupscene;

import gui.SceneController;
import gui.gamescene.GameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class SetupScene extends Scene {

    private VBox container;
    private CheckBox checkBox_ManualCV;
    private CheckBox checkBox_ManualAI;
    private CheckBox checkBox_PredefinedStock;

    public SetupScene() {
        super(new VBox(), 350, 450);

        // Setup container
        container = (VBox) getRoot();
        container.setPadding(new Insets(10,10,10,10));
        container.setAlignment(Pos.TOP_CENTER);


        Text title = new Text();
        title.setText("Eye Play Solitaire");
        title.setFont(Font.font("verdana", FontWeight.BOLD, 30));
        VBox.setMargin(title, new Insets(20, 0, 10, 0));
        container.getChildren().add(title);

        Text description = new Text();
        description.setText("Choose your startup settings and press 'Start' to run the program.");
        description.setFont(new Font(14));
        description.setWrappingWidth(getWidth()*0.90);
        description.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(description, new Insets(0, 0, 30, 0));
        container.getChildren().add(description);

        checkBox_ManualAI = new CheckBox("Use manual AI");
        addSetting(checkBox_ManualAI, "Disable the AI, and instead manually enter the changes happening to the game via a console.");

        checkBox_ManualCV = new CheckBox("Use manual CV");
        addSetting(checkBox_ManualCV, "Disable YOLO computer vision, and instead manually enter the changes happening to the game via a console.");

        checkBox_PredefinedStock = new CheckBox("Used predefined stock");
        checkBox_PredefinedStock.disableProperty().bind(checkBox_ManualCV.selectedProperty());
        addSetting(checkBox_PredefinedStock, "Use a predefined stock, and skip the traversing of the stock in the beginning of the game. The stock used is defined in the code.");

        Button button = new Button("Start");
        VBox.setMargin(button, new Insets(10, 0,0,0));
        button.setPadding(new Insets(5,15,5,15));
        button.setOnAction((event) -> start());
        button.setOnKeyPressed(event -> {
            if( event.getCode() == KeyCode.ENTER )
                start();
        });

        container.getChildren().add(button);

        button.requestFocus();
    }


    private void addSetting(Node node, String descriptionText){
        VBox.setMargin(node, new Insets(10, 0, 2, 0));

        Text description = new Text();
        description.setFont(Font.font("verdana", FontPosture.ITALIC, 12));
        description.setText(descriptionText);
        description.setFont(new Font(12));
        description.setFill(Color.color(0.4,0.4,0.4));
        description.setWrappingWidth(getWidth()*0.80);
        description.setTextAlignment(TextAlignment.CENTER);
        VBox.setMargin(description, new Insets(0, 0, 10, 0));

        container.getChildren().addAll(node, description);
    }



    private void start(){
        SceneController.get().setScene(new GameScene(
                checkBox_ManualAI.isSelected(),
                checkBox_ManualCV.isSelected(),
                checkBox_ManualCV.isSelected() || checkBox_PredefinedStock.isSelected()
        ));
    }
}
