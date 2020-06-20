package ai.action;

import ai.state.Card;
import ai.state.Foundation;
import ai.state.RemainingCards;
import ai.state.State;
import ai.state.Stock;
import ai.state.Tableau;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Set;

@DisplayName("Action classes:")
public class ActionTest {

    @Nested
    @DisplayName("Stock to foundation:")
    class StockToFoundation{
        ai.action.StockToFoundation stockToFoundation;
        State state;

        @AfterEach
        void tearDown(){
            state = null;
            stockToFoundation = null;
        }

        @RepeatedTest(value = 3, name = "card -> {currentRepetition}")
        @DisplayName("Turn based removal and execution of top 3 cards in stock")
        void threeTurnRemovalAndExecution(RepetitionInfo repetitionInfo){
            // setup initial state
            Card[] cards = {
                    new Card(10, 2), new Card(3,1), new Card(4,0), //top
            };
            state = new State(new Stock(cards), null, new Foundation(), null);
            Stock stock = state.getStock();
            Foundation foundation = state.getFoundation();

            // remove number of cards based on test number
            int i = repetitionInfo.getCurrentRepetition()-1;
            for (int j = 0; j < i; j++)
                stock.removeCard(stock.showOptions().get(0));

            // print status
            System.out.println("#" + repetitionInfo.getCurrentRepetition());
            System.out.println("Card options: " +stock.showOptions());
            System.out.println(foundation);

            // confirm assumptions
            Card cardToTake = stock.showOptions().get(0);
            System.out.println("Card to take: "  + cardToTake);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(cards[2-i], cardToTake, "Doesn't take the correct card from stock"),
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->foundation.peek(i), "Foundation is not empty")
            );

            // execute from stock to foundation action
            stockToFoundation = new ai.action.StockToFoundation(cardToTake);
            State resultState = stockToFoundation.getResults(state).iterator().next();

            // confirm correct result state
            boolean isCardRemovedFromStock = Arrays.stream(resultState.getStock().getCards()).noneMatch(card -> card.equals(cardToTake));
            Assertions.assertAll(
                    () -> Assertions.assertTrue(isCardRemovedFromStock, "Card is not removed from the stock"),
                    () -> Assertions.assertEquals(cardToTake, resultState.getFoundation().peek(i), "Card is not added to top of foundation")
            );
            System.out.println(resultState.getFoundation());
        }

        @Test
        @DisplayName("Stack cards of same suit in foundation")
        void stackThreeCardsOnTop(){
            final int topCard = 0, firstStack = 0;

            // setup initial state
            Card[] cards = {
                    new Card(10, 0), new Card(11,0), new Card(12,0), //top
            };
            state = new State(new Stock(cards), null, new Foundation(), null);
            System.out.println(state.getStock());

            // execute from stock to foundation action 3 times
            int count = 1;
            while (state.getStock().getCards().length > 0){
                Card cardToMove = state.getStock().showOptions().get(topCard);
                System.out.println("Card to take: " + cardToMove);

                // execute stock to foundation action
                stockToFoundation = new ai.action.StockToFoundation(cardToMove);
                State resultState = stockToFoundation.getResults(state).iterator().next();

                // confirm correct result state
                int numberOfCardsInFoundation = count++;
                Card topCardInFirstStack = resultState.getFoundation().peek(firstStack);
                Assertions.assertAll(
                        () -> Assertions.assertEquals(cardToMove, topCardInFirstStack, "Card is not added to top of first stack in foundation"),
                        () -> Assertions.assertEquals(numberOfCardsInFoundation, resultState.getFoundation().getCount(), "Number of cards in foundation is not correct")
                );

                state = resultState;
                System.out.println(state.getFoundation());
            }

            System.out.println("Final peek: " + state.getFoundation().peek(topCard));
        }
    }

    @Nested
    @DisplayName("Stock to tableau:")
    class StockToTableau{
        ai.action.StockToTableau stockToTableau;
        State state;

        @BeforeEach
        void beforeEach(){
            Card[] cards = {new Card(3, 0), new Card(2, 1), new Card(7, 2)};
            Card[][] tableau = {
                    {},
                    {null,new Card(8,1)},
            };
            state = new State(new Stock(cards), new Tableau(tableau),null,null);
        }

        @AfterEach
        void afterEach(){
            stockToTableau = null;
            state = null;
        }

        @Test
        @DisplayName("Add card to empty stack 1")
        void addCardToEmptyStack(){
            final int cardOnTop = 0;
            final int firstStack = 0;
            Stock stock = state.getStock();
            Tableau tableau = state.getTableau();

            // confirm assumptions
            System.out.println("Card options: " +stock.showOptions());
            System.out.println(tableau);
            Card cardToTake = stock.showOptions().get(cardOnTop);
            System.out.println("Card to take: "  + cardToTake);

            Assertions.assertAll(
                    () -> Assertions.assertEquals(stock.getCards()[2], cardToTake, "Doesn't take the correct card from stock"),
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->tableau.getStacks().get(firstStack).peek(), "Tableau stack 1 is not empty")
            );

            // execute stock to tableau action
            stockToTableau = new ai.action.StockToTableau(cardToTake, firstStack);
            State resultState = stockToTableau.getResults(state).iterator().next();

            // confirm correct result state
            boolean isCardRemovedFromStock = Arrays.stream(resultState.getStock().getCards()).noneMatch(card -> card.equals(cardToTake));
            Card topCardInTableau = resultState.getTableau().getStacks().get(firstStack).peek();

            Assertions.assertAll(
                    () -> Assertions.assertTrue(isCardRemovedFromStock, "Card is not removed from the stock"),
                    () -> Assertions.assertEquals(cardToTake, topCardInTableau, "Card is not added to empty tableau stack")
            );

            System.out.println(resultState.getTableau());
        }

        @Test
        @DisplayName("Add card on top of another card in stack 2")
        void addCardOnTopOfAnotherCard(){
            final int secondStack = 1;
            final int cardOnTop = 0;
            Tableau tableau = state.getTableau();
            Stock stock = state.getStock();

            // confirm assumptions
            System.out.println("Card options: "+state.getStock().showOptions());
            System.out.println(tableau);
            Card cardToTake = stock.showOptions().get(cardOnTop);
            System.out.println("Card to take: "+cardToTake);

            Card topCardInSecondTack = tableau.getStacks().get(secondStack).peek();
            Assertions.assertAll(
                    () -> Assertions.assertEquals(stock.getCards()[2], cardToTake, "Doesn't take the correct card from stock"),
                    () -> Assertions.assertNotNull(topCardInSecondTack, "Foundation stack 2 doesn't have another card to add on top off")
            );

            // execute stock to tableau action
            stockToTableau = new ai.action.StockToTableau(cardToTake, secondStack);
            State resultState = stockToTableau.getResults(state).iterator().next();

            // confirm correct result state
            boolean isCardRemovedFromStock = Arrays.stream(resultState.getStock().getCards()).noneMatch(card -> card.equals(cardToTake));
            Card topCardInTableau = resultState.getTableau().getStacks().get(secondStack).peek();
            int updatedSizeOfSecondStack = resultState.getTableau().getStacks().get(secondStack).size();

            Assertions.assertAll(
                    () -> Assertions.assertTrue(isCardRemovedFromStock, "Card is not removed from the stock"),
                    () -> Assertions.assertEquals(cardToTake, topCardInTableau, "Top card in stack 2 is not the card we moved"),
                    () -> Assertions.assertEquals(3, updatedSizeOfSecondStack, "Size of stack 2 is not correct")
            );

            System.out.println(resultState.getTableau());
        }
    }

    @Nested
    @DisplayName("Tableau to foundation: ")
    class TableauToFoundation{

        ai.action.TableauToFoundation tableauToFoundation;
        State state;
        Card[][] tableau = {
                {new Card(3,0)},
                {null,new Card(8,1)},
                {null,new Card(10,1), new Card(9,2)},
        };

        @BeforeEach
        void beforeEach(){
            Set<Card> cards = new HashSet<>();
            cards.add(new Card(4, 0));
            cards.add(new Card(5, 0));
            cards.add(new Card(6, 0));
            cards.add(new Card(7, 0));

            state = new State(null, new Tableau(tableau), new Foundation(), new RemainingCards(cards));
        }

        @Test
        @DisplayName("Add card to foundation and make stack 1 empty")
        void moveOneCardAndMakeStackEmpty(){
            final int firstStack = 0;

            // confirm assumptions
            System.out.println(state.getTableau());
            System.out.println(state.getFoundation());
            Card cardToMove = state.getTableau().getStacks().get(firstStack).peek();
            System.out.println("Card I wish to move: "+cardToMove);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(tableau[0][0], cardToMove),
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->state.getFoundation().peek(cardToMove.getSuit()))
            );

            // execute tableau to foundation action
            tableauToFoundation = new ai.action.TableauToFoundation(cardToMove, cardToMove.getSuit());
            State resultState = tableauToFoundation.getResults(state).iterator().next();

            // confirm result state
            Assertions.assertAll(
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->resultState.getTableau().getStacks().get(firstStack).peek()),
                    () -> Assertions.assertEquals(tableau[0][0], resultState.getFoundation().getStacks().get(cardToMove.getSuit()).peek())
            );
            System.out.println(resultState.getTableau());
            System.out.println(resultState.getFoundation());
        }

        @Test
        @DisplayName("Add card from filled stack 2 to foundation")
        void moveOneCardAndStackNotEmpty(){
            final int secondStack = 1;

            // confirm assumptions
            System.out.println(state.getTableau());
            System.out.println(state.getFoundation());
            Card cardToMove = state.getTableau().getStacks().get(secondStack).peek();
            System.out.println("Card I wish to move: "+cardToMove);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(tableau[1][1], cardToMove),
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->state.getFoundation().peek(cardToMove.getSuit()))
            );

            // execute tableau to foundation action
            tableauToFoundation = new ai.action.TableauToFoundation(cardToMove, cardToMove.getSuit());
            State resultState = tableauToFoundation.getResults(state).iterator().next();

            // confirm result state
            Assertions.assertAll(
                    () -> Assertions.assertNotEquals(cardToMove, resultState.getTableau().getStacks().get(secondStack).peek(), "Moved card is still in stock 2"),
                    () -> Assertions.assertEquals(cardToMove, resultState.getFoundation().peek(cardToMove.getSuit()))
            );
            System.out.println(resultState.getTableau());
            System.out.println(resultState.getFoundation());
        }
    }

    @Nested
    @DisplayName("Tableau to tableau: ")
    class TableauToTableau{

        ai.action.TableauToTableau tableauToTableau;
        State state;
        Card[][] cards = {
                {new Card(3,0)},
                {null,new Card(8,1)}
        };

        @BeforeEach
        void beforeEach(){
            Set<Card> cards = new HashSet<>();
            cards.add(this.cards[0][0]);
            cards.add(this.cards[1][1]);
            state = new State(null, new Tableau(this.cards), new Foundation(), new RemainingCards(cards));
        }

        @Test
        @DisplayName("Move card from stack 1 to stack 2")
        void moveOneCard(){
            final int firstStack = 0, secondStack = 1;
            Tableau tableau = state.getTableau();

            // confirm assumptions
            System.out.println(tableau);
            Card cardToMove = tableau.getStacks().get(firstStack).peek();
            final int sum = tableau.getSum();
            System.out.println("Card to move " + cardToMove);

            Card topCardInFirstStack = tableau.getStacks().get(firstStack).peek();
            Card topCardInSecondStack = tableau.getStacks().get(secondStack).peek();
            Assertions.assertAll(
                    () -> Assertions.assertEquals(cards[0][0], topCardInFirstStack),
                    () -> Assertions.assertEquals(cards[1][1], topCardInSecondStack)
            );

            // execute tableau to tableau action
            tableauToTableau = new ai.action.TableauToTableau(0, 1, cardToMove);
            State resultState = tableauToTableau.getResults(state).iterator().next();

            // confirm result state
            Card newTopCardInSecondStack = resultState.getTableau().getStacks().get(secondStack).peek();
            Assertions.assertAll(
                    () -> Assertions.assertThrows(EmptyStackException.class, ()->resultState.getTableau().getStacks().get(firstStack).peek()),
                    () -> Assertions.assertEquals(cardToMove, newTopCardInSecondStack),
                    () -> Assertions.assertEquals(sum, resultState.getTableau().getSum())
            );
            System.out.println(resultState.getTableau());
        }
    }
}
