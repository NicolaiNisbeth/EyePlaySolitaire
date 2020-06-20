package gui.gamescene.contolcomponent;

import gui.gamescene.IComponent;
import gui.util.Loader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ControlComponent extends VBox implements IComponent {

    private Button button_stopCompute, button_detect, button_compute;
    private Loader loader;


    private ControlListener
            listener_detectionStarted,
            listener_computeStarted,
            listener_computationStopped;

    private Text text_prompt, text_description;


    public ControlComponent() {
        setAlignment(Pos.CENTER);

        text_prompt = new Text();
        text_prompt.setFont(new Font(20));
        text_prompt.setTextAlignment(TextAlignment.CENTER);
        text_prompt.wrappingWidthProperty().bind(widthProperty().multiply(0.70));
        text_prompt.setVisible(false);
        text_prompt.managedProperty().bind(text_prompt.visibleProperty());
        getChildren().add(text_prompt);

        loader = new Loader(50, 50, 20);
        loader.setVisible(false);
        loader.managedProperty().bind(loader.visibleProperty());
        getChildren().add(loader);

        text_description = new Text();
        text_description.setFont(new Font(14));
        text_description.setWrappingWidth(getWidth()*0.80);
        text_description.setTextAlignment(TextAlignment.CENTER);
        text_description.managedProperty().bind(text_description.visibleProperty());
        text_description.setVisible(false);
        getChildren().add(text_description);

        button_detect = new Button("Detect state");
        button_detect.setPadding(new Insets(5));
        button_detect.setOnAction(e -> startDetection());
        button_detect.setDefaultButton(true);
        button_detect.setMinWidth( 150 );
        button_detect.setMinHeight( 30 );
        button_detect.managedProperty().bind(button_detect.visibleProperty());
        VBox.setMargin(button_detect, new Insets(30, 0, 0 ,0));
        getChildren().add(button_detect);

        button_compute = new Button("Compute action");
        button_compute.setPadding(new Insets(5));
        button_compute.setOnAction(e -> startComputation());
        button_compute.setVisible(false);
        button_compute.setMinWidth( 150 );
        button_compute.setMinHeight( 30 );
        button_compute.managedProperty().bind(button_compute.visibleProperty());
        VBox.setMargin(button_compute, new Insets(30, 0, 0,0));
        getChildren().add(button_compute);


        button_stopCompute = new Button("Stop");
        button_stopCompute.setPadding(new Insets(5));
        button_stopCompute.setVisible(false);
        button_stopCompute.setOnAction(e -> stopComputation());
        button_stopCompute.setMinWidth( 150 );
        button_stopCompute.setMinHeight( 30 );
        button_stopCompute.managedProperty().bind(button_stopCompute.visibleProperty());
        VBox.setMargin(button_stopCompute, new Insets(30, 0, 0, 0));
        getChildren().add(button_stopCompute);
    }


    private void startDetection(){
        text_prompt.setVisible(false);
        button_detect.setVisible(false);
        button_detect.setDefaultButton(false);
        button_compute.setVisible(true);
        button_compute.setDefaultButton(true);
        loader.setVisible(true);
        loader.setLoadingText("Detecting game state");
        text_description.setVisible(true);
        text_description.setText("Place your cards and make sure they match the displayed game state.");
        listener_detectionStarted.onControlAction();
    }


    private void startComputation(){
        button_compute.setVisible(false);
        button_compute.setDefaultButton(false);
        button_stopCompute.setVisible(true);
        button_stopCompute.setDefaultButton(true);
        button_stopCompute.setDisable(false);
        loader.setLoadingText("Computing best action");
        text_description.setText("Figuring out which actions is best to take. The longer you let it run, the better the result will be.");
        listener_computeStarted.onControlAction();
    }


    private void stopComputation(){
        button_stopCompute.setDisable(true);
        loader.setLoadingText("Loading best action");
        text_description.setVisible(false);
        listener_computationStopped.onControlAction();
    }


    public void onDetectStarted(ControlListener listener){
        listener_detectionStarted = listener;
    }

    public void onComputeStarted(ControlListener listener){
        listener_computeStarted = listener;
    }

    public void onComputeStopped(ControlListener listener){
        listener_computationStopped = listener;
    }

    public void promptAction(String prompt){
        loader.setVisible(false);
        text_description.setVisible(false);
        button_stopCompute.setDefaultButton(false);
        button_stopCompute.setVisible(false);
        text_prompt.setText(prompt);
        text_prompt.setVisible(true);
        button_detect.setVisible(true);
        button_detect.setDefaultButton(true);
    }

    @Override
    public Node getNode() {
        return this;
    }

    public interface ControlListener{
        void onControlAction();
    }

}


// Primary scene -> Detect, Compute

// Detecting (loading spinner, text, stop)

// Compute (loading spinner, text, stop)

// Prompt action
