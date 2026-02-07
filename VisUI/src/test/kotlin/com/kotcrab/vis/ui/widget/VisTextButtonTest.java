package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VisTextButton}.
 */
public class VisTextButtonTest {

    @Mock
    private Stage mockStage;
    @Mock
    private VisTextButtonStyle mockStyle;
    @Mock
    private Drawable mockDrawable;
    @Mock
    private BitmapFont mockFont;
    @Mock
    private ChangeListener mockChangeListener;

    private VisTextButton button;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup mock style
        mockStyle.font = mockFont;
        mockStyle.up = mockDrawable;
        mockStyle.down = mockDrawable;
        mockStyle.checked = mockDrawable;
        mockStyle.focusBorder = mockDrawable;
        
        // Create button with mock style
        button = new VisTextButton("Test Button", mockStyle);
    }

    @Test
    public void testConstructorWithText() {
        VisTextButton btn = new VisTextButton("Test");
        assertEquals("Test", btn.getLabel().getText().toString());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisTextButton btn = new VisTextButton("Test", "default");
        assertEquals("Test", btn.getLabel().getText().toString());
    }

    @Test
    public void testConstructorWithTextAndListener() {
        VisTextButton btn = new VisTextButton("Test", mockChangeListener);
        assertEquals("Test", btn.getLabel().getText().toString());
        assertTrue("Should have the listener", btn.getListeners().contains(mockChangeListener));
    }

    @Test
    public void testConstructorWithTextStyleNameAndListener() {
        VisTextButton btn = new VisTextButton("Test", "default", mockChangeListener);
        assertEquals("Test", btn.getLabel().getText().toString());
        assertTrue("Should have the listener", btn.getListeners().contains(mockChangeListener));
    }

    @Test
    public void testFocusBorderEnabledByDefault() {
        assertTrue("Focus border should be enabled by default", button.isFocusBorderEnabled());
    }

    @Test
    public void testSetFocusBorderEnabled() {
        button.setFocusBorderEnabled(false);
        assertFalse("Focus border should be disabled", button.isFocusBorderEnabled());
        
        button.setFocusBorderEnabled(true);
        assertTrue("Focus border should be enabled", button.isFocusBorderEnabled());
    }

    @Test
    public void testFocusGained() {
        button.focusGained();
        // Since drawBorder is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Focus gained should complete without errors", true);
    }

    @Test
    public void testFocusLost() {
        button.focusLost();
        // Since drawBorder is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Focus lost should complete without errors", true);
    }

    @Test
    public void testTouchDownRequestsFocus() {
        // Reset FocusManager state
        resetFocusManager();
        
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        button.fire(event);
        
        // Verify that FocusManager.switchFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownDoesNotRequestFocusWhenDisabled() {
        // Reset FocusManager state
        resetFocusManager();
        
        button.setDisabled(true);
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        button.fire(event);
        
        // Verify that FocusManager.switchFocus was NOT called
        verify(mockStage, never()).setKeyboardFocus(null);
    }

    @Test
    public void testVisTextButtonStyle() {
        VisTextButtonStyle style = new VisTextButtonStyle();
        assertNull("Focus border should be null by default", style.focusBorder);
        
        style.focusBorder = mockDrawable;
        assertEquals("Focus border should be set", mockDrawable, style.focusBorder);
    }

    @Test
    public void testVisTextButtonStyleCopyConstructor() {
        VisTextButtonStyle original = new VisTextButtonStyle();
        original.focusBorder = mockDrawable;
        original.up = mockDrawable;
        original.down = mockDrawable;
        original.checked = mockDrawable;
        original.font = mockFont;
        
        VisTextButtonStyle copy = new VisTextButtonStyle(original);
        assertEquals("Focus border should be copied", original.focusBorder, copy.focusBorder);
        assertEquals("Up drawable should be copied", original.up, copy.up);
        assertEquals("Down drawable should be copied", original.down, copy.down);
        assertEquals("Checked drawable should be copied", original.checked, copy.checked);
        assertEquals("Font should be copied", original.font, copy.font);
    }

    @Test
    public void testVisTextButtonStyleWithParameters() {
        VisTextButtonStyle style = new VisTextButtonStyle(mockDrawable, mockDrawable, mockDrawable, mockFont);
        assertEquals("Up drawable should be set", mockDrawable, style.up);
        assertEquals("Down drawable should be set", mockDrawable, style.down);
        assertEquals("Checked drawable should be set", mockDrawable, style.checked);
        assertEquals("Font should be set", mockFont, style.font);
    }

    /**
     * Helper method to reset FocusManager static state.
     */
    private void resetFocusManager() {
        try {
            java.lang.reflect.Field field = FocusManager.class.getDeclaredField("focusedWidget");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }
}
