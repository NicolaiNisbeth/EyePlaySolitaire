package gui.gamescene;

import javafx.scene.paint.Paint;

public interface IConsole {

    void print(String msg, Paint color, boolean bold);
    void printError(String msg);
    void printInfo(String msg);
    void registerInputCommand(String input, InputCommand action);


    interface InputCommand {
        void run();
    }

}
