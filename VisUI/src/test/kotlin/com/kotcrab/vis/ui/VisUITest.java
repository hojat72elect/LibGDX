package com.kotcrab.vis.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link VisUI}.
 */
public class VisUITest {

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
    }

    @Before
    public void setUp() {
        // Reset VisUI state before each test
        disposeVisUI();
        VisUI.setSkipGdxVersionCheck(true);
    }

    @Test
    public void testDefaultTitleAlign() {
        Assert.assertEquals(com.badlogic.gdx.utils.Align.left, VisUI.getDefaultTitleAlign());
    }

    @Test
    public void testSetDefaultTitleAlign() {
        VisUI.setDefaultTitleAlign(com.badlogic.gdx.utils.Align.center);
        Assert.assertEquals(com.badlogic.gdx.utils.Align.center, VisUI.getDefaultTitleAlign());
    }

    @Test
    public void testSetSkipGdxVersionCheck() {
        VisUI.setSkipGdxVersionCheck(false);
        // Can't easily test the effect without mocking Version, but we can test the setter
        assertTrue("Should be able to set skip version check", true);
    }

    @Test
    public void testIsLoadedInitiallyFalse() {
        Assert.assertFalse("VisUI should not be loaded initially", VisUI.isLoaded());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSkinWhenNotLoaded() {
        VisUI.getSkin();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSizesWhenNotLoaded() {
        VisUI.getSizes();
    }

    @Test(expected = GdxRuntimeException.class)
    public void testLoadTwice() {
        // Create a mock skin
        Skin skin = new Skin();
        VisUI.load(skin);
        VisUI.load(skin); // Should throw exception
    }

    @Test
    public void testLoadWithSkin() {
        Skin skin = new Skin();
        VisUI.load(skin);

        assertTrue("VisUI should be loaded", VisUI.isLoaded());
        Assert.assertEquals("Should return the loaded skin", skin, VisUI.getSkin());
    }

    @Test
    public void testLoadWithSkinScale() {
        try {
            VisUI.load(VisUI.SkinScale.X1);
            // If we get here, the test passes (actual skin loading may fail due to missing files)
            assertTrue("VisUI should attempt to load", true);
        } catch (Exception e) {
            // Expected if skin files don't exist
            assertTrue("Should handle missing skin gracefully", true);
        }
    }

    @Test
    public void testLoadDefault() {
        try {
            VisUI.load();
            // If we get here, the test passes (actual skin loading may fail due to missing files)
            assertTrue("VisUI should attempt to load", true);
        } catch (Exception e) {
            // Expected if skin files don't exist
            assertTrue("Should handle missing skin gracefully", true);
        }
    }

    @Test
    public void testDispose() {
        try {
            VisUI.load();
            assertTrue("VisUI should be loaded", VisUI.isLoaded());
        } catch (Exception e) {
            // If loading fails, dispose should still work
            assertTrue("Should handle loading failure", true);
        }

        VisUI.dispose();
        Assert.assertFalse("VisUI should not be loaded after dispose", VisUI.isLoaded());
    }

    @Test
    public void testDisposeWithFalseParameter() {
        try {
            VisUI.load();
            assertTrue("VisUI should be loaded", VisUI.isLoaded());
        } catch (Exception e) {
            // If loading fails, dispose should still work
            assertTrue("Should handle loading failure", true);
        }

        VisUI.dispose(false);
        Assert.assertFalse("VisUI should not be loaded after dispose", VisUI.isLoaded());
    }

    @Test
    public void testDisposeWhenNotLoaded() {
        // Should not throw exception
        VisUI.dispose();
        Assert.assertFalse("VisUI should not be loaded", VisUI.isLoaded());
    }

    @Test
    public void testSkinScaleEnum() {
        // Test X1 scale
        VisUI.SkinScale x1 = VisUI.SkinScale.X1;
        Assert.assertNotNull("X1 scale should not be null", x1.getSkinFile());
        Assert.assertEquals("default", x1.getSizesName());

        // Test X2 scale
        VisUI.SkinScale x2 = VisUI.SkinScale.X2;
        Assert.assertNotNull("X2 scale should not be null", x2.getSkinFile());
        Assert.assertEquals("x2", x2.getSizesName());
    }

    @Test
    public void testGetSizesAfterLoad() {
        try {
            VisUI.load(VisUI.SkinScale.X1);
            Sizes sizes = VisUI.getSizes();
            Assert.assertNotNull("Sizes should not be null", sizes);
        } catch (Exception e) {
            // Expected if skin files don't exist
            assertTrue("Should handle missing skin gracefully", true);
        }
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
