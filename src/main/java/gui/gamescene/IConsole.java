package gui.gamescene;

public interface IConsole {

    void printError(String msg);
    void printInfo(String msg);
    void registerInputCommand(String input, InputCommand action);


    interface InputCommand {
        void run();
    }

}
