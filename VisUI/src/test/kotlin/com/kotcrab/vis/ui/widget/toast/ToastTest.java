package com.kotcrab.vis.ui.widget.toast;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.widget.VisImageButton;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link Toast}.
 */
public class ToastTest {

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

        // Ensure VisUI isn't already loaded from other tests.
        try {
            VisUI.dispose(false);
        } catch (Exception ignored) {
        }

        skin = new Skin();

        // Minimal resources to allow scene2d widgets to exist.
        com.badlogic.gdx.graphics.g2d.BitmapFont font = Mockito.mock(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        skin.add("default-font", font, com.badlogic.gdx.graphics.g2d.BitmapFont.class);
        skin.add("default", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE), Label.LabelStyle.class);

        // Headless-friendly drawable (does not require GL context).
        Drawable drawable = new BaseDrawable();

        VisImageButton.VisImageButtonStyle closeButtonStyle = new VisImageButton.VisImageButtonStyle();
        closeButtonStyle.up = drawable;
        closeButtonStyle.down = drawable;
        closeButtonStyle.checked = drawable;
        closeButtonStyle.imageUp = drawable;

        Toast.ToastStyle toastStyle = new Toast.ToastStyle(drawable, closeButtonStyle);
        skin.add("default", toastStyle, Toast.ToastStyle.class);

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

    @Test
    public void testToastAssignsToToastTableContent() {
        ToastTable content = new ToastTable();

        Toast toast = new Toast(content);

        Assert.assertSame(toast, content.getToast());
        Assert.assertSame(content, toast.getContentTable());
        Assert.assertNotNull(toast.getMainTable());
    }

    @Test
    public void testFadeInSetsAlphaAndAddsAction() {
        Table content = new Table();
        Toast toast = new Toast(content);

        toast.fadeIn();

        Assert.assertEquals(0f, toast.getMainTable().getColor().a, 0.0001f);
        Assert.assertTrue(toast.getMainTable().getActions().size > 0);
    }

    @Test
    public void testFadeOutAddsSequenceActionThatRemovesToast() throws Exception {
        Table content = new Table();
        Toast toast = new Toast(content);

        ToastManager manager = org.mockito.Mockito.mock(ToastManager.class);
        toast.setToastManager(manager);

        toast.fadeOut();

        Assert.assertTrue("fadeOut should add an action", toast.getMainTable().getActions().size > 0);

        // Force-complete actions so that the final removal step runs. Sequence actions may require
        // at least one additional act() after the timed fade action completes.
        toast.getMainTable().act(com.kotcrab.vis.ui.widget.VisWindow.FADE_TIME + 0.01f);
        toast.getMainTable().act(0f);
        toast.getMainTable().act(0f);

        org.mockito.Mockito.verify(manager, org.mockito.Mockito.times(1)).remove(toast);
    }

    @Test
    public void testGetAndSetToastManager() {
        Toast toast = new Toast(new Table());
        ToastManager manager = org.mockito.Mockito.mock(ToastManager.class);

        toast.setToastManager(manager);

        Assert.assertSame(manager, toast.getToastManager());
    }

    @Test
    public void testMainTableHasBackgroundFromStyle() {
        Toast toast = new Toast(new Table());
        Assert.assertNotNull(toast.getMainTable().getBackground());
    }
}
