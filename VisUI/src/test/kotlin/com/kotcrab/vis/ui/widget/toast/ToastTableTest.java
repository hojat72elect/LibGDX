package com.kotcrab.vis.ui.widget.toast;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link ToastTable}.
 */
public class ToastTableTest {

    private static Skin skin;
    private static Files previousFiles;
    private static com.badlogic.gdx.Application previousApp;

    @BeforeClass
    public static void setup() {
        previousFiles = Gdx.files;
        previousApp = Gdx.app;

        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        if (args != null && args.length == 1 && args[0] instanceof String) {
                            String path = (String) args[0];
                            if ("classpath".equals(name) || "internal".equals(name) || "absolute".equals(name)
                                    || "local".equals(name) || "external".equals(name)) {
                                return new FileHandle(path);
                            }
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

        try {
            VisUI.dispose(false);
        } catch (Exception ignored) {
        }

        skin = new Skin();
        com.badlogic.gdx.graphics.g2d.BitmapFont font = Mockito.mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        skin.add("default-font", font, com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        skin.add("default", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE), Label.LabelStyle.class);
        VisUI.load(skin);
    }

    @AfterClass
    public static void tearDown() {
        try {
            VisUI.dispose(false);
        } catch (Exception ignored) {
        }
        if (skin != null) skin.dispose();

        Gdx.files = previousFiles;
        Gdx.app = previousApp;
    }

    @Test(expected = IllegalStateException.class)
    public void testFadeOutThrowsWhenNotShown() {
        ToastTable table = new ToastTable();
        table.fadeOut();
    }

    @Test
    public void testFadeOutDelegatesToToast() {
        ToastTable table = new ToastTable();
        Toast toast = Mockito.mock(Toast.class);
        table.setToast(toast);

        table.fadeOut();

        Mockito.verify(toast, Mockito.times(1)).fadeOut();
    }

    @Test
    public void testGetToast() {
        ToastTable table = new ToastTable();
        Toast toast = Mockito.mock(Toast.class);

        table.setToast(toast);

        org.junit.Assert.assertSame(toast, table.getToast());
    }
}
