package com.kotcrab.vis.ui.widget.toast;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisImageButton;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import com.badlogic.gdx.utils.Array;

/**
 * Unit tests for {@link MessageToast}.
 */
public class MessageToastTest {

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
        BitmapFont.BitmapFontData data = new BitmapFont.BitmapFontData();
        data.lineHeight = 10;
        data.capHeight = 10;
        data.ascent = 0;
        data.descent = 0;
        data.down = -10;
        data.scaleX = 1;
        data.scaleY = 1;

        Glyph missing = new Glyph();
        missing.id = 0;
        missing.xadvance = 10;
        missing.page = 0;
        data.missingGlyph = missing;

        Texture texture = Mockito.mock(Texture.class);
        Mockito.when(texture.getWidth()).thenReturn(1);
        Mockito.when(texture.getHeight()).thenReturn(1);
        TextureRegion region = new TextureRegion(texture);

        BitmapFont font = new BitmapFont(data, new Array<>(new TextureRegion[]{region}), false);
        skin.add("default-font", font, BitmapFont.class);
        skin.add("default", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE), Label.LabelStyle.class);

        // Headless-friendly drawable (does not require GL context).
        Drawable drawable = new BaseDrawable();

        // Styles required by LinkLabel constructor.
        LinkLabel.LinkLabelStyle linkStyle = new LinkLabel.LinkLabelStyle(skin.getFont("default-font"), com.badlogic.gdx.graphics.Color.WHITE, drawable);
        skin.add("default", linkStyle, LinkLabel.LinkLabelStyle.class);

        // Styles required by Toast (used transitively by some code paths).
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
    public void testConstructorAddsMessageLabelAndLinkTable() {
        MessageToast toast = new MessageToast("hello");

        Assert.assertEquals(2, toast.getChildren().size);
        Assert.assertTrue(toast.getChildren().get(0) instanceof com.kotcrab.vis.ui.widget.VisLabel);
    }

    @Test
    public void testAddLinkLabelAddsLinkLabelToInternalTable() throws Exception {
        MessageToast toast = new MessageToast("hello");

        LinkLabel.LinkLabelListener listener = url -> {
        };

        toast.addLinkLabel("Click", listener);

        Field tableField = MessageToast.class.getDeclaredField("linkLabelTable");
        tableField.setAccessible(true);
        Object linkTableObj = tableField.get(toast);

        Assert.assertTrue(linkTableObj instanceof com.kotcrab.vis.ui.widget.VisTable);
        com.kotcrab.vis.ui.widget.VisTable linkTable = (com.kotcrab.vis.ui.widget.VisTable) linkTableObj;

        Assert.assertEquals(1, linkTable.getChildren().size);
        Actor child = linkTable.getChildren().first();
        Assert.assertTrue(child instanceof LinkLabel);

        LinkLabel link = (LinkLabel) child;
        Assert.assertSame(listener, link.getListener());
    }
}
