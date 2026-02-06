package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Cursor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CursorManager}.
 */
public class CursorManagerTest {

    @Mock
    private Graphics graphics;

    @Mock
    private Cursor customCursor;

    private AutoCloseable closeable;

    @Before
    public void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);

        // Mock Gdx.graphics
        Gdx.graphics = graphics;

        // Reset CursorManager static state using reflection
        resetCursorManagerState();
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
    public void testSetDefaultCursorWithCustomCursor() throws Exception {
        CursorManager.setDefaultCursor(customCursor);

        // Verify the state was set correctly using reflection
        assertFalse("Should use custom cursor as default", isSystemCursorAsDefault());
        assertEquals("Custom cursor should be stored", customCursor, getDefaultCursor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultCursorWithNullCustomCursor() {
        CursorManager.setDefaultCursor((Cursor) null);
    }

    @Test
    public void testNullCustomCursorErrorMessage() {
        try {
            CursorManager.setDefaultCursor((Cursor) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message should be specific",
                    "defaultCursor can't be null", e.getMessage());
        }
    }

    @Test
    public void testSetDefaultCursorWithSystemCursor() throws Exception {
        Cursor.SystemCursor systemCursor = Cursor.SystemCursor.Hand;

        CursorManager.setDefaultCursor(systemCursor);

        // Verify the state was set correctly using reflection
        assertTrue("Should use system cursor as default", isSystemCursorAsDefault());
        assertEquals("System cursor should be stored", systemCursor, getDefaultSystemCursor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultCursorWithNullSystemCursor() {
        CursorManager.setDefaultCursor((Cursor.SystemCursor) null);
    }

    @Test
    public void testNullSystemCursorErrorMessage() {
        try {
            CursorManager.setDefaultCursor((Cursor.SystemCursor) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message should be specific",
                    "defaultCursor can't be null", e.getMessage());
        }
    }

    @Test
    public void testRestoreDefaultCursorWithSystemCursor() {
        Cursor.SystemCursor systemCursor = Cursor.SystemCursor.Hand;
        CursorManager.setDefaultCursor(systemCursor);

        CursorManager.restoreDefaultCursor();

        verify(graphics, times(1)).setSystemCursor(systemCursor);
        verify(graphics, never()).setCursor(any(Cursor.class));
    }

    @Test
    public void testRestoreDefaultCursorWithCustomCursor() {
        CursorManager.setDefaultCursor(customCursor);

        CursorManager.restoreDefaultCursor();

        verify(graphics, times(1)).setCursor(customCursor);
        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
    }

    @Test
    public void testRestoreDefaultCursorWithInitialState() {
        // Test with initial state (system cursor Arrow)
        CursorManager.restoreDefaultCursor();

        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.Arrow);
        verify(graphics, never()).setCursor(any(Cursor.class));
    }

    @Test
    public void testSwitchFromSystemToCustomCursor() throws Exception {
        // Start with system cursor
        CursorManager.setDefaultCursor(Cursor.SystemCursor.Hand);
        assertTrue("Should start with system cursor", isSystemCursorAsDefault());

        // Switch to custom cursor
        CursorManager.setDefaultCursor(customCursor);
        assertFalse("Should switch to custom cursor", isSystemCursorAsDefault());
        assertEquals("Custom cursor should be stored", customCursor, getDefaultCursor());

        // Verify restore uses custom cursor
        CursorManager.restoreDefaultCursor();
        verify(graphics, times(1)).setCursor(customCursor);
        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
    }

    @Test
    public void testSwitchFromCustomToSystemCursor() throws Exception {
        // Start with custom cursor
        CursorManager.setDefaultCursor(customCursor);
        assertFalse("Should start with custom cursor", isSystemCursorAsDefault());

        // Switch to system cursor
        Cursor.SystemCursor systemCursor = Cursor.SystemCursor.Ibeam;
        CursorManager.setDefaultCursor(systemCursor);
        assertTrue("Should switch to system cursor", isSystemCursorAsDefault());
        assertEquals("System cursor should be stored", systemCursor, getDefaultSystemCursor());

        // Verify restore uses system cursor
        CursorManager.restoreDefaultCursor();
        verify(graphics, times(1)).setSystemCursor(systemCursor);
        verify(graphics, never()).setCursor(any(Cursor.class));
    }

    @Test
    public void testMultipleRestoreCallsWithSystemCursor() {
        Cursor.SystemCursor systemCursor = Cursor.SystemCursor.Crosshair;
        CursorManager.setDefaultCursor(systemCursor);

        // Call restore multiple times
        CursorManager.restoreDefaultCursor();
        CursorManager.restoreDefaultCursor();
        CursorManager.restoreDefaultCursor();

        verify(graphics, times(3)).setSystemCursor(systemCursor);
        verify(graphics, never()).setCursor(any(Cursor.class));
    }

    @Test
    public void testMultipleRestoreCallsWithCustomCursor() {
        CursorManager.setDefaultCursor(customCursor);

        // Call restore multiple times
        CursorManager.restoreDefaultCursor();
        CursorManager.restoreDefaultCursor();
        CursorManager.restoreDefaultCursor();

        verify(graphics, times(3)).setCursor(customCursor);
        verify(graphics, never()).setSystemCursor(any(Cursor.SystemCursor.class));
    }

    @Test
    public void testDifferentSystemCursors() {
        Cursor.SystemCursor[] cursors = Cursor.SystemCursor.values();

        for (Cursor.SystemCursor cursor : cursors) {
            reset(graphics);
            CursorManager.setDefaultCursor(cursor);
            CursorManager.restoreDefaultCursor();

            verify(graphics, times(1)).setSystemCursor(cursor);
            verify(graphics, never()).setCursor(any(Cursor.class));
        }
    }

    @Test
    public void testCustomCursorOverride() throws Exception {
        Cursor firstCursor = mock(Cursor.class);
        Cursor secondCursor = mock(Cursor.class);

        // Set first cursor
        CursorManager.setDefaultCursor(firstCursor);
        assertEquals("First cursor should be stored", firstCursor, getDefaultCursor());

        // Override with second cursor
        CursorManager.setDefaultCursor(secondCursor);
        assertEquals("Second cursor should override first", secondCursor, getDefaultCursor());

        // Verify restore uses the latest cursor
        CursorManager.restoreDefaultCursor();
        verify(graphics, times(1)).setCursor(secondCursor);
        verify(graphics, never()).setCursor(firstCursor);
    }

    @Test
    public void testSystemCursorOverride() throws Exception {
        // Set first system cursor
        CursorManager.setDefaultCursor(Cursor.SystemCursor.Hand);
        assertEquals("First system cursor should be stored",
                Cursor.SystemCursor.Hand, getDefaultSystemCursor());

        // Override with second system cursor
        CursorManager.setDefaultCursor(Cursor.SystemCursor.Ibeam);
        assertEquals("Second system cursor should override first",
                Cursor.SystemCursor.Ibeam, getDefaultSystemCursor());

        // Verify restore uses the latest cursor
        CursorManager.restoreDefaultCursor();
        verify(graphics, times(1)).setSystemCursor(Cursor.SystemCursor.Ibeam);
    }

    /**
     * Helper method to reset CursorManager static state using reflection.
     */
    private void resetCursorManagerState() throws Exception {
        Field defaultCursorField = CursorManager.class.getDeclaredField("defaultCursor");
        defaultCursorField.setAccessible(true);
        defaultCursorField.set(null, null);

        Field defaultSystemCursorField = CursorManager.class.getDeclaredField("defaultSystemCursor");
        defaultSystemCursorField.setAccessible(true);
        defaultSystemCursorField.set(null, Cursor.SystemCursor.Arrow);

        Field systemCursorAsDefaultField = CursorManager.class.getDeclaredField("systemCursorAsDefault");
        systemCursorAsDefaultField.setAccessible(true);
        systemCursorAsDefaultField.set(null, true);
    }

    /**
     * Helper method to check if system cursor is used as default using reflection.
     */
    private boolean isSystemCursorAsDefault() throws Exception {
        Field field = CursorManager.class.getDeclaredField("systemCursorAsDefault");
        field.setAccessible(true);
        return field.getBoolean(null);
    }

    /**
     * Helper method to get default cursor using reflection.
     */
    private Cursor getDefaultCursor() throws Exception {
        Field field = CursorManager.class.getDeclaredField("defaultCursor");
        field.setAccessible(true);
        return (Cursor) field.get(null);
    }

    /**
     * Helper method to get default system cursor using reflection.
     */
    private Cursor.SystemCursor getDefaultSystemCursor() throws Exception {
        Field field = CursorManager.class.getDeclaredField("defaultSystemCursor");
        field.setAccessible(true);
        return (Cursor.SystemCursor) field.get(null);
    }
}
