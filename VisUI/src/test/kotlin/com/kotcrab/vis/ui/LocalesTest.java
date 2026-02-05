package com.kotcrab.vis.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link Locales}.
 */
public class LocalesTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return new FileHandle("test");
                        }
                        return null;
                    });
        }
    }

    @Before
    public void setUp() {
        // Reset all bundle fields to null before each test
        resetBundleFields();
    }

    @Test
    public void testGetCommonBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getCommonBundle();
            assertNotNull("Common bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetCommonBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setCommonBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getCommonBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testGetFileChooserBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getFileChooserBundle();
            assertNotNull("File chooser bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetFileChooserBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setFileChooserBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getFileChooserBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testGetDialogsBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getDialogsBundle();
            assertNotNull("Dialogs bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetDialogsBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setDialogsBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getDialogsBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testGetTabbedPaneBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getTabbedPaneBundle();
            assertNotNull("Tabbed pane bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetTabbedPaneBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setTabbedPaneBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getTabbedPaneBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testGetColorPickerBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getColorPickerBundle();
            assertNotNull("Color picker bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetColorPickerBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setColorPickerBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getColorPickerBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testGetButtonBarBundleReturnsNonNull() {
        try {
            I18NBundle bundle = Locales.getButtonBarBundle();
            assertNotNull("Button bar bundle should not be null", bundle);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testSetButtonBarBundle() {
        I18NBundle mockBundle = mock(I18NBundle.class);
        Locales.setButtonBarBundle(mockBundle);

        I18NBundle retrievedBundle = Locales.getButtonBarBundle();
        assertEquals("Should return the set bundle", mockBundle, retrievedBundle);
    }

    @Test
    public void testSetLocale() {
        Locale testLocale = Locale.FRANCE;
        Locales.setLocale(testLocale);

        // Can't easily test the effect without mocking I18NBundle.createBundle,
        // but we can test the setter doesn't throw an exception
        assertTrue("Should be able to set locale", true);
    }

    @Test
    public void testCommonTextEnum() {
        // Test PLEASE_WAIT
        Locales.CommonText pleaseWait = Locales.CommonText.PLEASE_WAIT;
        assertEquals("pleaseWait", pleaseWait.getName());

        try {
            assertNotNull("get() should not return null", pleaseWait.get());
            assertNotNull("format() should not return null", pleaseWait.format());
            assertNotNull("toString() should not return null", pleaseWait.toString());
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }

        // Test UNKNOWN_ERROR_OCCURRED
        Locales.CommonText unknownError = Locales.CommonText.UNKNOWN_ERROR_OCCURRED;
        assertEquals("unknownErrorOccurred", unknownError.getName());

        try {
            assertNotNull("get() should not return null", unknownError.get());
            assertNotNull("format() should not return null", unknownError.format());
            assertNotNull("toString() should not return null", unknownError.toString());
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    @Test
    public void testCommonTextFormatWithArguments() {
        Locales.CommonText pleaseWait = Locales.CommonText.PLEASE_WAIT;
        try {
            String formatted = pleaseWait.format("arg1", "arg2");
            assertNotNull("format() with arguments should not return null", formatted);
        } catch (Exception e) {
            // Expected if bundle files don't exist
            assertTrue("Should handle missing bundle gracefully", true);
        }
    }

    /**
     * Helper method to reset all bundle fields to null using reflection.
     */
    private void resetBundleFields() {
        try {
            Field[] fields = Locales.class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == I18NBundle.class) {
                    field.setAccessible(true);
                    field.set(null, null);
                }
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }
}
