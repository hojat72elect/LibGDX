package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link OsUtils}.
 */
@RunWith(MockitoJUnitRunner.class)
public class OsUtilsTest {

    @Before
    public void setUp() {
        // Reset Gdx.app to null before each test
        Gdx.app = null;
    }

    @After
    public void tearDown() {
        // Reset Gdx.app after each test
        Gdx.app = null;
    }

    @Test
    public void testIsWindows() {
        // Test the actual current OS detection
        boolean expectedWindows = System.getProperty("os.name", "").toLowerCase().contains("win");
        assertEquals("Should detect current OS correctly", expectedWindows, OsUtils.isWindows());
    }

    @Test
    public void testIsMac() {
        // Test the actual current OS detection
        boolean expectedMac = System.getProperty("os.name", "").toLowerCase().contains("mac");
        assertEquals("Should detect current OS correctly", expectedMac, OsUtils.isMac());
    }

    @Test
    public void testIsUnix() {
        // Test the actual current OS detection
        String os = System.getProperty("os.name", "").toLowerCase();
        boolean expectedUnix = os.contains("nix") || os.contains("nux") || os.contains("aix");
        assertEquals("Should detect current OS correctly", expectedUnix, OsUtils.isUnix());
    }

    @Test
    public void testOsDetectionConsistency() {
        // Test that only one OS detection method returns true for current OS
        int trueCount = 0;
        if (OsUtils.isWindows()) trueCount++;
        if (OsUtils.isMac()) trueCount++;
        if (OsUtils.isUnix()) trueCount++;

        assertTrue("At least one OS should be detected", trueCount >= 1);
        assertTrue("At most one desktop OS should be detected", trueCount <= 1);
    }

    @Test
    public void testIsIos() {
        Application mockApp = mock(Application.class);
        when(mockApp.getType()).thenReturn(Application.ApplicationType.iOS);
        Gdx.app = mockApp;

        assertTrue("Should detect iOS", OsUtils.isIos());
        assertFalse("Should not detect as Android", OsUtils.isAndroid());
    }

    @Test
    public void testIsAndroid() {
        Application mockApp = mock(Application.class);
        when(mockApp.getType()).thenReturn(Application.ApplicationType.Android);
        when(mockApp.getVersion()).thenReturn(30);
        Gdx.app = mockApp;

        assertTrue("Should detect Android", OsUtils.isAndroid());
        assertFalse("Should not detect as iOS", OsUtils.isIos());
        assertEquals("Should return correct API level", 30, OsUtils.getAndroidApiLevel());
    }

    @Test
    public void testGetAndroidApiLevelOnNonAndroid() {
        Application mockApp = mock(Application.class);
        when(mockApp.getType()).thenReturn(Application.ApplicationType.Desktop);
        Gdx.app = mockApp;

        assertEquals("Should return 0 for non-Android", 0, OsUtils.getAndroidApiLevel());
    }

    @Test
    public void testGetAndroidApiLevelOnAndroid() {
        Application mockApp = mock(Application.class);
        when(mockApp.getType()).thenReturn(Application.ApplicationType.Android);
        when(mockApp.getVersion()).thenReturn(33);
        Gdx.app = mockApp;

        assertEquals("Should return correct API level", 33, OsUtils.getAndroidApiLevel());
    }

    @Test
    public void testGetShortcutForCurrentPlatform() {
        // Test shortcut generation on current platform
        String shortcut = OsUtils.getShortcutFor(Input.Keys.CONTROL_LEFT, Input.Keys.SHIFT_LEFT, Input.Keys.A);
        assertTrue("Should contain A", shortcut.contains("A"));

        // Test with different key combinations
        shortcut = OsUtils.getShortcutFor(Input.Keys.ALT_LEFT, Input.Keys.F4);
        assertTrue("Should contain F4", shortcut.contains("F4"));
    }

    @Test
    public void testGetShortcutWithRightModifiers() {
        // Test with right-side modifier keys
        String shortcut = OsUtils.getShortcutFor(Input.Keys.CONTROL_RIGHT, Input.Keys.SHIFT_RIGHT, Input.Keys.ALT_RIGHT, Input.Keys.B);
        assertTrue("Should contain B", shortcut.contains("B"));

        // The exact format depends on current OS, so we just check it contains expected elements
        if (OsUtils.isMac()) {
            // On Mac, should contain Unicode symbols
            assertTrue("Should contain Mac symbols",
                    shortcut.contains("⌘") || shortcut.contains("⇧") || shortcut.contains("⌥"));
        } else {
            // On Windows/Unix, should contain text names
            assertTrue("Should contain modifier names",
                    shortcut.contains("Ctrl") || shortcut.contains("Shift") || shortcut.contains("Alt"));
        }
    }

    @Test
    public void testGetShortcutWithIgnoredKeys() {
        // Test with Integer.MIN_VALUE (should be ignored)
        String shortcut = OsUtils.getShortcutFor(Input.Keys.CONTROL_LEFT, Integer.MIN_VALUE, Input.Keys.A);
        assertTrue("Should contain A", shortcut.contains("A"));
        assertFalse("Should not contain MIN_VALUE", shortcut.contains(String.valueOf(Integer.MIN_VALUE)));

        // Test with multiple ignored keys
        shortcut = OsUtils.getShortcutFor(Integer.MIN_VALUE, Input.Keys.CONTROL_LEFT, Integer.MIN_VALUE, Input.Keys.A);
        assertTrue("Should contain A", shortcut.contains("A"));
    }

    @Test
    public void testGetShortcutWithRegularKeys() {
        // Test with regular keys
        String shortcut = OsUtils.getShortcutFor(Input.Keys.A, Input.Keys.B, Input.Keys.C);
        assertTrue("Should contain A", shortcut.contains("A"));
        assertTrue("Should contain B", shortcut.contains("B"));
        assertTrue("Should contain C", shortcut.contains("C"));

        // Test with function keys
        shortcut = OsUtils.getShortcutFor(Input.Keys.F1, Input.Keys.F2);
        assertTrue("Should contain F1", shortcut.contains("F1"));
        assertTrue("Should contain F2", shortcut.contains("F2"));
    }

    @Test
    public void testGetShortcutWithSingleKey() {
        // Test with single key
        String shortcut = OsUtils.getShortcutFor(Input.Keys.A);
        assertEquals("A", shortcut);

        // Test with single modifier
        shortcut = OsUtils.getShortcutFor(Input.Keys.CONTROL_LEFT);
        if (OsUtils.isMac()) {
            assertEquals("⌘", shortcut);
        } else {
            assertEquals("Ctrl", shortcut);
        }
    }

    @Test
    public void testGetShortcutWithEmptyArray() {
        // Test with empty array
        String shortcut = OsUtils.getShortcutFor();
        assertEquals("", shortcut);
    }

    @Test
    public void testGetShortcutWithAllIgnoredKeys() {
        // Test with all ignored keys
        String shortcut = OsUtils.getShortcutFor(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertEquals("", shortcut);
    }

    @Test
    public void testSymKeyMapping() {
        // Test that SYM key is mapped to Ctrl
        String shortcut = OsUtils.getShortcutFor(Input.Keys.SYM, Input.Keys.C);
        assertTrue("Should contain C", shortcut.contains("C"));

        if (OsUtils.isMac()) {
            assertTrue("Should contain Cmd symbol", shortcut.contains("⌘"));
        } else {
            assertTrue("Should contain Ctrl", shortcut.contains("Ctrl"));
        }
    }

    @Test
    public void testPlatformSpecificSeparators() {
        // Test that platform-specific separators are used correctly
        String shortcut = OsUtils.getShortcutFor(Input.Keys.CONTROL_LEFT, Input.Keys.A);
        assertTrue("Should contain A", shortcut.contains("A"));

        // Verify format is correct for current platform
        if (OsUtils.isMac()) {
            assertFalse("Mac should not use + separator", shortcut.contains("+"));
            assertTrue("Mac should use Cmd symbol", shortcut.contains("⌘"));
        } else {
            assertTrue("Other platforms should use + separator", shortcut.contains("+"));
            assertTrue("Other platforms should use Ctrl", shortcut.contains("Ctrl"));
        }
    }

    @Test
    public void testMobilePlatformDetection() {
        // Test that mobile platforms are properly detected
        Application mockApp = mock(Application.class);

        // Test iOS
        when(mockApp.getType()).thenReturn(Application.ApplicationType.iOS);
        Gdx.app = mockApp;
        assertTrue("Should detect iOS", OsUtils.isIos());
        assertFalse("Should not detect iOS as Android", OsUtils.isAndroid());

        // Test Android
        when(mockApp.getType()).thenReturn(Application.ApplicationType.Android);
        Gdx.app = mockApp;
        assertTrue("Should detect Android", OsUtils.isAndroid());
        assertFalse("Should not detect Android as iOS", OsUtils.isIos());

        // Test Desktop
        when(mockApp.getType()).thenReturn(Application.ApplicationType.Desktop);
        Gdx.app = mockApp;
        assertFalse("Should not detect Desktop as iOS", OsUtils.isIos());
        assertFalse("Should not detect Desktop as Android", OsUtils.isAndroid());
    }
}
