package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.toast.Toast;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link ToastManager}.
 */
public class ToastManagerTest {

    private ToastManager toastManager;
    private Group testRoot;

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }
        if (Gdx.app == null) {
            Gdx.app = (Application) Proxy.newProxyInstance(
                    Application.class.getClassLoader(),
                    new Class[]{Application.class},
                    (proxy, method, args) -> null);
        }
        if (Gdx.graphics == null) {
            Gdx.graphics = (Graphics) Proxy.newProxyInstance(
                    Graphics.class.getClassLoader(),
                    new Class[]{Graphics.class},
                    (proxy, method, args) -> {
                        if ("getWidth".equals(method.getName())) {
                            return 800;
                        }
                        if ("getHeight".equals(method.getName())) {
                            return 600;
                        }
                        return null;
                    });
        }
    }

    @Before
    public void setUp() {
        // Create a mock root group for testing
        testRoot = new Group() {
            @Override
            public float getWidth() {
                return 800f;
            }

            @Override
            public float getHeight() {
                return 600f;
            }
        };
        toastManager = new ToastManager(testRoot);
    }

    @Test
    public void testConstructorWithGroup() {
        ToastManager manager = new ToastManager(new Group());
        Assert.assertNotNull(manager);
    }

    @Test
    public void testGetScreenPadding() {
        toastManager.setScreenPadding(15);
        Assert.assertEquals(15, toastManager.getScreenPadding());
    }

    @Test
    public void testGetScreenPaddingThrowsWhenDifferent() {
        toastManager.setScreenPaddingX(10);
        toastManager.setScreenPaddingY(20);
        try {
            toastManager.getScreenPadding();
            Assert.fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("Value of screen padding X is different than padding Y"));
        }
    }

    @Test
    public void testSetScreenPadding() {
        toastManager.setScreenPadding(25);
        Assert.assertEquals(25, toastManager.getScreenPaddingX());
        Assert.assertEquals(25, toastManager.getScreenPaddingY());
    }

    @Test
    public void testSetScreenPaddingXY() {
        toastManager.setScreenPadding(10, 20);
        Assert.assertEquals(10, toastManager.getScreenPaddingX());
        Assert.assertEquals(20, toastManager.getScreenPaddingY());
    }

    @Test
    public void testSetScreenPaddingX() {
        toastManager.setScreenPaddingX(30);
        Assert.assertEquals(30, toastManager.getScreenPaddingX());
    }

    @Test
    public void testSetScreenPaddingY() {
        toastManager.setScreenPaddingY(40);
        Assert.assertEquals(40, toastManager.getScreenPaddingY());
    }

    @Test
    public void testGetMessagePadding() {
        Assert.assertEquals(5, toastManager.getMessagePadding()); // Default value
    }

    @Test
    public void testSetMessagePadding() {
        toastManager.setMessagePadding(15);
        Assert.assertEquals(15, toastManager.getMessagePadding());
    }

    @Test
    public void testGetAlignment() {
        Assert.assertEquals(Align.topRight, toastManager.getAlignment()); // Default value
    }

    @Test
    public void testSetAlignment() {
        toastManager.setAlignment(Align.bottomLeft);
        Assert.assertEquals(Align.bottomLeft, toastManager.getAlignment());
    }

    @Test
    public void testUntilClosedConstant() {
        Assert.assertEquals(-1, ToastManager.UNTIL_CLOSED);
    }

    @Test
    public void testDefaultValues() {
        ToastManager manager = new ToastManager(new Group());
        Assert.assertEquals(20, manager.getScreenPaddingX());
        Assert.assertEquals(20, manager.getScreenPaddingY());
        Assert.assertEquals(5, manager.getMessagePadding());
        Assert.assertEquals(Align.topRight, manager.getAlignment());
    }

    @Test
    public void testClear() {
        // Test clear doesn't throw exception even with no toasts
        toastManager.clear();
        Assert.assertEquals(0, getToastCount(toastManager));
    }

    @Test
    public void testToFront() {
        // This test just ensures the method doesn't throw an exception
        toastManager.toFront();
    }

    @Test
    public void testResize() {
        // This test just ensures the method doesn't throw an exception
        toastManager.resize();
    }

    @Test
    public void testRemoveNonExistingToast() {
        Table table = new Table();
        boolean removed = toastManager.remove(null); // Test with null
        Assert.assertFalse("Non-existing toast should not be removed", removed);
    }

    @Test
    public void testAlignmentPositions() {
        // Test all alignment options don't throw exceptions
        toastManager.setAlignment(Align.topLeft);
        toastManager.resize();

        toastManager.setAlignment(Align.topRight);
        toastManager.resize();

        toastManager.setAlignment(Align.bottomLeft);
        toastManager.resize();

        toastManager.setAlignment(Align.bottomRight);
        toastManager.resize();

        toastManager.setAlignment(Align.center);
        toastManager.resize();

        // Should not throw any exceptions
        Assert.assertTrue(true);
    }

    /**
     * Helper method to get the current toast count using reflection.
     */
    private int getToastCount(ToastManager manager) {
        try {
            Field toastsField = ToastManager.class.getDeclaredField("toasts");
            toastsField.setAccessible(true);
            Array<Toast> toasts = (Array<Toast>) toastsField.get(manager);
            return toasts.size;
        } catch (Exception e) {
            return -1; // Error case
        }
    }
}
