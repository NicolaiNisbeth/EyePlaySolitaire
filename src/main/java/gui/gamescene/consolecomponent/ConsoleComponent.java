package gui.gamescene.consolecomponent;

import gui.gamescene.IComponent;
import gui.gamescene.consolecomponent.ScrollableTextField;
import javafx.scene.Node;


public class ConsoleComponent implements IComponent {

    private InputListener inputListener = null;

    private ScrollableTextField output = new ScrollableTextField();


    public ConsoleComponent(){

    }

    public void print(String msg){
        output.appendText(msg);
    }


    public void setInputListener(InputListener inputListener){
        this.inputListener = inputListener;
    }


    @Override
    public Node getNode() {
        return output;
    }


    interface InputListener{
        void onInput(String input);
    }

}
