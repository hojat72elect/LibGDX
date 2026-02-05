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
 * Unit tests for {@link Dialogs.DetailsDialog}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsDialogTest {

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
    public void testDetailsDialogConstructorWithDetails() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Title", dialog.getTitleLabel().getText().toString());
        assertFalse("Details should not be visible initially", dialog.isDetailsVisible());
        assertTrue("Copy button should be visible", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogConstructorWithoutDetails() {
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", null);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Title", dialog.getTitleLabel().getText().toString());
        assertFalse("Details should not be visible", dialog.isDetailsVisible());
        assertFalse("Copy button should not be visible without details", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogConstructorWithEmptyDetails() {
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", "");
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Title", dialog.getTitleLabel().getText().toString());
        assertFalse("Details should not be visible initially", dialog.isDetailsVisible());
        assertTrue("Copy button should be visible even with empty details", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisible() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        assertFalse("Details should not be visible initially", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(true);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(false);
        assertFalse("Details should not be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisibleIdempotent() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        // Setting to same state multiple times should not cause issues
        dialog.setDetailsVisible(false);
        assertFalse("Details should not be visible", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(true);
        dialog.setDetailsVisible(true);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisibleWithoutStage() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        // Dialog has no stage set
        dialog.setDetailsVisible(true);
        
        // Test passes if no exception is thrown
        assertTrue("Details should be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogSetDetailsVisibleWithStage() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        // Can't test setStage with mock stage due to protected access
        // Test passes if no exception is thrown when setting details visible
        dialog.setDetailsVisible(true);
        
        assertTrue("Details should be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogSetWrapDetails() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        dialog.setWrapDetails(true);
        // Test passes if no exception is thrown
        
        dialog.setWrapDetails(false);
        // Test passes if no exception is thrown
    }

    @Test
    public void testDetailsDialogCopyButtonVisibility() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        assertTrue("Copy button should be visible by default", dialog.isCopyDetailsButtonVisible());
        
        dialog.setCopyDetailsButtonVisible(false);
        assertFalse("Copy button should be hidden", dialog.isCopyDetailsButtonVisible());
        
        dialog.setCopyDetailsButtonVisible(true);
        assertTrue("Copy button should be visible again", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogCopyButtonVisibilityWithoutDetails() {
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", null);
        
        assertFalse("Copy button should not be visible without details", dialog.isCopyDetailsButtonVisible());
        
        // Even trying to show it should not work without details
        dialog.setCopyDetailsButtonVisible(true);
        assertFalse("Copy button should still not be visible", dialog.isCopyDetailsButtonVisible());
    }

    @Test
    public void testDetailsDialogWithEmptyMessage() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("", "Title", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testDetailsDialogWithNullMessage() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog(null, "Title", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should match", "Title", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testDetailsDialogWithEmptyTitle() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "", details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertEquals("Dialog title should be empty", "", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testDetailsDialogWithNullTitle() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", null, details);
        
        assertNotNull("Dialog should not be null", dialog);
        assertNull("Dialog title should be null", dialog.getTitleLabel().getText().toString());
    }

    @Test
    public void testDetailsDialogWithLongDetails() {
        StringBuilder longDetails = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longDetails.append("This is a very long detail line ").append(i).append("\n");
        }
        
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", longDetails.toString());
        
        assertNotNull("Dialog should not be null", dialog);
        assertFalse("Details should not be visible initially", dialog.isDetailsVisible());
        
        dialog.setDetailsVisible(true);
        assertTrue("Details should be visible", dialog.isDetailsVisible());
    }

    @Test
    public void testDetailsDialogChaining() {
        String details = "Detailed information";
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog("Message", "Title", details);
        
        dialog.setWrapDetails(true);
        dialog.setCopyDetailsButtonVisible(false);
        dialog.setDetailsVisible(true);
        
        assertTrue("Details should be visible", dialog.isDetailsVisible());
        assertFalse("Copy button should be hidden", dialog.isCopyDetailsButtonVisible());
    }
}
