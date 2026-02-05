package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link SimpleListAdapter}. Uses constructor with style to avoid VisUI.getSkin().
 */
public class SimpleListAdapterTest {

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
        if (!com.kotcrab.vis.ui.VisUI.isLoaded()) {
            com.kotcrab.vis.ui.VisUI.load(new com.badlogic.gdx.scenes.scene2d.ui.Skin());
        }
    }

    private Array<String> array;
    private SimpleListAdapter<String> adapter;
    private SimpleListAdapter.SimpleListAdapterStyle style;

    @Before
    public void setUp() {
        array = new Array<>();
        array.add("first");
        array.add("second");
        style = new SimpleListAdapter.SimpleListAdapterStyle();
        adapter = new SimpleListAdapter<>(array, style);
    }

    @Test
    public void testIndexOf() {
        Assert.assertEquals(0, adapter.indexOf("first"));
        Assert.assertEquals(1, adapter.indexOf("second"));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(2, adapter.size());
    }

    @Test
    public void testGet() {
        Assert.assertEquals("first", adapter.get(0));
        Assert.assertEquals("second", adapter.get(1));
    }

    @Test
    public void testSimpleListAdapterStyleConstructors() {
        SimpleListAdapter.SimpleListAdapterStyle empty = new SimpleListAdapter.SimpleListAdapterStyle();
        Assert.assertNull(empty.background);
        Assert.assertNull(empty.selection);

        SimpleListAdapter.SimpleListAdapterStyle copy = new SimpleListAdapter.SimpleListAdapterStyle(style);
        Assert.assertSame(style.background, copy.background);
        Assert.assertSame(style.selection, copy.selection);
    }
}
