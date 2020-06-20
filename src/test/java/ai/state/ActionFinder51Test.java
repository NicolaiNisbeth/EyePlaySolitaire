package ai.state;

import ai.action.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionFinder51Test {

    private IActionFinder actionFinder;
    private List<Action> actions;

    @BeforeEach
    public void setup(){
        actionFinder = new ActionFinder52();
        actions = new ArrayList<>();
    }

    @AfterEach
    public void tearDown(){
        actionFinder = null;
        actions = null;
    }

    @Test
    @DisplayName("find stock card to empty foundation action")
    void stockEmptyFoundationAction() {
        Foundation foundation = new Foundation();
        List<Card> options = new ArrayList<>();
        for (int i = 0; i <= 3 ; i++) {
            options.add(new Card(1, i));
        }
        assertEquals(0, actions.size());
        actionFinder.addStockToFoundationActions(options, foundation, actions);
        assertEquals(4, actions.stream().filter(StockToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find stock card to one card foundation action")
    void stockOneCardFoundation(){
        Foundation foundation = new Foundation();
        for (int i = 0; i <= 3; i++)
            foundation.add(new Card(1, i));

        List<Card> options = new ArrayList<>();
        for (int i = 0; i <= 3; i++)
            options.add(new Card(2, i));

        assertEquals(0, actions.size());
        actionFinder.addStockToFoundationActions(options, foundation, actions);
        assertEquals(4, actions.stream().filter(StockToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find stock card to full-1 foundation action")
    void stockFullFoundationMinusOneAction(){
        Foundation foundation = new Foundation();
        for (int i = 0; i <= 12; i++)
            foundation.add(new Card(i, 0));

        List<Card> options = new ArrayList<>();
        options.add(new Card(13, 0));

        assertEquals(0, actions.size());
        actionFinder.addStockToFoundationActions(options, foundation, actions);
        assertEquals(1, actions.stream().filter(StockToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find stock card to winning-1 foundation action")
    void stockWinningFoundationMinusOneAction(){
        Foundation foundation = new Foundation();
        for (int i = 0; i <= 12; i++)
            foundation.add(new Card(i, 0));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 1));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 2));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 3));

        List<Card> options = new ArrayList<>();
        options.add(new Card(13, 0));

        assertEquals(0, actions.size());
        actionFinder.addStockToFoundationActions(options, foundation, actions);
        assertEquals(1, actions.stream().filter(StockToFoundation.class::isInstance).count());
    }

    @Test
    @Disabled
    @DisplayName("find stock card to empty tableau action")
    void addStockToEmptyTableau() {
        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        Tableau tableau = new Tableau(board);

        List<Card> options = new ArrayList<>();
        options.add(new Card(1,0));

        assertEquals(0, actions.size());
        actionFinder.addStockToTableauActions(options,tableau, actions);
        assertEquals(1, actions.stream().filter(StockToTableau.class::isInstance).count());
    }

    @Test
    @DisplayName("find stock card to one card tableau action")
    void addStockToOneCardTableau(){
        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        board[0][0] = new Card(2, 0);
        Tableau tableau = new Tableau(board);

        List<Card> options = new ArrayList<>();
        options.add(new Card(1,1));

        assertEquals(0, actions.size());
        actionFinder.addStockToTableauActions(options,tableau, actions);
        assertEquals(1, actions.stream().filter(StockToTableau.class::isInstance).count());
    }


    @Test
    @DisplayName("find tableau card to empty foundation action")
    void tableauEmptyFoundationAction() {
        Foundation foundation = new Foundation();

        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        board[0][0] = new Card(1, 0);
        Tableau tableau = new Tableau(board);

        assertEquals(0, actions.size());
        actionFinder.addTableauToFoundationActions(tableau, foundation, actions);
        assertEquals(1, actions.stream().filter(TableauToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find tableau card to one card foundation action")
    void tableauOneCardFoundation(){
        Foundation foundation = new Foundation();
        foundation.add(new Card(1,0));

        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        board[0][0] = new Card(2, 0);
        Tableau tableau = new Tableau(board);


        assertEquals(0, actions.size());
        actionFinder.addTableauToFoundationActions(tableau, foundation, actions);
        assertEquals(1, actions.stream().filter(TableauToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find move tableau card to full-1 foundation action")
    void tableauFullFoundationMinusOneAction(){
        Foundation foundation = new Foundation();
        for (int i = 0; i <= 12; i++){
            foundation.add(new Card(i, 0));
        }

        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        board[0][0] = new Card(13, 0);
        Tableau tableau = new Tableau(board);


        assertEquals(0, actions.size());
        actionFinder.addTableauToFoundationActions(tableau, foundation, actions);
        assertEquals(1, actions.stream().filter(TableauToFoundation.class::isInstance).count());
    }

    @Test
    @DisplayName("find tableau card to winning-1 foundation action")
    void tableauWinningFoundationMinusOneAction(){
        Foundation foundation = new Foundation();
        for (int i = 0; i <= 12; i++)
            foundation.add(new Card(i, 0));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 1));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 2));

        for (int i = 0; i <= 13; i++)
            foundation.add(new Card(i, 3));

        Card[][] board = new Card[1][];
        board[0] = new Card[1];
        board[0][0] = new Card(13, 0);
        Tableau tableau = new Tableau(board);


        assertEquals(0, actions.size());
        actionFinder.addTableauToFoundationActions(tableau, foundation, actions);
        assertEquals(1, actions.stream().filter(TableauToFoundation.class::isInstance).count());
    }

    @Test
    @Disabled
    @DisplayName("find tableau card to empty tableau action")
    void tableauToEmptyTableauAction() {

    }

    @Test
    @DisplayName("find tableau card to one card tableau action")
    void tableauToOneCardTableauAction(){
        Card[][] board = new Card[2][];
        board[0] = new Card[1];
        board[1] = new Card[1];
        board[0][0] = new Card(1, 0);
        board[1][0] = new Card(2, 1);
        Tableau tableau = new Tableau(board);

        assertEquals(0, actions.size());
        actionFinder.addTableauToTableauActions(tableau, actions);
        assertEquals(1, actions.stream().filter(TableauToTableau.class::isInstance).count());
    }

    @Test
    @Disabled
    @DisplayName("find tableau cards to empty tableau action")
    void tableauCardsToEmptyTableauAction(){

    }

    @Test
    @DisplayName("find tableau cards to one card tableau action")
    void tableauCardsToOneCardTableauAction(){
        Card[][] board = new Card[2][];
        board[0] = new Card[1];
        board[1] = new Card[2];
        board[0][0] = new Card(3, 1);
        board[1][0] = new Card(2, 0);
        board[1][1] = new Card(1, 1); //top
        Tableau tableau = new Tableau(board);

        assertEquals(0, actions.size());
        actionFinder.addTableauToTableauActions(tableau, actions);
        assertEquals(1, actions.stream().filter(TableauToTableau.class::isInstance).count());
    }
}