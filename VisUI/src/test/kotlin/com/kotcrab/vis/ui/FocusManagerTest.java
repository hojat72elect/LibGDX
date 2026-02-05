package com.kotcrab.vis.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FocusManager}.
 */
public class FocusManagerTest {

    private Stage mockStage;
    private Focusable mockFocusable1;
    private Focusable mockFocusable2;
    private Actor mockActor;

    @Before
    public void setUp() {
        resetFocusManagerState();
        mockStage = mock(Stage.class);
        mockFocusable1 = mock(Focusable.class);
        mockFocusable2 = mock(Focusable.class);
        mockActor = mock(Actor.class);
    }

    /**
     * Helper method to reset FocusManager static state using reflection.
     */
    private void resetFocusManagerState() {
        try {
            Field focusedWidgetField = FocusManager.class.getDeclaredField("focusedWidget");
            focusedWidgetField.setAccessible(true);
            focusedWidgetField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }

    @Test
    public void testGetFocusedWidgetInitiallyNull() {
        assertNull("Focused widget should be null initially", FocusManager.getFocusedWidget());
    }

    @Test
    public void testSwitchFocusWithNullStage() {
        FocusManager.switchFocus(null, mockFocusable1);

        verify(mockFocusable1).focusGained();
        assertEquals("Focused widget should be the one we switched to", mockFocusable1, FocusManager.getFocusedWidget());
    }

    @Test
    public void testSwitchFocusWithStage() {
        FocusManager.switchFocus(mockStage, mockFocusable1);

        verify(mockStage).setKeyboardFocus(null);
        verify(mockFocusable1).focusGained();
        assertEquals("Focused widget should be the one we switched to", mockFocusable1, FocusManager.getFocusedWidget());
    }

    @Test
    public void testSwitchFocusFromOneToAnother() {
        // First focus on widget1
        FocusManager.switchFocus(mockStage, mockFocusable1);
        reset(mockFocusable1, mockFocusable2, mockStage);

        // Then switch to widget2
        FocusManager.switchFocus(mockStage, mockFocusable2);

        verify(mockFocusable1).focusLost();
        verify(mockStage).setKeyboardFocus(null);
        verify(mockFocusable2).focusGained();
        assertEquals("Focused widget should be widget2", mockFocusable2, FocusManager.getFocusedWidget());
    }

    @Test
    public void testSwitchFocusToSameWidget() {
        // First focus on widget1
        FocusManager.switchFocus(mockStage, mockFocusable1);
        reset(mockFocusable1, mockStage);

        // Try to switch to the same widget
        FocusManager.switchFocus(mockStage, mockFocusable1);

        verify(mockFocusable1, never()).focusLost();
        verify(mockFocusable1, never()).focusGained();
        verify(mockStage, never()).setKeyboardFocus(null);
        assertEquals("Focused widget should still be widget1", mockFocusable1, FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWithNullStage() {
        // First focus on a widget
        FocusManager.switchFocus(null, mockFocusable1);
        reset(mockFocusable1);

        // Reset focus
        FocusManager.resetFocus(null);

        verify(mockFocusable1).focusLost();
        assertNull("Focused widget should be null after reset", FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWithStage() {
        // First focus on a widget
        FocusManager.switchFocus(mockStage, mockFocusable1);
        reset(mockFocusable1, mockStage);

        // Reset focus
        FocusManager.resetFocus(mockStage);

        verify(mockFocusable1).focusLost();
        verify(mockStage).setKeyboardFocus(null);
        assertNull("Focused widget should be null after reset", FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWithStageAndActor() {
        // First focus on a widget
        FocusManager.switchFocus(mockStage, mockFocusable1);
        reset(mockFocusable1, mockStage);

        // Mock stage keyboard focus
        when(mockStage.getKeyboardFocus()).thenReturn(mockActor);

        // Reset focus
        FocusManager.resetFocus(mockStage, mockActor);

        verify(mockFocusable1).focusLost();
        verify(mockStage).setKeyboardFocus(null);
        assertNull("Focused widget should be null after reset", FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWithStageAndDifferentActor() {
        // First focus on a widget
        FocusManager.switchFocus(mockStage, mockFocusable1);
        reset(mockFocusable1, mockStage);

        // Mock stage keyboard focus to a different actor
        when(mockStage.getKeyboardFocus()).thenReturn(mock(Actor.class));

        // Reset focus
        FocusManager.resetFocus(mockStage, mockActor);

        verify(mockFocusable1).focusLost();
        verify(mockStage, never()).setKeyboardFocus(null);
        assertNull("Focused widget should be null after reset", FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWhenNoWidgetFocused() {
        // Reset focus when nothing is focused
        FocusManager.resetFocus(mockStage);

        verify(mockStage).setKeyboardFocus(null);
        assertNull("Focused widget should remain null", FocusManager.getFocusedWidget());
    }

    @Test
    public void testResetFocusWithStageAndActorWhenNoWidgetFocused() {
        // Reset focus when nothing is focused
        when(mockStage.getKeyboardFocus()).thenReturn(mockActor);
        FocusManager.resetFocus(mockStage, mockActor);

        verify(mockStage).setKeyboardFocus(null);
        assertNull("Focused widget should remain null", FocusManager.getFocusedWidget());
    }
}
