package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link ButtonBar}.
 */
public class ButtonBarTest {

    @BeforeClass
    public static void setupGdx() {
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
                        if ("classpath".equals(name)) {
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
    public void setupVisUI() {
        if (VisUI.isLoaded()) VisUI.dispose(true);
        VisUI.setSkipGdxVersionCheck(true);

        Skin skin = new Skin();
        Sizes sizes = new Sizes();
        sizes.buttonBarSpacing = 10f;
        skin.add("default", sizes, Sizes.class);
        VisUI.load(skin);
    }

    @After
    public void tearDownVisUI() {
        if (VisUI.isLoaded()) VisUI.dispose(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSizesThrows() {
        new ButtonBar(null, ButtonBar.LINUX_ORDER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOrderNullThrows() {
        ButtonBar bar = new ButtonBar(new Sizes(), ButtonBar.LINUX_ORDER);
        bar.setOrder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetButtonNullTypeThrows() {
        ButtonBar bar = new ButtonBar(new Sizes(), "O");
        bar.setButton(null, new Button());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetButtonNullButtonThrows() {
        ButtonBar bar = new ButtonBar(new Sizes(), "O");
        bar.setButton(ButtonBar.ButtonType.OK, (Button) null);
    }

    @Test
    public void testCreateTableRespectsOrderAndSpacing() {
        Sizes sizes = new Sizes();
        sizes.buttonBarSpacing = 10f;

        Button ok = new Button();
        Button cancel = new Button();

        ButtonBar bar = new ButtonBar(sizes, "O C");
        bar.setButton(ButtonBar.ButtonType.OK, ok);
        bar.setButton(ButtonBar.ButtonType.CANCEL, cancel);

        VisTable table = bar.createTable();

        Assert.assertEquals(3, table.getCells().size);

        Cell<?> cell0 = table.getCells().get(0);
        Cell<?> cell1 = table.getCells().get(1);
        Cell<?> cell2 = table.getCells().get(2);

        Assert.assertSame(ok, cell0.getActor());
        Assert.assertNull(cell1.getActor());
        Assert.assertEquals(10f, cell1.getPrefWidth(), 0.0001f);
        Assert.assertSame(cancel, cell2.getActor());
    }

    @Test
    public void testCreateTableIgnoreSpacingSkipsSpacerCells() {
        Sizes sizes = new Sizes();
        sizes.buttonBarSpacing = 10f;

        Button ok = new Button();
        Button cancel = new Button();

        ButtonBar bar = new ButtonBar(sizes, "O C");
        bar.setIgnoreSpacing(true);
        bar.setButton(ButtonBar.ButtonType.OK, ok);
        bar.setButton(ButtonBar.ButtonType.CANCEL, cancel);

        VisTable table = bar.createTable();

        Assert.assertEquals(2, table.getCells().size);
        Assert.assertSame(ok, table.getCells().get(0).getActor());
        Assert.assertSame(cancel, table.getCells().get(1).getActor());
    }

    @Test
    public void testSetButtonReplacesExistingButton() {
        ButtonBar bar = new ButtonBar(new Sizes(), "O");

        Button first = new Button();
        Button second = new Button();

        bar.setButton(ButtonBar.ButtonType.OK, first);
        Assert.assertSame(first, bar.getButton(ButtonBar.ButtonType.OK));

        bar.setButton(ButtonBar.ButtonType.OK, second);
        Assert.assertSame(second, bar.getButton(ButtonBar.ButtonType.OK));
    }

    @Test
    public void testCreateTableMultipleCallsMoveButtonsBetweenTables() {
        ButtonBar bar = new ButtonBar(new Sizes(), "O");
        Button ok = new Button();
        bar.setButton(ButtonBar.ButtonType.OK, ok);

        VisTable table1 = bar.createTable();
        Assert.assertSame(table1, ok.getParent());

        VisTable table2 = bar.createTable();
        Assert.assertSame(table2, ok.getParent());
        Assert.assertNotSame(table1, ok.getParent());

        // old table should no longer contain button as child
        Assert.assertFalse(table1.getChildren().contains((Actor) ok, true));
    }
}
