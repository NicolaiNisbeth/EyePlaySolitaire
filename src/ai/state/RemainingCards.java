package ai.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RemainingCards implements Iterable<Card> {

    private Set<Card> remainingCards;

    public RemainingCards(Set<Card> remainingCards){
        this.remainingCards = remainingCards;
    }

    public RemainingCards copy(){
        return new RemainingCards(new HashSet<>(remainingCards));
    }

    public void removeCard(Card toRemove){
        remainingCards.remove(toRemove);
    }

    @Override
    public Iterator<Card> iterator() {
        return remainingCards.iterator();
    }
}
