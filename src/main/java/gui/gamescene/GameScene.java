package gui.gamescene;

import ai.action.Action;
import ai.agent.ExpectimaxAgent;
import ai.demo.DemoDeck;
import ai.demo.SolitaireAI;
import ai.heuristic.Heuristic;
import ai.heuristic.OptionsKnowledgeFoundation;
import ai.state.Card;
import ai.state.Deck;
import ai.state.Foundation;
import ai.state.Producer;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import cv_test.Camera;
import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.gamecomponent.GameComponent;
import gui.gamescene.gamecomponent.IGameComponent;
import gui.gamescene.gamestate.GameState;
import gui.gamescene.gamestate.GameStateGenerator;
import gui.gamescene.gamestate.UICard;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


public class GameScene extends Scene implements ConsoleComponent.InputListener {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 720;

    private GridPane grid;
    private ConsoleComponent consoleComponent;
    private CameraComponent cameraComponent;
    private IGameComponent gameComponent;
    private IGamePrompter prompter;
    private final ISolitaireAI ai = new SolitaireAI();
    private Camera camera;


    public GameScene() {
        super(new GridPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        grid = (GridPane) getRoot();

        // Setup Row constraints
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(40);
        grid.getRowConstraints().add(row1);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(35);
        grid.getColumnConstraints().add(column1);


        // Add Console component
        consoleComponent = new ConsoleComponent(this);
        grid.add(consoleComponent.getNode(), 0,1 );
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);

        prompter = consoleComponent;


        // Add Game Component
        gameComponent = new GameComponent();
        Node gameNode = gameComponent.getNode();
        grid.add(gameNode, 1, 0, 1, 2);
        GridPane.setHgrow(gameNode, Priority.ALWAYS);
        GridPane.setVgrow(gameNode, Priority.ALWAYS);


        // Add Camera Component
        cameraComponent = new CameraComponent();
        Node cameraNode = cameraComponent.getNode();
        grid.add(cameraNode, 0,0 );
        GridPane.setHgrow(cameraNode, Priority.ALWAYS);
        GridPane.setVgrow(cameraNode, Priority.ALWAYS);


        // Just a temporary image
        Image image = new Image("images/solitaire_irl.jpg", false);
        cameraComponent.updateImage(image);


        // TODO: Remove this once CV has been implemented
        // Display a randomized Game State
        GameState state = GameStateGenerator.generateGameState(1000);
        gameComponent.updateGameState(state);

        // TODO: Remove this once CV has been implemented
        // Starts a Thread for testing AI
        new Thread(this::aiTest).start();


        // TODO: Remove this when camera is implemented corretly
        /*
        // Testing camera
        camera = new Camera();
        camera.startCamera( (img) -> {
            cameraComponent.updateImage(img);
        });
        */
    }


    @Override
    public void onConsoleInput(String input) {
        System.out.println("Input from console: " + input);

        // Generates a random state of cards and updates the game component
        if( input.equals("newstate")) {
            GameState state = GameStateGenerator.generateGameState(System.currentTimeMillis());
            gameComponent.updateGameState(state);
        }
    }


    /**
     * Sends an asynchronous computation request to the AI on a new non-UI thread.
     */
    public void computeNextAction(GameState state){
        new Thread(() -> {
            // TODO: Implement this with better error message system in the GUI
            try{
                ai.computeAction(state, prompter);
            }catch(ISolitaireAI.IllegalGameStateException e){
                e.printStackTrace();
                System.out.println(state);
            }
        }).start();
    }



    private void aiTest(){
        // TODO: JD og Nicolai implementer jeres AI shit her
        Heuristic heuristic = new OptionsKnowledgeFoundation(1, 0, 1);
        //MiniMaxAgent agent = new MiniMaxAgent(3, heuristic);

        ExpectimaxAgent agent = new ExpectimaxAgent(3, heuristic);
        //Agent agent = new RandomAgent();
        int sum = 0;
        int max = 0;
        int wins = 0;
        int iterations = 1;
        for (int i = 0; i < iterations; i++) {
            State state = generateInitialState();
            HashSet<Action> repetitions = new HashSet<>();
            while(true){
                Action action = agent.getAction(state);
                if(repetitions.contains(action)) break;
                repetitions.add(action);
                if(action == null) break;
                state = getRandom(action.getResults(state));
                final GameState gameState = convertState(state);
                final int count = state.getStock().showOptions().size();
                Platform.runLater(() -> {
                    System.out.println("Available cards from stock count: " + count);
                    gameComponent = new GameComponent();
                    Node gameNode = gameComponent.getNode();
                    grid.add(gameNode, 1, 0, 1, 2);
                    GridPane.setHgrow(gameNode, Priority.ALWAYS);
                    GridPane.setVgrow(gameNode, Priority.ALWAYS);
                    gameComponent.updateGameState(gameState);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        System.out.println("Leaf nodes " + agent.getCounter());
        System.out.println(String.format("Wins %d\nMax %d\nAverage %f", wins, max, (double)sum/iterations));

        /*
        Brug denne her til at opdatere spillet i GUI'en
        Platform.runLater(() -> {
            gameComponent.updateState(state);
        });*/


    }

    private static GameState convertState(State state){

        Stock stock = Producer.produceStock(state.getStock(), lol -> {});
        List<UICard> stockList = new ArrayList<>();
        for(Card card : stock.getCards())
            stockList.add(convertCard(card));

        Foundation foundation = Producer.produceFoundation(state.getFoundation(), lol -> {});
        List<List<UICard>> foundationList = new ArrayList<>();
        for(Stack<Card> stack : foundation.getStacks()){
            List<UICard> convertedStack = new ArrayList<>();
            List<UICard> temp = new ArrayList<>();
            while(!stack.isEmpty()){
                temp.add(convertCard(stack.pop()));
            }
            for (int i = temp.size()-1; i >= 0; i--) {
                convertedStack.add(temp.get(i));
            }
            foundationList.add(convertedStack);
        }

        Tableau tableau = Producer.produceTableau(state.getTableau(), lol -> {});
        List<List<UICard>> tableauList = new ArrayList<>();
        for(Stack<Card> stack : tableau.getStacks()){
            List<UICard> convertedStack = new ArrayList<>();
            List<UICard> temp = new ArrayList<>();
            while(!stack.isEmpty()){
                temp.add(convertCard(stack.pop()));
            }
            for (int i = temp.size()-1; i >= 0; i--) {
                convertedStack.add(temp.get(i));
            }
            tableauList.add(convertedStack);
        }

        List<UICard> flipped = new ArrayList<>();



        return new GameState(stockList, flipped, tableauList, foundationList);
    }

    private static UICard convertCard(Card card){
        UICard.Suit suit;
        if(card == null)
            return new UICard(UICard.Suit.UNKNOWN, 2);
        switch(card.getSuit()){
            case 0: suit = UICard.Suit.CLUBS;
                break;
            case 1: suit = UICard.Suit.DIAMONDS;
                break;
            case 2: suit = UICard.Suit.HEARTS;
                break;
            case 3: suit = UICard.Suit.SPADES;
                break;
            default: suit = UICard.Suit.UNKNOWN;
        }
        return new UICard(suit, card.getValue());
    }


    private static State generateInitialState() {
        DemoDeck deck = new DemoDeck();

        ai.state.Card[][] board = createBoard(deck);
        Tableau tableau = new Tableau(board);

        ai.state.Card[] stockCards = new ai.state.Card[24];
        for (int i = 0; i < stockCards.length; i++)
            stockCards[i] = deck.draw();
        Stock stock = new Stock(stockCards);

        Foundation foundation = new Foundation();
        Set<ai.state.Card> cards = new HashSet<>();
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

    private static ai.state.Card[][] createBoard(DemoDeck deck) {
        ai.state.Card[][] board = new ai.state.Card[7][];
        board[0] = new ai.state.Card[1];
        board[1] = new ai.state.Card[2];
        board[2] = new ai.state.Card[3];
        board[3] = new ai.state.Card[4];
        board[4] = new ai.state.Card[5];
        board[5] = new ai.state.Card[6];
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