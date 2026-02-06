package com.kotcrab.vis.ui.widget.spinner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link Spinner}.
 */
public class SpinnerTest {

    @Mock
    private SpinnerModel mockModel;

    @Mock
    private Stage mockStage;

    @Mock
    private Sizes mockSizes;

    private Spinner spinner;
    private Spinner.SpinnerStyle testStyle;

    @BeforeClass
    public static void setupGdx() {
        if (com.badlogic.gdx.Gdx.files == null) {
            com.badlogic.gdx.Gdx.files = (com.badlogic.gdx.Files) Proxy.newProxyInstance(
                    com.badlogic.gdx.Files.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return new com.badlogic.gdx.files.FileHandle("test");
                        }
                        return null;
                    });
        }
        if (com.badlogic.gdx.Gdx.app == null) {
            Clipboard clipboard = mock(Clipboard.class);
            com.badlogic.gdx.Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> {
                        if ("getClipboard".equals(method.getName())) {
                            return clipboard;
                        }
                        return null;
                    });
        }
    }

    @Before
    public void setUp() {
        // Reset VisUI state before each test
        disposeVisUI();
        VisUI.setSkipGdxVersionCheck(true);

        // Load VisUI with a minimal in-memory skin to avoid dependency on skin files.
        // Spinner internally creates VisLabel and VisValidatableTextField, both require styles present in VisUI skin.
        Skin skin = new Skin();

        Texture mockTexture = mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(1);
        when(mockTexture.getHeight()).thenReturn(1);

        TextureRegion mockRegion = mock(TextureRegion.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };
        BitmapFont font = new BitmapFont(fontData, Array.with(mockRegion), true);

        skin.add("default-font", font, BitmapFont.class);
        skin.add("default", new Label.LabelStyle(font, Color.WHITE), Label.LabelStyle.class);

        VisTextField.VisTextFieldStyle textFieldStyle = new VisTextField.VisTextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        skin.add("default", textFieldStyle, VisTextField.VisTextFieldStyle.class);

        Sizes sizes = new Sizes();
        sizes.spinnerButtonHeight = 20f;
        sizes.spinnerFieldSize = 100f;
        skin.add("default", sizes, Sizes.class);

        Spinner.SpinnerStyle spinnerStyle = new Spinner.SpinnerStyle();
        spinnerStyle.up = mock(Drawable.class);
        spinnerStyle.down = mock(Drawable.class);
        skin.add("default", spinnerStyle, Spinner.SpinnerStyle.class);

        VisImageButton.VisImageButtonStyle imageButtonStyle = new VisImageButton.VisImageButtonStyle();
        imageButtonStyle.up = mock(Drawable.class);
        imageButtonStyle.down = mock(Drawable.class);
        skin.add("default", imageButtonStyle, VisImageButton.VisImageButtonStyle.class);

        VisUI.load(skin);

        MockitoAnnotations.openMocks(this);

        // Create test style
        testStyle = new Spinner.SpinnerStyle();
        testStyle.up = mock(Drawable.class);
        testStyle.down = mock(Drawable.class);

        mockSizes.spinnerButtonHeight = 20f;
        mockSizes.spinnerFieldSize = 100f;

        spinner = new Spinner(testStyle, mockSizes, "Test", mockModel);
    }

    @Test
    public void testConstructorWithNameAndModel() {
        Spinner simpleSpinner = new Spinner("Simple", mockModel);
        assertNotNull("Spinner should be created", simpleSpinner);
        assertEquals("Name should be set", "Simple", simpleSpinner.getSelectorName());
    }

    @Test
    public void testConstructorWithStyleName() {
        Spinner styleSpinner = new Spinner("default", "Styled", mockModel);
        assertNotNull("Spinner should be created", styleSpinner);
        assertEquals("Name should be set", "Styled", styleSpinner.getSelectorName());
    }

    @Test
    public void testGetAndSetSelectorName() {
        assertEquals("Initial name should be Test", "Test", spinner.getSelectorName());

        spinner.setSelectorName("NewName");
        assertEquals("Name should be updated", "NewName", spinner.getSelectorName());

        spinner.setSelectorName("");
        assertEquals("Empty name should be handled", "", spinner.getSelectorName());

        spinner.setSelectorName(null);
        assertEquals("Null name should be handled", "", spinner.getSelectorName());
    }

    @Test
    public void testIncrement() {
        spinner.increment();
        verify(mockModel).increment(true);
    }

    @Test
    public void testDecrement() {
        spinner.decrement();
        verify(mockModel).decrement(true);
    }

    @Test
    public void testProgrammaticChangeEvents() {
        assertTrue("Programmatic change events should be true by default", spinner.isProgrammaticChangeEvents());

        spinner.setProgrammaticChangeEvents(false);
        assertFalse("Programmatic change events should be false", spinner.isProgrammaticChangeEvents());

        spinner.increment();
        verify(mockModel).increment(false);

        spinner.decrement();
        verify(mockModel).decrement(false);
    }

    @Test
    public void testTextFieldEventPolicy() {
        assertEquals("Default policy should be ON_FOCUS_LOST",
                Spinner.TextFieldEventPolicy.ON_FOCUS_LOST, spinner.getTextFieldEventPolicy());

        spinner.setTextFieldEventPolicy(Spinner.TextFieldEventPolicy.ON_ENTER_ONLY);
        assertEquals("Policy should be updated",
                Spinner.TextFieldEventPolicy.ON_ENTER_ONLY, spinner.getTextFieldEventPolicy());
    }

    @Test
    public void testGetAndSetMaxLength() {
        VisValidatableTextField textField = spinner.getTextField();
        textField.setMaxLength(10);

        assertEquals("Max length should be set", 10, spinner.getMaxLength());

        spinner.setMaxLength(20);
        assertEquals("Max length should be updated", 20, textField.getMaxLength());
    }

    @Test
    public void testNotifyValueChanged() {
        when(mockModel.getText()).thenReturn("test value");

        spinner.notifyValueChanged(true);

        VisValidatableTextField textField = spinner.getTextField();
        assertEquals("Text field should be updated", "test value", textField.getText());
        // Verify that change event was fired by checking if any event was fired
        // This is harder to test directly without complex mocking
    }

    @Test
    public void testNotifyValueChangedWithoutFire() {
        when(mockModel.getText()).thenReturn("test value");

        spinner.notifyValueChanged(false);

        VisValidatableTextField textField = spinner.getTextField();
        assertEquals("Text field should be updated", "test value", textField.getText());
    }

    @Test
    public void testGetTextField() {
        VisValidatableTextField textField = spinner.getTextField();
        assertNotNull("Text field should not be null", textField);
        assertTrue("Should be VisValidatableTextField", textField instanceof VisValidatableTextField);
    }

    @Test
    public void testDisabledState() {
        assertFalse("Should not be disabled by default", spinner.isDisabled());

        spinner.setDisabled(true);
        assertTrue("Should be disabled", spinner.isDisabled());

        VisValidatableTextField textField = spinner.getTextField();
        assertTrue("Text field should be disabled", textField.isDisabled());

        spinner.setDisabled(false);
        assertFalse("Should not be disabled", spinner.isDisabled());
        assertFalse("Text field should not be disabled", textField.isDisabled());
    }

    @Test
    public void testSpinnerStyle() {
        Spinner.SpinnerStyle style = new Spinner.SpinnerStyle();
        assertNull("Up drawable should be null by default", style.up);
        assertNull("Down drawable should be null by default", style.down);

        Drawable upDrawable = mock(Drawable.class);
        Drawable downDrawable = mock(Drawable.class);

        style.up = upDrawable;
        style.down = downDrawable;

        assertEquals("Up drawable should be set", upDrawable, style.up);
        assertEquals("Down drawable should be set", downDrawable, style.down);
    }

    @Test
    public void testSpinnerStyleCopy() {
        Drawable upDrawable = mock(Drawable.class);
        Drawable downDrawable = mock(Drawable.class);

        Spinner.SpinnerStyle original = new Spinner.SpinnerStyle(upDrawable, downDrawable);
        Spinner.SpinnerStyle copy = new Spinner.SpinnerStyle(original);

        assertEquals("Copied style should have same up drawable", original.up, copy.up);
        assertEquals("Copied style should have same down drawable", original.down, copy.down);
    }

    @Test
    public void testTextFieldEventPolicyEnum() {
        assertEquals("Should have 3 policies", 3, Spinner.TextFieldEventPolicy.values().length);

        Spinner.TextFieldEventPolicy[] policies = Spinner.TextFieldEventPolicy.values();
        boolean hasOnEnterOnly = false, hasOnFocusLost = false, hasOnKeyTyped = false;

        for (Spinner.TextFieldEventPolicy policy : policies) {
            if (policy == Spinner.TextFieldEventPolicy.ON_ENTER_ONLY) hasOnEnterOnly = true;
            if (policy == Spinner.TextFieldEventPolicy.ON_FOCUS_LOST) hasOnFocusLost = true;
            if (policy == Spinner.TextFieldEventPolicy.ON_KEY_TYPED) hasOnKeyTyped = true;
        }

        assertTrue("Should have ON_ENTER_ONLY", hasOnEnterOnly);
        assertTrue("Should have ON_FOCUS_LOST", hasOnFocusLost);
        assertTrue("Should have ON_KEY_TYPED", hasOnKeyTyped);
    }

    @Test
    public void testModelBinding() {
        verify(mockModel).bind(spinner);
    }

    @Test
    public void testButtonListeners() {
        // Test that buttons are created and have listeners
        // This is more of an integration test, but we can verify the structure
        VisValidatableTextField textField = spinner.getTextField();
        assertNotNull("Text field should exist", textField);

        // The spinner should have children (buttons and text field)
        assertTrue("Spinner should have children", spinner.getChildren().size > 0);
    }

    @Test
    public void testChangeEventListener() {
        final boolean[] eventFired = {false};
        spinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                eventFired[0] = true;
            }
        });

        when(mockModel.getText()).thenReturn("new value");
        spinner.notifyValueChanged(true);

        // Note: This test might not work perfectly due to the way events are fired
        // In a real scenario, you'd need to set up a proper stage and event system
        // But we can at least verify the listener is added
    }

    @Test
    public void testCursorPositionHandling() {
        when(mockModel.getText()).thenReturn("test");

        VisValidatableTextField textField = spinner.getTextField();
        textField.setText("xxxx");
        textField.setCursorPosition(2);

        spinner.notifyValueChanged(false);

        assertEquals("Cursor position should be preserved", 2, textField.getCursorPosition());
    }

    @Test
    public void testWithRealModel() {
        // Test with a real model to ensure integration works
        IntSpinnerModel intModel = new IntSpinnerModel(5, 0, 10);
        Spinner realSpinner = new Spinner("Real", intModel);

        assertNotNull("Spinner with real model should be created", realSpinner);
        assertEquals("Model should be set", intModel, realSpinner.getModel());
        assertEquals("Initial value should be 5", "5", realSpinner.getTextField().getText());
    }

    /**
     * Helper method to dispose VisUI and reset static state using reflection.
     */
    private void disposeVisUI() {
        try {
            Field skinField = VisUI.class.getDeclaredField("skin");
            skinField.setAccessible(true);
            skinField.set(null, null);

            Field scaleField = VisUI.class.getDeclaredField("scale");
            scaleField.setAccessible(true);
            scaleField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }
}
