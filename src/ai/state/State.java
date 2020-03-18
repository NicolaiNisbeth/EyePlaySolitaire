package ai.state;

import ai.action.Action;
import ai.action.PlayStockpileCardToCardPiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class State {


    //Bunken
    private Stockpile stockpile;

    //Byggestabler
    private CardPile[] cardPiles;

    //Grundbunker
    private ArrayList<Stack<Card>> foundations;

    public State(Stockpile stockpile, CardPile[] cardPiles, ArrayList<Stack<Card>> foundations){
        this.stockpile = stockpile;
        this.cardPiles = cardPiles;
        this.foundations = foundations;
    }

    public List<Action> getActions() {
        List<Action> actions = new ArrayList<>();
        List<Integer> stockpilePlayableIndexes = findStockpilePlayableIndexes();
        addPlayStockpileCardToCardPilesActions(actions, stockpilePlayableIndexes);
        addPlayStockpileCardToFoundationsActions(actions, stockpilePlayableIndexes);
        return actions;
    }

    private List<Integer> findStockpilePlayableIndexes() {
        List<Integer> indices = new ArrayList<>();
        int index = stockpile.getHead();
        int size = stockpile.getCards().length;
        while(!indices.contains(index)){
            indices.add(index);
            index = (index + 3) % size;
        }
        return indices;
    }

    private void addPlayStockpileCardToCardPilesActions(List<Action> actions, List<Integer> stockpilePlayableIndexes) {
        for (Integer integer : stockpilePlayableIndexes) {
            Card card = stockpile.getCards()[integer];
            for (int i = 0; i < cardPiles.length; i++) {
                CardPile cardPile = cardPiles[i];
                Card first = cardPile.getFirstVisible().card;
                boolean suitFits = ((card.getSuit() == Card.DIAMOND || card.getSuit() == Card.HEART)
                        && (first.getSuit() == Card.CLUB || first.getSuit() == Card.SPADE))
                        || ((card.getSuit() == Card.CLUB || card.getSuit() == Card.SPADE)
                        && (first.getSuit() == Card.DIAMOND || first.getSuit() == Card.HEART));
                if(suitFits && first.getValue() == card.getValue() + 1){
                    actions.add(new PlayStockpileCardToCardPiles(integer, i));
                }
            }
        }
    }

    private void addPlayStockpileCardToFoundationsActions(List<Action> actions, List<Integer> stockpilePlayableIndexes) {

    }


    public Stockpile getStockpile() {
        return stockpile;
    }

    public CardPile[] getCardPiles() {
        return cardPiles;
    }

    public CardPile[] shallowCopyCardPiles() {
        return Arrays.copyOf(cardPiles, cardPiles.length);
    }

    public ArrayList<Stack<Card>> getFoundations() {
        return foundations;
    }

    public ArrayList<Stack<Card>> shallowCopyFoundations() {
        return new ArrayList<>(foundations);
    }

    @Override
    public String toString() {
        return "State{" + "\n" +
                "stockpile=" + stockpile + "\n" +
                ", cardPiles=" + Arrays.toString(cardPiles) + "\n" +
                ", foundations=" + foundations + "\n" +
                '}';
    }
}
