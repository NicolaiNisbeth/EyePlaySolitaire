package ai;

import ai.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State {

    //Bunken
    Stockpile stockpile;

    //Byggestabler
    CardStack[] cardStacks;

    //Grundbunker
    Stack<Card>[] foundations;


    public List<Action> getActions(){
        return null;
    }


}
