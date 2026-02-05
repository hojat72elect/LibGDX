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
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Dialogs.ConfirmDialog}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfirmDialogTest {

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
    public void testConfirmDialogConstructor() {
        String[] buttons = {"Button1", "Button2", "Button3"};
        String[] returns = {"Return1", "Return2", "Return3"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
        assertTrue("Dialog should be modal", dialog.isModal());
    }

    @Test(expected = IllegalStateException.class)
    public void testConfirmDialogConstructorWithMismatchedArrays() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1"}; // Mismatched length
        ConfirmDialogListener<String> mockListener = result -> {};
        
        new Dialogs.ConfirmDialog<>("Test Title", "Test Message", buttons, returns, mockListener);
    }

    @Test(expected = IllegalStateException.class)
    public void testConfirmDialogConstructorWithEmptyArrays() {
        String[] buttons = {};
        String[] returns = {};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        new Dialogs.ConfirmDialog<>("Test Title", "Test Message", buttons, returns, mockListener);
    }

    @Test
    public void testConfirmDialogConstructorWithSingleButton() {
        String[] buttons = {"OK"};
        String[] returns = {"OK_RESULT"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogConstructorWithNullListener() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, null);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithEmptyMessage() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithNullMessage() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                null, buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithEmptyTitle() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should be empty", "", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithNullTitle() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>(null, 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertNull("Dialog title should be null", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithNullButtons() {
        String[] buttons = {null, "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithNullReturns() {
        String[] buttons = {"Button1", "Button2"};
        String[] returns = {"Return1", null};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithEmptyButtonStrings() {
        String[] buttons = {"", "Button2"};
        String[] returns = {"Return1", "Return2"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithManyButtons() {
        String[] buttons = {"Button1", "Button2", "Button3", "Button4", "Button5"};
        String[] returns = {"Return1", "Return2", "Return3", "Return4", "Return5"};
        ConfirmDialogListener<String> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<String> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithIntegerReturns() {
        String[] buttons = {"Button1", "Button2", "Button3"};
        Integer[] returns = {1, 2, 3};
        ConfirmDialogListener<Integer> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<Integer> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testConfirmDialogWithEnumReturns() {
        String[] buttons = {"Option1", "Option2"};
        TestEnum[] returns = {TestEnum.VALUE1, TestEnum.VALUE2};
        ConfirmDialogListener<TestEnum> mockListener = result -> {};
        
        Dialogs.ConfirmDialog<TestEnum> dialog = new Dialogs.ConfirmDialog<>("Test Title", 
                "Test Message", buttons, returns, mockListener);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Test Title", dialog.getTitleLabel().getText().toString());
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }
}
