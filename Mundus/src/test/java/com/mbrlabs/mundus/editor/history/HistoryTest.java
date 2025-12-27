package com.mbrlabs.mundus.editor.history;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HistoryTest {

    @Test
    public void addThenGoBackAndAdd() {
        CommandHistory history = new CommandHistory(CommandHistory.Companion.getDEFAULT_LIMIT());
        int ptr;

        ptr = history.add(new MockCommand());
        assertEquals(0, ptr);
        ptr = history.add(new MockCommand());
        assertEquals(1, ptr);
        ptr = history.add(new MockCommand());
        assertEquals(2, ptr);

        assertEquals(3, history.size());

        ptr = history.goBack();
        assertEquals(1, ptr);
        ptr = history.goBack();
        assertEquals(0, ptr);
        ptr = history.goBack();
        assertEquals(-1, ptr);

        ptr = history.add(new MockCommand());
        assertEquals(0, ptr);
        assertEquals(1, history.size());
    }

    @Test
    public void addThenGoBackThenGoForwardOneThenAdd() {
        CommandHistory history = new CommandHistory(CommandHistory.Companion.getDEFAULT_LIMIT());
        int ptr;

        ptr = history.add(new MockCommand());
        assertEquals(0, ptr);
        ptr = history.add(new MockCommand());
        assertEquals(1, ptr);
        ptr = history.add(new MockCommand());
        assertEquals(2, ptr);

        assertEquals(3, history.size());

        ptr = history.goBack();
        assertEquals(1, ptr);
        ptr = history.goBack();
        assertEquals(0, ptr);
        ptr = history.goBack();
        assertEquals(-1, ptr);

        ptr = history.goForward();
        assertEquals(0, ptr);

        ptr = history.add(new MockCommand());
        assertEquals(1, ptr);
        assertEquals(2, history.size());
    }

    private static class MockCommand implements Command {
        @Override
        public void execute() {
        }

        @Override
        public void undo() {
        }
    }
}
