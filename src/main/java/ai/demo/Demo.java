package ai.demo;

import ai.action.*;
import ai.agent.*;
import ai.heuristic.Cocktail;
import ai.heuristic.Heuristic;
import ai.heuristic.OptionsKnowledgeFoundation;
import ai.state.Card;
import ai.state.Foundation;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Demo {

    public static void main(String[] args) {
        //Heuristic heuristic = new OptionsKnowledgeFoundation(1, 0, 1);
        //MiniMaxAgent agent = new MiniMaxAgent(3, heuristic);




        //Agent agent = new RandomAgent();
        List<int[]> memory = new ArrayList<>();
        int sum = 0;
        int max = 0;
        int wins = 0;
        int iterations = 200;
        for (int i = 0; i < iterations; i++) {
            Heuristic heuristic = new Cocktail(1,1,1,1,1,1,1,1,1);
            MCTSAgent agent = new MCTSAgent(5000, heuristic);
            int counter = 0;
            State state = generateInitialState();
            while(true){
                Action action = agent.getAction(state);
                if(action == null)  break;
                state = getRandom(action.getResults(state));
                if(!validState(state)) System.out.println("aaaa");
                tracker(action, counter++, memory);

                //System.out.println(action);
            }
            int foundationCount = state.getFoundation().getCount();
            if (foundationCount == 52)
                wins++;
            if(foundationCount > max){
                max = foundationCount;
            }
            sum += foundationCount;
            System.out.println(i + "\t" + (foundationCount == 52 ? "W" : ""));
        }
        //System.out.println("Leaf nodes " + agent.getCounter());
        System.out.println(String.format("Wins %d\nMax %d\nAverage %f", wins, max, (double)sum/iterations));
        saveActionsToFile(memory);
    }

    private static void saveActionsToFile(List<int[]> memory) {
        Path path = Paths.get("output.txt");
        String[] possibilities = {"StockToFoundation", "StockToTableau", "TableauToFoundation", "TableauToTableau"};
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (int i = 0; i < possibilities.length; i++) {
                writer.write(possibilities[i] + " ");
            }
            writer.write("\n");
            for (int i=0; i<memory.size(); i++){
                int[] actions = memory.get(i);
                for (Integer count : actions){
                    writer.write(count + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tracker(Action action, int counter, List<int[]> memory) {
        int index = -1;
        if (action instanceof StockToFoundation){
            index = 0;
        } else if (action instanceof StockToTableau){
            index = 1;
        } else if (action instanceof TableauToFoundation){
            index = 2;
        } else if (action instanceof TableauToTableau){
            index = 3;
        }
        if(memory.size() == counter){
            memory.add(new int[4]);
        }
        memory.get(counter)[index]++;
    }

    public static boolean validState(State state) {
        Tableau tableau = state.getTableau();
        for (int i = 0; i < tableau.getStacks().size(); i++) {
            Stack<Card> stack = tableau.getStacks().get(i);
            for (int j = 1; j < stack.size(); j++) {
                Card card = stack.get(j);
                Card previous = stack.get(j-1);
                if(card != null && previous != null) {
                    if(card.getValue() != previous.getValue() - 1)
                        return false;
                }
            }
        }
        return true;
    }

    private static void search(int size, List<Integer> permutation, List<List<Integer>> permutations){
        if(permutation.size() == size){
            permutations.add(permutation);
        } else {
            for (int i = 0; i < size; i++) {
                if(permutation.contains(i)) continue;
                permutation.add(i);
                search(size, permutation, permutations);
                permutation.remove(i);
            }
        }
    }

    public static State generateInitialState() {
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
