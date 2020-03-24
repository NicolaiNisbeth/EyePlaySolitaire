package ai.demo;

import ai.action.Action;
import ai.agent.Agent;
import ai.agent.ExpectimaxAgent;
import ai.agent.MCTSAgent;
import ai.agent.MiniMaxAgent;
import ai.agent.RandomAgent;
import ai.heuristic.Heuristic;
import ai.heuristic.OptionsKnowledgeFoundation;
import ai.state.Card;
import ai.state.Foundation;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {
        Heuristic heuristic = new OptionsKnowledgeFoundation(1, 1, 1);
        Agent agent = new MiniMaxAgent(1, heuristic);
        //Agent agent = new RandomAgent();
        int sum = 0;
        int max = 0;
        int wins = 0;
        int iterations = 1000;
        for (int i = 0; i < iterations; i++) {
            State state = generateInitialState();
            while(true){
                Action action = agent.getAction(state);
                //System.out.println(action);
                if(action == null) break;
                state = getRandom(action.getResults(state));
            }
            int foundationCount = state.getFoundation().getCount();
            if (foundationCount == 52)
                wins++;
            if(foundationCount > max){
                max = foundationCount;
            }
            sum += foundationCount;
        }
        System.out.println(String.format("Wins %d\nMax %d\nAverage %f", wins, max, (double)sum/iterations));
    }

    private static State generateInitialState() {
        DemoDeck deck = new DemoDeck();

        Card[][] board = createBoard(deck);
        Tableau tableau = new Tableau(board);

        Card[] stockCards = new Card[24];
        for (int i = 0; i < stockCards.length; i++)
            stockCards[i] = deck.draw();
        Stock stock = new Stock(stockCards);

        Foundation foundation = new Foundation();
        Set<Card> cards = new HashSet<>();
        for (int i = 0; i < 21; i++)
            cards.add(deck.draw());
        RemainingCards remainingCards = new RemainingCards(cards);

        return new State(stock, tableau, foundation, remainingCards);
    }

    private static State getRandom(Collection<State> collection) {
        return collection
                .stream()
                .skip((int)(Math.random() * collection.size()))
                .findFirst()
                .orElse(null);
    }

    private static Card[][] createBoard(DemoDeck deck) {
        Card[][] board = new Card[7][];
        board[0] = new Card[1];
        board[1] = new Card[2];
        board[2] = new Card[3];
        board[3] = new Card[4];
        board[4] = new Card[5];
        board[5] = new Card[6];
        board[6] = new Card[7];

        board[0][0] = deck.draw();
        board[1][0] = null;
        board[1][1] = deck.draw();
        board[2][0] = null;
        board[2][1] = null;
        board[2][2] = deck.draw();
        board[3][0] = null;
        board[3][1] = null;
        board[3][2] = null;
        board[3][3] = deck.draw();
        board[4][0] = null;
        board[4][1] = null;
        board[4][2] = null;
        board[4][3] = null;
        board[4][4] = deck.draw();
        board[5][0] = null;
        board[5][1] = null;
        board[5][2] = null;
        board[5][3] = null;
        board[5][4] = null;
        board[5][5] = deck.draw();
        board[6][0] = null;
        board[6][1] = null;
        board[6][2] = null;
        board[6][3] = null;
        board[6][4] = null;
        board[6][5] = null;
        board[6][6] = deck.draw();
        return board;
    }
}
