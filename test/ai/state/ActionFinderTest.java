package ai.state;

import static org.junit.jupiter.api.Assertions.*;

class ActionFinderTest {

    ActionFinder actionFinder;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        actionFinder = new ActionFinder();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        actionFinder = null;
    }
}