package com.kotcrab.vis.ui.util.dialog;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.kotcrab.vis.ui.Locales;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Simplified unit tests for {@link Dialogs} that focus on basic functionality.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DialogsTest {

    @Mock
    private Stage mockStage;

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            FileHandle mockHandle = mock(FileHandle.class);
                            when(mockHandle.exists()).thenReturn(false);
                            return mockHandle;
                        }
                        return null;
                    });
        }
        if (Gdx.app == null) {
            Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }

        // Setup mock I18N bundles
        setupMockBundles();
    }

    private static void setupMockBundles() {
        try {
            I18NBundle mockBundle = (I18NBundle) Proxy.newProxyInstance(
                    I18NBundle.class.getClassLoader(),
                    new Class[]{I18NBundle.class},
                    (proxy, method, args) -> {
                        if ("get".equals(method.getName()) && args.length > 0) {
                            return args[0]; // Return key as value
                        }
                        return null;
                    });

            setBundleField("buttonBarBundle", mockBundle);
            setBundleField("commonBundle", mockBundle);
            setBundleField("dialogsBundle", mockBundle);
        } catch (Exception e) {
            // Continue if reflection fails
        }
    }

    private static void setBundleField(String fieldName, I18NBundle bundle) throws Exception {
        Field field = Locales.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, bundle);
    }

    @Before
    public void setUp() {
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
    }

    @Test
    public void testOptionDialogTypeEnum() {
        Dialogs.OptionDialogType[] values = Dialogs.OptionDialogType.values();

        assertEquals("Should have 3 enum values", 3, values.length);

        boolean hasYesNo = false;
        boolean hasYesNoCancel = false;
        boolean hasYesCancel = false;

        for (Dialogs.OptionDialogType type : values) {
            if (type == Dialogs.OptionDialogType.YES_NO) hasYesNo = true;
            if (type == Dialogs.OptionDialogType.YES_NO_CANCEL) hasYesNoCancel = true;
            if (type == Dialogs.OptionDialogType.YES_CANCEL) hasYesCancel = true;
        }

        assertTrue("Should have YES_NO", hasYesNo);
        assertTrue("Should have YES_NO_CANCEL", hasYesNoCancel);
        assertTrue("Should have YES_CANCEL", hasYesCancel);
    }

    @Test
    public void testOptionDialogTypeEnumValueOf() {
        assertEquals("YES_NO should match",
                Dialogs.OptionDialogType.YES_NO,
                Dialogs.OptionDialogType.valueOf("YES_NO"));

        assertEquals("YES_NO_CANCEL should match",
                Dialogs.OptionDialogType.YES_NO_CANCEL,
                Dialogs.OptionDialogType.valueOf("YES_NO_CANCEL"));

        assertEquals("YES_CANCEL should match",
                Dialogs.OptionDialogType.YES_CANCEL,
                Dialogs.OptionDialogType.valueOf("YES_CANCEL"));
    }

    @Test
    public void testConfirmDialogConstructorWithMismatchedArrays() {
        try {
            String[] buttons = {"Button1", "Button2"};
            String[] returns = {"Return1"}; // Mismatched length

            new Dialogs.ConfirmDialog<String>("Test Title", "Test Message", buttons, returns, null);
            assertTrue("Should have thrown exception", false);
        } catch (IllegalStateException e) {
            assertNotNull("Exception should have message", e.getMessage());
        }
    }

    @Test
    public void testDialogsClassExists() {
        assertNotNull("Dialogs class should exist", Dialogs.class);
    }

    @Test
    public void testOptionDialogTypeClassExists() {
        assertNotNull("OptionDialogType class should exist", Dialogs.OptionDialogType.class);
    }

    @Test
    public void testConfirmDialogClassExists() {
        assertNotNull("ConfirmDialog class should exist", Dialogs.ConfirmDialog.class);
    }

    @Test
    public void testInputDialogClassExists() {
        assertNotNull("InputDialog class should exist", Dialogs.InputDialog.class);
    }

    @Test
    public void testDetailsDialogClassExists() {
        assertNotNull("DetailsDialog class should exist", Dialogs.DetailsDialog.class);
    }
}
