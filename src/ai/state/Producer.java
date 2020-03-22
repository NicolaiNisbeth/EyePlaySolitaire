package ai.state;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Producer {

    public static Stock produceStock(Stock stock, Consumer<Stock> consumer){
        Card[] cardsCopyDeep = Arrays
                .stream(stock.getCards())
                .map(card -> Deck.getCard(card.getValue(), card.getSuit()))
                .toArray(Card[]::new);

        Stock stockProduced = new Stock(cardsCopyDeep);
        consumer.accept(stockProduced);
        return stockProduced;
    }

    public static Tableau produceTableau(Tableau tableau, Consumer<Tableau> consumer){
        // TODO: is deep copy of every stack necessary?
        //  if not how do we know which one to copy?
        Card[][] cardsCopyDeep = tableau
                .getStacks()
                .stream()
                .map(stack -> stack
                        .stream()
                        .map(card -> Deck.getCard(card.getValue(), card.getSuit()))
                        .toArray(Card[]::new))
                .toArray(Card[][]::new);

        Tableau tableauProduced = new Tableau(cardsCopyDeep);
        consumer.accept(tableauProduced);
        return tableauProduced;
    }

    public static Foundation produceFoundation(Foundation foundation, Consumer<Foundation> consumer){
        // TODO: is deep copy of every stack necessary?
        //  if not how do we know which one to copy?
        List<Stack<Card>> cardsCopyDeep = foundation
                .getStacks()
                .stream()
                .map(stack -> stack
                        .stream()
                        .map(card -> Deck.getCard(card.getValue(), card.getSuit()))
                        .collect(Collectors.toCollection(Stack::new)))
                .collect(Collectors.toList());

        Foundation foundationProduced = new Foundation();
        foundationProduced.setStacks(cardsCopyDeep);
        consumer.accept(foundationProduced);
        return foundationProduced;
    }
}