package ai.heuristic;

import ai.state.Card;
import ai.state.State;
import ai.state.Tableau;

import java.util.Stack;

public class TableauDelta implements Heuristic {

    @Override
    public double evaluate(State state) {
        Tableau tableau = state.getTableau();
        double sum = 0;
        double cardCount = 0;
        for (Stack<Card> stack : tableau.getStacks()) {
            for (Card card : stack) {
                if(card == null) continue;
                cardCount++;
                for (Stack<Card> alsoStack : tableau.getStacks()){
                    for(Card otherCard : alsoStack){
                        if(otherCard == null) continue;
                        sum += distance(card, otherCard);
                    }
                }
            }
        }
        cardCount = cardCount == 0 ? 1 : cardCount;
        return sum / cardCount;
    }


    // 1 1  &  0 0 ,    1 0    0 1
    private double distance(Card card, Card otherCard) {
        boolean cardGroup = card.getValue() % 2 == card.getColour() % 2;
        boolean otherCardGroup = otherCard.getValue() % 2 == otherCard.getColour() % 2;
        if(cardGroup != otherCardGroup) return 13;
        return Math.abs(card.getValue() - otherCard.getValue());
    }

}
