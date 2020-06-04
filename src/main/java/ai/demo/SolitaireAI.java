package ai.demo;

import ai.action.Action;
import ai.agent.ExpectimaxAgent;
import ai.heuristic.Heuristic;
import ai.heuristic.OptionsKnowledgeFoundation;
import ai.state.Deck;
import ai.state.Foundation;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.gamestate.UICard;
import gui.gamescene.gamestate.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SolitaireAI implements ISolitaireAI {
    private static final Heuristic heuristic = new OptionsKnowledgeFoundation(1, 0, 1);
    private static final ExpectimaxAgent agent = new ExpectimaxAgent(3, heuristic);
    private static final Set<ai.state.Card> deck = Deck.cardSet; // used in flipped to remaining cards conversion

    @Override
    public void computeAction(GameState gameState, IGamePrompter prompter) throws IllegalGameStateException {
        State state = stateConverter(gameState);
        Action action = agent.getAction(state);
        if (action == null) prompter.gameLost();
        else action.prompt(prompter, state);
    }

    // TODO: plenty of room for optimization in stateConverters
    /**
     * Converts UI gameState to AI gameState
     * @param uiGameState
     * @return AI gameState
     */
    private State stateConverter(GameState uiGameState) {
        Stock stock = stockConverter(uiGameState.getStock());
        Tableau tableau = tableauConverter(uiGameState.getTableaus());
        Foundation foundation = foundationConverter(uiGameState.getFoundations());
        RemainingCards remainingCards = remainingCardsConverter(uiGameState.getFlipped());
        return new State(stock, tableau, foundation, remainingCards);
    }

    /**
     *
     * @param uiFlippedUICards
     * @return
     */
    private RemainingCards remainingCardsConverter(List<UICard> uiFlippedUICards) {
        for (UICard flippedUICard : uiFlippedUICards){
            deck.remove(cardConverter(flippedUICard));
        }
        return new RemainingCards(deck);
    }

    /**
     * Converts UI tableau to AI tableau
     * @param uiTableau
     * @return AI tableau
     */
    private Tableau tableauConverter(List<List<UICard>> uiTableau) {
        int size = uiTableau.size();
        ai.state.Card[][] cards = new ai.state.Card[size][];
        for (int i = 0; i < size; i++) {
            List<UICard> stack = uiTableau.get(i);
            ai.state.Card[] cc = new ai.state.Card[stack.size()];
            for (int j = 0; j < stack.size(); j++){
                UICard c = stack.get(j);
                cc[j] = cardConverter(c);
            }
            cards[i] = cc;
        }
        return new Tableau(cards);
    }

    /**
     * Converts UI foundation to AI foundation
     * @param uiFoundation
     * @return AI foundation
     */
    private Foundation foundationConverter(List<List<UICard>> uiFoundation) {
        List<Stack<ai.state.Card>> stacks = new ArrayList<>();
        for (List<UICard> f : uiFoundation){
            Stack<ai.state.Card> stack = new Stack<>();
            for (UICard c : f){
                stack.add(cardConverter(c));
            }
            stacks.add(stack);
        }
        return new Foundation(stacks);
    }

    /**
     * Converts UI stock to AI stock
     * @param uiStock
     * @return AI stock
     */
    private Stock stockConverter(List<UICard> uiStock) {
        int size = uiStock.size();
        ai.state.Card[] cards = new ai.state.Card[size];
        for (int i = 0; i < size; i++){
            UICard c = uiStock.get(i);
            cards[i] = cardConverter(c);
        }
        return new Stock(cards);
    }

    /**
     * Converts UI card to AI card
     * @param aiUICard
     * @return AI card
     */
    private ai.state.Card cardConverter(UICard aiUICard) {
        int value = aiUICard.getValue();
        int suit = aiUICard.getSuit().ordinal()-1;
        return suit >= 0  ? new ai.state.Card(value, suit) : null;
    }
}
