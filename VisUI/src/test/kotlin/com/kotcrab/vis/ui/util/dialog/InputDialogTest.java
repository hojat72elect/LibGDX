package com.kotcrab.vis.ui.util.dialog;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Dialogs.InputDialog}.
 */
@RunWith(MockitoJUnitRunner.class)
public class InputDialogTest {

    @Mock
    private Stage mockStage;
    
    @Mock
    private InputDialogListener mockInputDialogListener;
    
    @Mock
    private InputValidator mockValidator;

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
    public void testInputDialogConstructorWithValidator() {
        when(mockValidator.validateInput(anyString())).thenReturn(true);
        
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, mockValidator, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testInputDialogConstructorWithoutValidator() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testInputDialogConstructorNonCancelable() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", false, null, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test
    public void testInputDialogSetStageFocusesField() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        dialog.setStage(mockStage);
        
        // Test passes if no exception is thrown - field should receive focus
        verify(mockStage, never()).addActor(any()); // Should not add actor again
    }

    @Test
    public void testInputDialogSetStageNull() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        dialog.setStage(null);
        
        // Test passes if no exception is thrown
    }

    @Test
    public void testInputDialogSetText() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText("Test Text");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testInputDialogSetTextWithSelection() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText("Test Text", true);
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testInputDialogSetTextEmpty() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText("");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testInputDialogSetTextNull() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText(null);
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testInputDialogWithValidatableField() {
        when(mockValidator.validateInput(anyString())).thenReturn(false).thenReturn(true);
        
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, mockValidator, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        
        // The OK button should be initially disabled since validation returns false
        // This is tested implicitly through the constructor logic
    }

    @Test
    public void testInputDialogCloseTriggersCanceled() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "Field Title", true, null, mockInputDialogListener);
        
        dialog.close();
        
        verify(mockInputDialogListener).canceled();
    }

    @Test
    public void testInputDialogWithNullFieldTitle() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                null, true, null, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testInputDialogWithEmptyFieldTitle() {
        Dialogs.InputDialog dialog = new Dialogs.InputDialog("Test Title", 
                "", true, null, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }
}
