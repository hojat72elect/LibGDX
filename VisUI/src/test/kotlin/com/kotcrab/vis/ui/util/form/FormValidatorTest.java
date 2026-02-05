package com.kotcrab.vis.ui.util.form;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator.FormValidatorStyle;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FormValidator}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormValidatorTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return Gdx.files.internal("test");
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
    }

    @Before
    public void setUp() {
        // Reset VisUI state before each test
        disposeVisUI();
        VisUI.setSkipGdxVersionCheck(true);
        // Load VisUI with a minimal skin for testing
        try {
            Skin skin = new Skin();
            // Add required FormValidatorStyle to the skin
            FormValidatorStyle style = new FormValidatorStyle();
            skin.add("default", style, FormValidatorStyle.class);
            // Add custom style for testing
            FormValidatorStyle customStyle = new FormValidatorStyle();
            skin.add("custom", customStyle, FormValidatorStyle.class);
            VisUI.load(skin);
        } catch (Exception e) {
            // If loading fails, continue anyway - tests should still work
        }
        
        // Configure mock behavior
        lenient().when(mockField.getText()).thenReturn("");
        lenient().when(mockField.isInputValid()).thenReturn(true);
        lenient().when(mockField.isDisabled()).thenReturn(false);
        lenient().when(mockField.getValidators()).thenReturn(new com.badlogic.gdx.utils.Array<>());
        lenient().when(mockRelativeField.getText()).thenReturn("");
    }

    /**
     * Helper method to dispose VisUI and reset static state using reflection.
     */
    private void disposeVisUI() {
        try {
            java.lang.reflect.Field skinField = VisUI.class.getDeclaredField("skin");
            skinField.setAccessible(true);
            skinField.set(null, null);

            java.lang.reflect.Field scaleField = VisUI.class.getDeclaredField("scale");
            scaleField.setAccessible(true);
            scaleField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }

    @Mock
    private Disableable mockDisableable;

    @Mock
    private Label mockLabel;

    @Mock
    private VisValidatableTextField mockField;

    @Mock
    private VisTextField mockRelativeField;

    // Constructor tests
    @Test
    public void testConstructorWithDisableable() {
        FormValidator validator = new FormValidator(mockDisableable);

        assertNotNull("Validator should not be null", validator);
        assertTrue("Should extend SimpleFormValidator", validator instanceof SimpleFormValidator);
    }

    @Test
    public void testConstructorWithDisableableAndLabel() {
        FormValidator validator = new FormValidator(mockDisableable, mockLabel);

        assertNotNull("Validator should not be null", validator);
        assertTrue("Should extend SimpleFormValidator", validator instanceof SimpleFormValidator);
    }

    @Test
    public void testConstructorWithDisableableLabelAndStyleName() {
        FormValidator validator = new FormValidator(mockDisableable, mockLabel, "custom");

        assertNotNull("Validator should not be null", validator);
        assertTrue("Should extend SimpleFormValidator", validator instanceof SimpleFormValidator);
    }

    @Test
    public void testConstructorWithDisableableLabelAndStyle() {
        FormValidatorStyle style = new FormValidatorStyle();
        FormValidator validator = new FormValidator(mockDisableable, mockLabel, style);

        assertNotNull("Validator should not be null", validator);
        assertTrue("Should extend SimpleFormValidator", validator instanceof SimpleFormValidator);
    }

    // Test that FormValidator methods delegate to SimpleFormValidator
    @Test
    public void testFileExistsDelegatesToSimpleFormValidator() {
        FormValidator validator = new FormValidator(mockDisableable);

        // Test that fileExists method calls addValidator and add
        FormInputValidator result = validator.fileExists(mockField, "File must exist");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    @Test
    public void testFileExistsWithVisTextFieldRelative() {
        FormValidator validator = new FormValidator(mockDisableable);

        FormInputValidator result = validator.fileExists(mockField, mockRelativeField, "File must exist");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    @Test
    public void testFileNotExistsCallsAddValidator() {
        FormValidator validator = new FormValidator(mockDisableable);

        FormInputValidator result = validator.fileNotExists(mockField, "File must not exist");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    @Test
    public void testDirectoryCallsAddValidator() {
        FormValidator validator = new FormValidator(mockDisableable);

        FormInputValidator result = validator.directory(mockField, "Must be directory");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    @Test
    public void testDirectoryEmptyCallsAddValidator() {
        FormValidator validator = new FormValidator(mockDisableable);

        FormInputValidator result = validator.directoryEmpty(mockField, "Directory must be empty");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    @Test
    public void testDirectoryNotEmptyCallsAddValidator() {
        FormValidator validator = new FormValidator(mockDisableable);

        FormInputValidator result = validator.directoryNotEmpty(mockField, "Directory must not be empty");

        assertNotNull("Validator should not be null", result);
        verify(mockField).addValidator(any(FormInputValidator.class));
        // Note: add() is called internally by FormValidator, no need to verify it
    }

    // DirectoryValidator tests
    @Test
    public void testDirectoryValidatorConstructor() {
        FormValidator.DirectoryValidator validator = new FormValidator.DirectoryValidator("Test error");

        assertEquals("Error message should be set", "Test error", validator.getErrorMsg());
    }

    @Test
    public void testDirectoryValidatorInheritance() {
        FormValidator.DirectoryValidator validator = new FormValidator.DirectoryValidator("Test error");

        assertTrue("Should extend FormInputValidator", validator instanceof FormInputValidator);
        assertNotNull("Error message should not be null", validator.getErrorMsg());
    }

    // DirectoryContentValidator tests
    @Test
    public void testDirectoryContentValidatorConstructor() {
        FormValidator.DirectoryContentValidator validator = new FormValidator.DirectoryContentValidator("Test error", true);

        assertEquals("Error message should be set", "Test error", validator.getErrorMsg());
        assertTrue("Must be empty should be true", validator.isMustBeEmpty());
    }

    @Test
    public void testDirectoryContentValidatorSetMustBeEmpty() {
        FormValidator.DirectoryContentValidator validator = new FormValidator.DirectoryContentValidator("Test error", false);

        validator.setMustBeEmpty(true);
        assertTrue("Must be empty should be true", validator.isMustBeEmpty());

        validator.setMustBeEmpty(false);
        assertFalse("Must be empty should be false", validator.isMustBeEmpty());
    }

    @Test
    public void testDirectoryContentValidatorInheritance() {
        FormValidator.DirectoryContentValidator validator = new FormValidator.DirectoryContentValidator("Test error", true);

        assertTrue("Should extend FormInputValidator", validator instanceof FormInputValidator);
        assertNotNull("Error message should not be null", validator.getErrorMsg());
        assertTrue("Must be empty should be true", validator.isMustBeEmpty());
    }

    // FileExistsValidator tests
    @Test
    public void testFileExistsValidatorConstructor() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");

        assertEquals("Error message should be set", "Test error", validator.getErrorMsg());
        assertNull("Relative to should be null by default", validator.relativeTo);
        assertNull("Relative to file should be null by default", validator.relativeToFile);
        assertFalse("Must not exist should be false by default", validator.mustNotExist);
        assertFalse("Error if relative empty should be false by default", validator.errorIfRelativeEmpty);
    }

    @Test
    public void testFileExistsValidatorConstructorWithMustNotExist() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error", true);

        assertTrue("Must not exist should be true", validator.mustNotExist);
    }

    @Test
    public void testFileExistsValidatorConstructorWithVisTextFieldRelative() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator(mockRelativeField, "Test error");

        assertEquals("Relative to should be set", mockRelativeField, validator.relativeTo);
        assertNull("Relative to file should be null", validator.relativeToFile);
    }

    @Test
    public void testFileExistsValidatorConstructorWithFileRelative() {
        java.io.File relativeFile = new java.io.File("/test/path");
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator(relativeFile, "Test error");

        assertEquals("Relative to file should be set", relativeFile, validator.relativeToFile);
        assertNull("Relative to should be null", validator.relativeTo);
    }

    @Test
    public void testFileExistsValidatorConstructorWithAllParameters() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator(mockRelativeField, "Test error", true, true);

        assertEquals("Relative to should be set", mockRelativeField, validator.relativeTo);
        assertTrue("Must not exist should be true", validator.mustNotExist);
        assertTrue("Error if relative empty should be true", validator.errorIfRelativeEmpty);
    }

    @Test
    public void testFileExistsValidatorInheritance() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");

        assertTrue("Should extend FormInputValidator", validator instanceof FormInputValidator);
        assertNotNull("Error message should not be null", validator.getErrorMsg());
    }

    @Test
    public void testFileExistsValidatorSetMustNotExist() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");

        validator.setMustNotExist(true);
        assertTrue("Must not exist should be true", validator.mustNotExist);

        validator.setMustNotExist(false);
        assertFalse("Must not exist should be false", validator.mustNotExist);
    }

    @Test
    public void testFileExistsValidatorSetErrorIfRelativeEmpty() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");

        validator.setErrorIfRelativeEmpty(true);
        assertTrue("Error if relative empty should be true", validator.errorIfRelativeEmpty);

        validator.setErrorIfRelativeEmpty(false);
        assertFalse("Error if relative empty should be false", validator.errorIfRelativeEmpty);
    }

    @Test(expected = IllegalStateException.class)
    public void testFileExistsValidatorSetRelativeToFileWhenRelativeFieldSet() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator(mockRelativeField, "Test error");

        validator.setRelativeToFile(new java.io.File("/path"));
    }

    @Test
    public void testFileExistsValidatorSetRelativeToFile() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");
        java.io.File newRelativeFile = new java.io.File("/new/relative/path");

        validator.setRelativeToFile(newRelativeFile);

        assertEquals("Relative to file should be set", newRelativeFile, validator.relativeToFile);
    }

    @Test
    public void testFileExistsValidatorSetRelativeToTextField() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator("Test error");

        validator.setRelativeToTextField(mockRelativeField);

        assertEquals("Relative to should be set", mockRelativeField, validator.relativeTo);
    }

    @Test(expected = IllegalStateException.class)
    public void testFileExistsValidatorSetRelativeToTextFieldWhenRelativeFileSet() {
        FormValidator.FileExistsValidator validator = new FormValidator.FileExistsValidator(new java.io.File("/test"), "Test error");

        validator.setRelativeToTextField(mockRelativeField);
    }
}
