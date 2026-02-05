package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.ListViewStyle;
import com.kotcrab.vis.ui.widget.VisTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Unit tests for {@link ArrayListAdapter}.
 */
public class ArrayListAdapterTest {

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

    private ArrayList<String> list;
    private TestArrayListAdapter adapter;
    private ListView<String> listView;

    @Before
    public void setUp() {
        list = new ArrayList<>(Arrays.asList("one", "two", "three"));
        adapter = new TestArrayListAdapter(list);
        ListViewStyle style = new ListViewStyle();
        style.scrollPaneStyle = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
        listView = new ListView<>(adapter, style);
    }

    @Test
    public void testIterable() {
        int count = 0;
        for (String s : adapter.iterable()) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, adapter.size());
    }

    @Test
    public void testIndexOf() {
        Assert.assertEquals(0, adapter.indexOf("one"));
        Assert.assertEquals(1, adapter.indexOf("two"));
        Assert.assertEquals(-1, adapter.indexOf("missing"));
    }

    @Test
    public void testAdd() {
        adapter.add("four");
        Assert.assertEquals(4, adapter.size());
        Assert.assertEquals("four", adapter.get(3));
    }

    @Test
    public void testGet() {
        Assert.assertEquals("one", adapter.get(0));
        Assert.assertEquals("two", adapter.get(1));
    }

    @Test
    public void testSet() {
        String prev = adapter.set(1, "TWO");
        Assert.assertEquals("two", prev);
        Assert.assertEquals("TWO", adapter.get(1));
    }

    @Test
    public void testAddAtIndex() {
        adapter.add(1, "inserted");
        Assert.assertEquals(4, adapter.size());
        Assert.assertEquals("inserted", adapter.get(1));
        Assert.assertEquals("two", adapter.get(2));
    }

    @Test
    public void testRemoveByIndex() {
        String removed = adapter.remove(1);
        Assert.assertEquals("two", removed);
        Assert.assertEquals(2, adapter.size());
    }

    @Test
    public void testRemoveByItem() {
        boolean removed = adapter.remove("two");
        Assert.assertTrue(removed);
        Assert.assertEquals(2, adapter.size());
    }

    @Test
    public void testRemoveByItemNotPresent() {
        boolean removed = adapter.remove("missing");
        Assert.assertFalse(removed);
        Assert.assertEquals(3, adapter.size());
    }

    @Test
    public void testClear() {
        adapter.clear();
        Assert.assertEquals(0, adapter.size());
    }

    @Test
    public void testAddAllCollection() {
        boolean res = adapter.addAll(Arrays.asList("a", "b"));
        Assert.assertTrue(res);
        Assert.assertEquals(5, adapter.size());
    }

    @Test
    public void testAddAllAtIndex() {
        adapter.addAll(1, Arrays.asList("x", "y"));
        Assert.assertEquals(5, adapter.size());
        Assert.assertEquals("x", adapter.get(1));
        Assert.assertEquals("y", adapter.get(2));
    }

    @Test
    public void testRemoveAll() {
        boolean res = adapter.removeAll(Arrays.asList("one", "three"));
        Assert.assertTrue(res);
        Assert.assertEquals(1, adapter.size());
        Assert.assertEquals("two", adapter.get(0));
    }

    private static class TestArrayListAdapter extends ArrayListAdapter<String, VisTable> {
        TestArrayListAdapter(ArrayList<String> list) {
            super(list);
        }

        @Override
        protected VisTable createView(String item) {
            return new VisTable();
        }

        @Override
        protected void selectView(VisTable view) {}

        @Override
        protected void deselectView(VisTable view) {}
    }
}
