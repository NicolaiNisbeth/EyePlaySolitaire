package gui.gamescene.gamecomponent;


import gui.util.MarginContainer;
import javafx.scene.layout.StackPane;

public class CardStackContainer extends StackPane {

    private MarginContainer[] containers;
    private int cardCount = 0;
    private int size;

    public CardStackContainer(int size, double generalMargin, double stackMargin ) {

        this.size = size;
        containers = new MarginContainer[size];

        for( int i=0; i<size; i++ ){
            MarginContainer container = new MarginContainer();
            container.setMargin(generalMargin);
            container.setMarginTop(generalMargin + stackMargin*i);
            container.setMarginBottom(generalMargin + stackMargin*(size-1-i));
            containers[i] = container;
            getChildren().add(container);

        }
    }

    public void addCard(CardPane card){
        if( cardCount == size )
            throw new IndexOutOfBoundsException(String.format("Too many cards in container. Size is %d.",size));

        containers[cardCount].setNode(card);
        cardCount++;
    }

    public void clear(){
        for( MarginContainer container : containers ){
            container.removeNode();
        }
    }

}
