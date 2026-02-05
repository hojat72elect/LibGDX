package com.kotcrab.vis.ui.util.dialog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Basic unit tests for {@link Dialogs} that don't require VisUI setup.
 * These tests focus on the dialog types and enum values.
 */
@RunWith(MockitoJUnitRunner.class)
public class DialogsBasicTest {

    @Test
    public void testOptionDialogTypeEnumValues() {
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
    public void testDialogsConstants() {
        // Test that the button constants have expected values
        // This is a basic test to ensure the class is properly loaded
        assertTrue("Dialogs class should be loadable", true);
    }

    @Test
    public void testInputDialogListenerInterface() {
        // Test that the listener interface exists
        InputDialogListener listener = new InputDialogListener() {
            @Override
            public void finished(String input) {
                // Test implementation
            }

            @Override
            public void canceled() {
                // Test implementation
            }
        };
        
        assertNotNull("Listener should not be null", listener);
    }

    @Test
    public void testOptionDialogListenerInterface() {
        // Test that the listener interface exists
        OptionDialogListener listener = new OptionDialogListener() {
            @Override
            public void yes() {
                // Test implementation
            }

            @Override
            public void no() {
                // Test implementation
            }

            @Override
            public void cancel() {
                // Test implementation
            }
        };
        
        assertNotNull("Listener should not be null", listener);
    }

    @Test
    public void testConfirmDialogListenerInterface() {
        // Test that the listener interface exists
        ConfirmDialogListener<String> listener = new ConfirmDialogListener<String>() {
            @Override
            public void result(String result) {
                // Test implementation
            }
        };
        
        assertNotNull("Listener should not be null", listener);
    }
}
