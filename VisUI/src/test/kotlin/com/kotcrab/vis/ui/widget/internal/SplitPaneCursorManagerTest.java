package com.kotcrab.vis.ui.widget.internal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SplitPaneCursorManager}.
 */
public class SplitPaneCursorManagerTest {

    @Mock
    private Graphics graphics;

    @Mock
    private Actor owner;

    @Mock
    private Actor toActor;

    @Mock
    private InputEvent inputEvent;

    private AutoCloseable closeable;
    private TestSplitPaneCursorManager verticalCursorManager;
    private TestSplitPaneCursorManager horizontalCursorManager;

    @Before
    public void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);

        // Mock Gdx.graphics
        Gdx.graphics = graphics;

        // Setup owner actor
        when(owner.isDescendantOf(owner)).thenReturn(true);
        when(toActor.isDescendantOf(owner)).thenReturn(false);

        // Create test instances
        verticalCursorManager = new TestSplitPaneCursorManager(owner, true);
        horizontalCursorManager = new TestSplitPaneCursorManager(owner, false);
    }

    @After
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }

        // Reset Gdx.graphics to null to avoid affecting other tests
        Gdx.graphics = null;
    }

    @Test
    public void testTouchDownWithinBounds() {
        verticalCursorManager.setHandleBoundsContainsResult(true);

        boolean result = verticalCursorManager.touchDown(inputEvent, 10f, 10f, 0, 0);

        assertTrue("Should return true when within bounds", result);
    }

    @Test
    public void testTouchDownOutsideBounds() {
        verticalCursorManager.setHandleBoundsContainsResult(false);

        boolean result = verticalCursorManager.touchDown(inputEvent, 100f, 100f, 0, 0);

        assertFalse("Should return false when outside bounds", result);
    }

    @Test
    public void testTouchDraggedWithinBounds() throws Exception {
        verticalCursorManager.setContainsResult(true);

        verticalCursorManager.touchDragged(inputEvent, 10f, 10f, 0);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be VerticalResize",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testTouchDraggedOutsideBounds() throws Exception {
        verticalCursorManager.setContainsResult(false);

        verticalCursorManager.touchDragged(inputEvent, 100f, 100f, 0);

        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
        assertEquals("Current cursor should be null", null, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testTouchDraggedFromInsideToOutside() throws Exception {
        // Start inside bounds
        verticalCursorManager.setContainsResult(true);
        verticalCursorManager.touchDragged(inputEvent, 10f, 10f, 0);

        // Move outside bounds
        verticalCursorManager.setContainsResult(false);
        verticalCursorManager.touchDragged(inputEvent, 100f, 100f, 0);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be null after exit", null, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testTouchDraggedFromOutsideToInside() throws Exception {
        // Start outside bounds
        verticalCursorManager.setContainsResult(false);
        verticalCursorManager.touchDragged(inputEvent, 100f, 100f, 0);

        // Move inside bounds
        verticalCursorManager.setContainsResult(true);
        verticalCursorManager.touchDragged(inputEvent, 10f, 10f, 0);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be VerticalResize",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testMouseMovedWithinBounds() throws Exception {
        verticalCursorManager.setHandleBoundsContainsResult(true);

        boolean result = verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be VerticalResize",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
        assertFalse("Should return false", result);
    }

    @Test
    public void testMouseMovedOutsideBounds() throws Exception {
        verticalCursorManager.setHandleBoundsContainsResult(false);

        boolean result = verticalCursorManager.mouseMoved(inputEvent, 100f, 100f);

        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
        assertEquals("Current cursor should be null", null, getCurrentCursor(verticalCursorManager));
        assertFalse("Should return false", result);
    }

    @Test
    public void testMouseMovedFromInsideToOutside() throws Exception {
        // Start inside bounds
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Move outside bounds
        verticalCursorManager.setHandleBoundsContainsResult(false);
        verticalCursorManager.mouseMoved(inputEvent, 100f, 100f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be null after exit", null, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testMouseMovedFromOutsideToInside() throws Exception {
        // Start outside bounds
        verticalCursorManager.setHandleBoundsContainsResult(false);
        verticalCursorManager.mouseMoved(inputEvent, 100f, 100f);

        // Move inside bounds
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
        assertEquals("Current cursor should be VerticalResize",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testHorizontalCursorSet() throws Exception {
        horizontalCursorManager.setHandleBoundsContainsResult(true);

        horizontalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.HorizontalResize);
        assertEquals("Current cursor should be HorizontalResize",
                Cursor.SystemCursor.HorizontalResize, getCurrentCursor(horizontalCursorManager));
    }

    @Test
    public void testExitWithNullToActor() throws Exception {
        // Set cursor first
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Exit with null toActor
        verticalCursorManager.exit(inputEvent, 10f, 10f, -1, null);

        assertEquals("Current cursor should be null after exit", null, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testExitWithNonDescendantToActor() throws Exception {
        // Set cursor first
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Exit with non-descendant toActor
        verticalCursorManager.exit(inputEvent, 10f, 10f, -1, toActor);

        assertEquals("Current cursor should be null after exit", null, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testExitWithDescendantToActor() throws Exception {
        // Set cursor first
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Create descendant actor
        Actor descendantActor = mock(Actor.class);
        when(descendantActor.isDescendantOf(owner)).thenReturn(true);

        // Exit with descendant toActor
        verticalCursorManager.exit(inputEvent, 10f, 10f, -1, descendantActor);

        assertEquals("Current cursor should remain when exiting to descendant",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testExitWithNonMousePointer() throws Exception {
        // Set cursor first
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Exit with non-mouse pointer (pointer != -1)
        verticalCursorManager.exit(inputEvent, 10f, 10f, 0, toActor);

        assertEquals("Current cursor should remain when exiting with non-mouse pointer",
                Cursor.SystemCursor.VerticalResize, getCurrentCursor(verticalCursorManager));
    }

    @Test
    public void testMultipleCursorChangesVertical() {
        // Test multiple cursor changes for vertical split pane
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);

        // Change to horizontal
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Should not set cursor again since it's the same
        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);
    }

    @Test
    public void testMultipleCursorChangesHorizontal() {
        // Test multiple cursor changes for horizontal split pane
        horizontalCursorManager.setHandleBoundsContainsResult(true);
        horizontalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.HorizontalResize);

        // Change again
        horizontalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        // Should not set cursor again since it's the same
        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.HorizontalResize);
    }

    @Test
    public void testCursorSwitchBetweenVerticalAndHorizontal() {
        // Test with vertical manager
        verticalCursorManager.setHandleBoundsContainsResult(true);
        verticalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.VerticalResize);

        // Test with horizontal manager
        horizontalCursorManager.setHandleBoundsContainsResult(true);
        horizontalCursorManager.mouseMoved(inputEvent, 10f, 10f);

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.HorizontalResize);
    }

    @Test
    public void testClearCursorWhenNotSet() {
        // Try to clear cursor when it's not set
        verticalCursorManager.setContainsResult(false);
        verticalCursorManager.touchDragged(inputEvent, 100f, 100f, 0);

        // Should not cause any issues
        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
    }

    /**
     * Helper method to get current cursor using reflection.
     */
    private Cursor.SystemCursor getCurrentCursor(SplitPaneCursorManager manager) throws Exception {
        Field field = SplitPaneCursorManager.class.getDeclaredField("currentCursor");
        field.setAccessible(true);
        return (Cursor.SystemCursor) field.get(manager);
    }

    /**
     * Test implementation of SplitPaneCursorManager that allows controlling the abstract methods.
     */
    private static class TestSplitPaneCursorManager extends SplitPaneCursorManager {
        private boolean handleBoundsContainsResult = false;
        private boolean containsResult = false;

        public TestSplitPaneCursorManager(Actor owner, boolean vertical) {
            super(owner, vertical);
        }

        public void setHandleBoundsContainsResult(boolean result) {
            this.handleBoundsContainsResult = result;
        }

        public void setContainsResult(boolean result) {
            this.containsResult = result;
        }


        @Override
        protected boolean handleBoundsContains(float x, float y) {
            return handleBoundsContainsResult;
        }

        @Override
        protected boolean contains(float x, float y) {
            return containsResult;
        }
    }
}
