package com.kotcrab.vis.ui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link Sizes}.
 */
public class SizesTest {

    @Test
    public void testDefaultConstructor() {
        Sizes sizes = new Sizes();

        // All fields should be initialized to default values (0 for primitives)
        assertEquals("scaleFactor should be 0", 0f, sizes.scaleFactor, 0.001f);
        assertEquals("spacingTop should be 0", 0f, sizes.spacingTop, 0.001f);
        assertEquals("spacingBottom should be 0", 0f, sizes.spacingBottom, 0.001f);
        assertEquals("spacingRight should be 0", 0f, sizes.spacingRight, 0.001f);
        assertEquals("spacingLeft should be 0", 0f, sizes.spacingLeft, 0.001f);
        assertEquals("buttonBarSpacing should be 0", 0f, sizes.buttonBarSpacing, 0.001f);
        assertEquals("menuItemIconSize should be 0", 0f, sizes.menuItemIconSize, 0.001f);
        assertEquals("borderSize should be 0", 0f, sizes.borderSize, 0.001f);
        assertEquals("spinnerButtonHeight should be 0", 0f, sizes.spinnerButtonHeight, 0.001f);
        assertEquals("spinnerFieldSize should be 0", 0f, sizes.spinnerFieldSize, 0.001f);
        assertEquals("fileChooserViewModeBigIconsSize should be 0", 0f, sizes.fileChooserViewModeBigIconsSize, 0.001f);
        assertEquals("fileChooserViewModeMediumIconsSize should be 0", 0f, sizes.fileChooserViewModeMediumIconsSize, 0.001f);
        assertEquals("fileChooserViewModeSmallIconsSize should be 0", 0f, sizes.fileChooserViewModeSmallIconsSize, 0.001f);
        assertEquals("fileChooserViewModeListWidthSize should be 0", 0f, sizes.fileChooserViewModeListWidthSize, 0.001f);
    }

    @Test
    public void testCopyConstructor() {
        Sizes original = new Sizes();
        original.scaleFactor = 1.5f;
        original.spacingTop = 2.0f;
        original.spacingBottom = 3.0f;
        original.spacingRight = 4.0f;
        original.spacingLeft = 5.0f;
        original.buttonBarSpacing = 6.0f;
        original.menuItemIconSize = 7.0f;
        original.borderSize = 8.0f;
        original.spinnerButtonHeight = 9.0f;
        original.spinnerFieldSize = 10.0f;
        original.fileChooserViewModeBigIconsSize = 11.0f;
        original.fileChooserViewModeMediumIconsSize = 12.0f;
        original.fileChooserViewModeSmallIconsSize = 13.0f;
        original.fileChooserViewModeListWidthSize = 14.0f;

        Sizes copy = new Sizes(original);

        assertEquals("scaleFactor should be copied", 1.5f, copy.scaleFactor, 0.001f);
        assertEquals("spacingTop should be copied", 2.0f, copy.spacingTop, 0.001f);
        assertEquals("spacingBottom should be copied", 3.0f, copy.spacingBottom, 0.001f);
        assertEquals("spacingRight should be copied", 4.0f, copy.spacingRight, 0.001f);
        assertEquals("spacingLeft should be copied", 5.0f, copy.spacingLeft, 0.001f);
        assertEquals("buttonBarSpacing should be copied", 6.0f, copy.buttonBarSpacing, 0.001f);
        assertEquals("menuItemIconSize should be copied", 7.0f, copy.menuItemIconSize, 0.001f);
        assertEquals("borderSize should be copied", 8.0f, copy.borderSize, 0.001f);
        assertEquals("spinnerButtonHeight should be copied", 9.0f, copy.spinnerButtonHeight, 0.001f);
        assertEquals("spinnerFieldSize should be copied", 10.0f, copy.spinnerFieldSize, 0.001f);
        assertEquals("fileChooserViewModeBigIconsSize should be copied", 11.0f, copy.fileChooserViewModeBigIconsSize, 0.001f);
        assertEquals("fileChooserViewModeMediumIconsSize should be copied", 12.0f, copy.fileChooserViewModeMediumIconsSize, 0.001f);
        assertEquals("fileChooserViewModeSmallIconsSize should be copied", 13.0f, copy.fileChooserViewModeSmallIconsSize, 0.001f);
        assertEquals("fileChooserViewModeListWidthSize should be copied", 14.0f, copy.fileChooserViewModeListWidthSize, 0.001f);
    }

    @Test
    public void testCopyConstructorWithZeroValues() {
        Sizes original = new Sizes();
        // All values remain 0

        Sizes copy = new Sizes(original);

        // All values should be 0
        assertEquals("scaleFactor should be 0", 0f, copy.scaleFactor, 0.001f);
        assertEquals("spacingTop should be 0", 0f, copy.spacingTop, 0.001f);
        assertEquals("spacingBottom should be 0", 0f, copy.spacingBottom, 0.001f);
        assertEquals("spacingRight should be 0", 0f, copy.spacingRight, 0.001f);
        assertEquals("spacingLeft should be 0", 0f, copy.spacingLeft, 0.001f);
        assertEquals("buttonBarSpacing should be 0", 0f, copy.buttonBarSpacing, 0.001f);
        assertEquals("menuItemIconSize should be 0", 0f, copy.menuItemIconSize, 0.001f);
        assertEquals("borderSize should be 0", 0f, copy.borderSize, 0.001f);
        assertEquals("spinnerButtonHeight should be 0", 0f, copy.spinnerButtonHeight, 0.001f);
        assertEquals("spinnerFieldSize should be 0", 0f, copy.spinnerFieldSize, 0.001f);
        assertEquals("fileChooserViewModeBigIconsSize should be 0", 0f, copy.fileChooserViewModeBigIconsSize, 0.001f);
        assertEquals("fileChooserViewModeMediumIconsSize should be 0", 0f, copy.fileChooserViewModeMediumIconsSize, 0.001f);
        assertEquals("fileChooserViewModeSmallIconsSize should be 0", 0f, copy.fileChooserViewModeSmallIconsSize, 0.001f);
        assertEquals("fileChooserViewModeListWidthSize should be 0", 0f, copy.fileChooserViewModeListWidthSize, 0.001f);
    }

    @Test
    public void testCopyConstructorWithNegativeValues() {
        Sizes original = new Sizes();
        original.scaleFactor = -1.5f;
        original.spacingTop = -2.0f;
        original.borderSize = -3.0f;

        Sizes copy = new Sizes(original);

        assertEquals("Negative scaleFactor should be copied", -1.5f, copy.scaleFactor, 0.001f);
        assertEquals("Negative spacingTop should be copied", -2.0f, copy.spacingTop, 0.001f);
        assertEquals("Negative borderSize should be copied", -3.0f, copy.borderSize, 0.001f);
    }

    @Test
    public void testCopyConstructorIndependence() {
        Sizes original = new Sizes();
        original.scaleFactor = 1.0f;
        original.borderSize = 2.0f;

        Sizes copy = new Sizes(original);

        // Modify original
        original.scaleFactor = 3.0f;
        original.borderSize = 4.0f;

        // Copy should remain unchanged
        assertEquals("Copy should be independent of original", 1.0f, copy.scaleFactor, 0.001f);
        assertEquals("Copy should be independent of original", 2.0f, copy.borderSize, 0.001f);
    }

    @Test
    public void testCopyConstructorWithNull() {
        // This test ensures the copy constructor handles null gracefully
        // Though the current implementation doesn't check for null, we test the behavior
        try {
            new Sizes(null);
            fail("Should throw NullPointerException or handle null gracefully");
        } catch (NullPointerException e) {
            // Expected behavior
            assertTrue("Should throw NullPointerException", true);
        }
    }
}
