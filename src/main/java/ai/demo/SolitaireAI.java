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
import gui.gamescene.aiinterface.IActionPrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.gamestate.Card;
import gui.gamescene.gamestate.GameState;

import java.util.*;

public class SolitaireAI implements ISolitaireAI {
    private static final Heuristic heuristic = new OptionsKnowledgeFoundation(1, 0, 1);
    private static final ExpectimaxAgent agent = new ExpectimaxAgent(3, heuristic);
    private static final Set<ai.state.Card> deck = Deck.cardSet; // used in flipped to remaining cards conversion

    @Override
    public void computeAction(GameState gameState, IActionPrompter prompter) throws IllegalGameStateException {
        State state = stateConverter(gameState);
        Action action = agent.getAction(state);
        if (action == null) prompter.gameLost();
        else action.prompt(prompter, state);
    }

    @Override
    public void startActionComputation(GameState gameState) throws IllegalGameStateException {
        agent.getAction(stateConverter(gameState));
    }

    @Override
    public void endActionComputation(IActionPrompter prompter) {

    }

    private Stock previous = null;

    // TODO: plenty of room for optimization in stateConverters
    /**
     * Converts UI gameState to AI gameState
     * @param uiGameState
     * @return AI gameState
     */
    public State stateConverter(GameState uiGameState) {
        Tableau tableau = tableauConverter(uiGameState.getTableaus());
        Foundation foundation = foundationConverter(uiGameState.getFoundations());
        RemainingCards remainingCards = remainingCardsConverter(uiGameState);
        Stock stock = previous == null ? stockConverter(uiGameState.getStock()) : previous;

        Set<ai.state.Card> tableauOrFoundationCards = new HashSet<>();
        for (Stack<ai.state.Card> stack : tableau.getStacks()) {
            for(ai.state.Card card : stack) {
                if(card != null){
                    tableauOrFoundationCards.add(card);
                }
            }
        }
        for (Stack<ai.state.Card> stack : foundation.getStacks()) {
            for(ai.state.Card card : stack) {
                if(card != null){
                    tableauOrFoundationCards.add(card);
                }
            }
        }
        for (int i = 0; i < stock.getCards().length; i++) {
            ai.state.Card card = stock.getCards()[i];
            if(tableauOrFoundationCards.contains(card)){
                stock.removeCard(card);
                break;
            }
        }

        return new State(stock, tableau, foundation, remainingCards);
    }

    /**
     *
     * @param uiGameState
     * @return
     */
    private static RemainingCards remainingCardsConverter(GameState uiGameState) {

        for( int i=0; i<7; i++){
            List<Card> tableau = uiGameState.getTableaus().get(i);
            for(int j = 0; j < tableau.size(); j++){
                if(!tableau.get(j).isUnknown()){
                    deck.remove(cardConverter(tableau.get(j)));
                }
            }
        }

        for( int i=0; i<4; i++){
            List<Card> foundations = uiGameState.getFoundations().get(i);
            for(int j = 0; j < foundations.size(); j++){
                deck.remove(cardConverter(foundations.get(j)));
            }
        }

        for (Card stockCards : uiGameState.getStock()){
            deck.remove(cardConverter(stockCards));
        }

        return new RemainingCards(deck);
    }

    /**
     * Converts UI tableau to AI tableau
     * @param uiTableau
     * @return AI tableau
     */
    private static Tableau tableauConverter(List<List<Card>> uiTableau) {
        int size = uiTableau.size();
        ai.state.Card[][] cards = new ai.state.Card[size][];
        for (int i = 0; i < size; i++) {
            List<Card> stack = uiTableau.get(i);
            ai.state.Card[] cc = new ai.state.Card[stack.size()];
            for (int j = 0; j < stack.size(); j++){
                Card c = stack.get(j);
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
    private static Foundation foundationConverter(List<List<Card>> uiFoundation) {
        List<Stack<ai.state.Card>> stacks = new ArrayList<>();
        for (List<Card> f : uiFoundation){
            Stack<ai.state.Card> stack = new Stack<>();
            for (Card c : f){
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
    private static Stock stockConverter(List<Card> uiStock) {
        int size = uiStock.size();
        ai.state.Card[] cards = new ai.state.Card[size];
        for (int i = 0; i < size; i++){
            Card c = uiStock.get(i);
            cards[i] = cardConverter(c);
        }
        return new Stock(cards);
    }

    /**
     * Converts UI card to AI card
     * @param aiCard
     * @return AI card
     */
    private static ai.state.Card cardConverter(Card aiCard) {
        int value = aiCard.getValue();
        int suit = aiCard.getSuit().ordinal()-1;
        return suit >= 0  ? new ai.state.Card(value, suit) : null;
    }
}
