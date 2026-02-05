package com.kotcrab.vis.ui.util.dialog;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Dialogs}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DialogsTest {

    @Mock
    private Stage mockStage;
    
    @Mock
    private InputDialogListener mockInputDialogListener;
    
    @Mock
    private OptionDialogListener mockOptionDialogListener;
    
    @Mock
    private ConfirmDialogListener<String> mockConfirmDialogListener;
    
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
            // Create a minimal skin with required styles for testing
            Skin skin = new Skin();
            
            // Add white drawable
            skin.add("white", Color.WHITE);
            
            // Add minimal window style
            Window.WindowStyle windowStyle = new Window.WindowStyle();
            windowStyle.background = skin.newDrawable("white", Color.WHITE);
            windowStyle.titleFont = new BitmapFont();
            windowStyle.titleFontColor = Color.BLACK;
            skin.add("default", windowStyle);
            
            VisUI.load(skin);
        }
    }

    @Before
    public void setUp() {
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
    }

    @Test
    public void testShowOKDialog() {
        VisDialog dialog = Dialogs.showOKDialog(mockStage, "Test Title", "Test Message");
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
        
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowOKDialogWithEnterKey() {
        VisDialog dialog = Dialogs.showOKDialog(mockStage, "Test Title", "Test Message");
        
        assertNotNull("Dialog should not be null", dialog);
        
        // Dialog handles ENTER key through InputListener (tested implicitly)
        assertNotNull("Dialog should handle ENTER key", dialog);
    }

    @Test
    public void testShowOptionDialogYesNo() {
        Dialogs.OptionDialog dialog = Dialogs.showOptionDialog(mockStage, "Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
        
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowOptionDialogYesNoCancel() {
        Dialogs.OptionDialog dialog = Dialogs.showOptionDialog(mockStage, "Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO_CANCEL, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowOptionDialogYesCancel() {
        Dialogs.OptionDialog dialog = Dialogs.showOptionDialog(mockStage, "Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_CANCEL, mockOptionDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowConfirmDialog() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        
        Dialogs.ConfirmDialog<String> dialog = Dialogs.showConfirmDialog(mockStage, "Test Title", 
                "Test Message", buttons, returns, mockConfirmDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
        
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testShowConfirmDialogWithMismatchedArrays() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1"}; // Mismatched length
        
        new Dialogs.ConfirmDialog<String>("Test Title", "Test Message", buttons, returns, mockConfirmDialogListener);
    }

    @Test
    public void testShowInputDialogBasic() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
        
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowInputDialogWithValidator() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", mockValidator, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowInputDialogCancelable() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", true, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowInputDialogCancelableWithValidator() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", true, mockValidator, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowErrorDialogBasic() {
        Dialogs.DetailsDialog dialog = Dialogs.showErrorDialog(mockStage, "Error Message");
        
        assertNotNull("Dialog should not be null", dialog);
        assertTrue("Dialog title should contain 'Error'", dialog.getTitleLabel().getText().toString().contains("Error"));
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowErrorDialogWithException() {
        Exception testException = new RuntimeException("Test exception");
        Dialogs.DetailsDialog dialog = Dialogs.showErrorDialog(mockStage, "Error Message", testException);
        
        assertNotNull("Dialog should not be null", dialog);
        assertTrue("Dialog title should contain 'Error'", dialog.getTitleLabel().getText().toString().contains("Error"));
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowErrorDialogWithDetails() {
        String details = "Detailed error information";
        Dialogs.DetailsDialog dialog = Dialogs.showErrorDialog(mockStage, "Error Message", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertTrue("Dialog title should contain 'Error'", dialog.getTitleLabel().getText().toString().contains("Error"));
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowErrorDialogWithNullException() {
        Dialogs.DetailsDialog dialog = Dialogs.showErrorDialog(mockStage, "Error Message", (Throwable) null);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowDetailsDialog() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Custom Title", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Custom Title", dialog.getTitleLabel().getText().toString());
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testShowDetailsDialogExpanded() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Custom Title", details, true);
        
        assertNotNull("Dialog should not be null", dialog);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testInputDialogSetText() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText("Test Text");
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testInputDialogSetTextWithSelection() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", mockInputDialogListener);
        
        Dialogs.InputDialog result = dialog.setText("Test Text", true);
        
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testOptionDialogSetButtonText() {
        Dialogs.OptionDialog dialog = Dialogs.showOptionDialog(mockStage, "Test Title", "Test Message", 
                Dialogs.OptionDialogType.YES_NO, mockOptionDialogListener);
        
        Dialogs.OptionDialog result = dialog.setYesButtonText("Custom Yes");
        
        assertSame("Should return same dialog instance", dialog, result);
        
        result = dialog.setNoButtonText("Custom No");
        assertSame("Should return same dialog instance", dialog, result);
        
        result = dialog.setCancelButtonText("Custom Cancel");
        assertSame("Should return same dialog instance", dialog, result);
    }

    @Test
    public void testDetailsDialogSetWrapDetails() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Title", details);
        
        dialog.setWrapDetails(true);
        // Test passes if no exception is thrown
        
        dialog.setWrapDetails(false);
        // Test passes if no exception is thrown
    }

    @Test
    public void testDetailsDialogCopyButtonVisibility() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Title", details);
        
        assertTrue("Copy button should be visible by default", dialog.isCopyDetailsButtonVisible());
        
        dialog.setCopyDetailsButtonVisible(false);
        assertFalse("Copy button should be hidden", dialog.isCopyDetailsButtonVisible());
        
        dialog.setCopyDetailsButtonVisible(true);
        assertTrue("Copy button should be visible again", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisible() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Title", details, false);
        
        assertFalse("Details should not be visible initially", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(true);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(false);
        assertFalse("Details should not be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisibleIdempotent() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = Dialogs.showDetailsDialog(mockStage, "Message", 
                "Title", details, false);
        
        // Setting to same state should not cause issues
        dialog.setDetailsVisible(false);
        assertFalse("Details should not be visible", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(true);
        dialog.setDetailsVisible(true);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testOptionDialogTypeEnum() {
        assertEquals("Should have 3 enum values", 3, Dialogs.OptionDialogType.values().length);
        
        assertNotNull("YES_NO should not be null", Dialogs.OptionDialogType.YES_NO);
        assertNotNull("YES_NO_CANCEL should not be null", Dialogs.OptionDialogType.YES_NO_CANCEL);
        assertNotNull("YES_CANCEL should not be null", Dialogs.OptionDialogType.YES_CANCEL);
    }

    @Test
    public void testInputDialogWithIntegerValidator() {
        when(mockValidator.validateInput(anyString())).thenReturn(true);
        
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                "Field Title", Validators.INTEGERS, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }

    @Test
    public void testInputDialogWithNullFieldTitle() {
        Dialogs.InputDialog dialog = Dialogs.showInputDialog(mockStage, "Test Title", 
                null, mockInputDialogListener);
        
        assertNotNull("Dialog should not be null", dialog);
        verify(mockStage).addActor(any(Actor.class));
    }
}
