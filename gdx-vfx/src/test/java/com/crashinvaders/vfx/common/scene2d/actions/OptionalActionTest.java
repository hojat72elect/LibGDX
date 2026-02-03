package com.crashinvaders.vfx.common.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OptionalActionTest {

    @Test
    public void testConditionPassed() {
        OptionalAction action = new OptionalAction();
        Action wrappedAction = mock(Action.class);
        action.setAction(wrappedAction);

        OptionalAction.Condition condition = mock(OptionalAction.Condition.class);
        when(condition.check()).thenReturn(true);
        action.setCondition(condition);

        when(wrappedAction.act(0.1f)).thenReturn(false);
        assertFalse(action.act(0.1f));
        verify(wrappedAction).act(0.1f);

        // Second call should not check condition again but still delegate
        when(wrappedAction.act(0.2f)).thenReturn(true);
        assertTrue(action.act(0.2f));
        verify(wrappedAction).act(0.2f);
        verify(condition, times(1)).check();
    }

    @Test
    public void testConditionFailed() {
        OptionalAction action = new OptionalAction();
        Action wrappedAction = mock(Action.class);
        action.setAction(wrappedAction);

        OptionalAction.Condition condition = mock(OptionalAction.Condition.class);
        when(condition.check()).thenReturn(false);
        action.setCondition(condition);

        assertTrue(action.act(0.1f));
        verify(wrappedAction, never()).act(anyFloat());
        verify(condition, times(1)).check();
    }

    @Test
    public void testRestart() {
        OptionalAction action = new OptionalAction();
        Action wrappedAction = mock(Action.class);
        action.setAction(wrappedAction);

        OptionalAction.Condition condition = mock(OptionalAction.Condition.class);
        when(condition.check()).thenReturn(false);
        action.setCondition(condition);

        assertTrue(action.act(0.1f));
        verify(condition, times(1)).check();

        action.restart();
        when(condition.check()).thenReturn(true);
        when(wrappedAction.act(0.1f)).thenReturn(true);

        assertTrue(action.act(0.1f));
        verify(condition, times(2)).check();
        verify(wrappedAction).act(0.1f);
    }
}
