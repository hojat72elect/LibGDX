package com.kotcrab.vis.ui.util.dialog;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Dialogs.OptionDialog}.
 */
@RunWith(MockitoJUnitRunner.class)
public class OptionDialogTest {

    @Mock
    private Stage mockStage;
    
    @Mock
    private OptionDialogListener mockOptionDialogListener;

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
        if (Gdx.app == null) {
            Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }
        if (!VisUI.isLoaded()) {
            VisUI.setSkipGdxVersionCheck(true);
            VisUI.load(VisUI.SkinScale.X1);
        }
    }

    @Before
    public void setUp() {
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
    }

    @Test
    public void testOptionDialogConstructorYesNo() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testOptionDialogConstructorYesNoCancel() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO_CANCEL, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testOptionDialogConstructorYesCancel() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_CANCEL, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testOptionDialogSetYesButtonText() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setYesButtonText("Custom Yes");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogSetNoButtonText() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setNoButtonText("Custom No");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogSetCancelButtonText() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO_CANCEL, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setCancelButtonText("Custom Cancel");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogSetButtonTextEmpty() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setYesButtonText("");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogSetButtonTextNull() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setYesButtonText(null);
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogWithEmptyMessage() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testOptionDialogWithNullMessage() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", null, 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testOptionDialogWithEmptyTitle() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should be empty", "", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testOptionDialogWithNullTitle() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog(null, "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertNull("Dialog title should be null", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testOptionDialogChaining() {
        Dialogs.OptionDialog dialog = new Dialogs.OptionDialog("Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO_CANCEL, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog
                .setYesButtonText("Custom Yes")
                .setNoButtonText("Custom No")
                .setCancelButtonText("Custom Cancel");
        
        assertSame("Should return same dialog instance for chaining", dialog, result);
    }
}
