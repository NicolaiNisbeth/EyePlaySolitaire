package ai.state;

import ai.demo.Demo;
import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    @Test
    public void testEquals() {
        State state = Demo.generateInitialState();
        State copy = CopyState.copyValues(state);
        assertEquals(state.getStock(), copy.getStock());
        assertEquals(state.getFoundation(), copy.getFoundation());
        assertEquals(state.getTableau(), copy.getTableau());
        assertEquals(state, copy);
    }

    @Test
    public void testHash() {
        State state = Demo.generateInitialState();
        State copy = CopyState.copyValues(state);
        assertEquals(state.getStock().hashCode(), copy.getStock().hashCode());
        assertEquals(state.getFoundation().hashCode(), copy.getFoundation().hashCode());
        assertEquals(state.getTableau().hashCode(), copy.getTableau().hashCode());
        assertEquals(state.hashCode(), copy.hashCode());
    }
}