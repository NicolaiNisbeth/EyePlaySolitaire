package gui.gamescene.gamecomponent;

import gui.util.MarginContainer;



public class CardContainer extends MarginContainer {
    public CardContainer( double margin ){
        setMargin(margin);
    }

    public void setCard(CardPane card) {
        setNode(card);
    }

    public void clearCard(){
        removeNode();
    }
}
