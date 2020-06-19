package gui.gamescene;

import ai.demo.SolitaireAI;
import cv.SolitaireCV;
import gui.gamescene.aiinterface.IGamePrompter;
import gui.gamescene.aiinterface.ISolitaireAI;
import gui.gamescene.cameracomponent.CameraComponent;
import gui.gamescene.consolecomponent.ConsoleComponent;
import gui.gamescene.cvinterface.ISolitaireCV;
import gui.gamescene.gamecomponent.GameComponent;
import gui.gamescene.gamecomponent.IGameComponent;
import gui.gamescene.gamestate.GameState;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.io.Console;


public class GameScene extends Scene {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 720;

    private GridPane grid;
    private IConsole console;
    private CameraComponent cameraComponent;
    private IGameComponent gameComponent;
    private IGamePrompter prompter;
    private final ISolitaireAI ai = new SolitaireAI();
    private final ISolitaireCV cv = new SolitaireCV();

    private GameController gameController;


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
        ConsoleComponent consoleComponent = new ConsoleComponent();
        grid.add(consoleComponent.getNode(), 0,1 );
        GridPane.setHgrow(consoleComponent.getNode(), Priority.ALWAYS);
        GridPane.setVgrow(consoleComponent.getNode(), Priority.ALWAYS);
        console = consoleComponent;
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

        cameraComponent.startLoading("Starting computer vision...");

        // Start Computer Vision
        cv.setImageUpdateListener((newImage) -> cameraComponent.updateImage(newImage));
        cv.setGameStateUpdateListener((newState -> gameComponent.updateGameState(newState) ));
        cv.setFinishedCallback(err -> {
            cameraComponent.showError("Computer vision client has been stopped!");
        });
        cv.start();

        // TODO: Remove this once CV has been implemented
        // Display a randomized Game State
      /*  GameState state = GameStateGenerator.generateGameState(1000);
        gameComponent.updateGameState(state);*/

        // TODO: Remove this once CV has been implemented
        // Starts a Thread for testing AI
        // new Thread(this::aiTest).start();


        gameController = new GameController(this);
    }


    public IConsole getConsole() {
        return console;
    }

    public IGameComponent getGameComponent() {
        return gameComponent;
    }


    public CameraComponent getCameraComponent() {
        return cameraComponent;
    }

    public ISolitaireAI getAi() {
        return ai;
    }

    public ISolitaireCV getCv() {
        return cv;
    }

    public IGamePrompter getPrompter() {
        return prompter;
    }

    /*
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

        *//*
        Brug denne her til at opdatere spillet i GUI'en
        Platform.runLater(() -> {
            gameComponent.updateState(state);
        });*//*


    }

    private static GameState convertState(State state){

        Stock stock = Producer.produceStock(state.getStock(), lol -> {});
        List<Card> stockList = new ArrayList<>();
        for(ai.state.Card card : stock.getCards())
            stockList.add(convertCard(card));

        Foundation foundation = Producer.produceFoundation(state.getFoundation(), lol -> {});
        List<List<Card>> foundationList = new ArrayList<>();
        for(Stack<ai.state.Card> stack : foundation.getStacks()){
            List<Card> convertedStack = new ArrayList<>();
            List<Card> temp = new ArrayList<>();
            while(!stack.isEmpty()){
                temp.add(convertCard(stack.pop()));
            }
            for (int i = temp.size()-1; i >= 0; i--) {
                convertedStack.add(temp.get(i));
            }
            foundationList.add(convertedStack);
        }

        Tableau tableau = Producer.produceTableau(state.getTableau(), lol -> {});
        List<List<Card>> tableauList = new ArrayList<>();
        for(Stack<ai.state.Card> stack : tableau.getStacks()){
            List<Card> convertedStack = new ArrayList<>();
            List<Card> temp = new ArrayList<>();
            while(!stack.isEmpty()){
                temp.add(convertCard(stack.pop()));
            }
            for (int i = temp.size()-1; i >= 0; i--) {
                convertedStack.add(temp.get(i));
            }
            tableauList.add(convertedStack);
        }

        List<Card> flipped = new ArrayList<>();



        return new GameState(stockList, flipped, tableauList, foundationList);
    }

    private static Card convertCard(ai.state.Card card){
        Card.Suit suit;
        if(card == null)
            return new Card(Card.Suit.UNKNOWN, 2);
        switch(card.getSuit()){
            case 0: suit = Card.Suit.CLUBS;
                break;
            case 1: suit = Card.Suit.DIAMONDS;
                break;
            case 2: suit = Card.Suit.HEARTS;
                break;
            case 3: suit = Card.Suit.SPADES;
                break;
            default: suit = Card.Suit.UNKNOWN;
        }
        return new Card(suit, card.getValue());
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
        board[6] = new ai.state.Card[7];

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

    @Override
    public void onConsoleInput(String input) {

    }*/
}
