package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.ListViewStyle;
import com.kotcrab.vis.ui.widget.VisTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link ArrayAdapter}.
 */
public class ArrayAdapterTest {

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
    private TestArrayAdapter adapter;
    private ListView<String> listView;

    @Before
    public void setUp() {
        array = new Array<>();
        array.add("one");
        array.add("two");
        array.add("three");
        adapter = new TestArrayAdapter(array);
        ListViewStyle style = new ListViewStyle();
        style.scrollPaneStyle = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
        listView = new ListView<>(adapter, style);
    }

    @Test
    public void testIndexOf() {
        Assert.assertEquals(0, adapter.indexOf("one"));
        Assert.assertEquals(1, adapter.indexOf("two"));
        Assert.assertEquals(2, adapter.indexOf("three"));
        Assert.assertEquals(-1, adapter.indexOf("missing"));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, adapter.size());
    }

    @Test
    public void testGet() {
        Assert.assertEquals("one", adapter.get(0));
        Assert.assertEquals("two", adapter.get(1));
        Assert.assertEquals("three", adapter.get(2));
    }

    @Test
    public void testAdd() {
        adapter.add("four");
        Assert.assertEquals(4, adapter.size());
        Assert.assertEquals("four", adapter.get(3));
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
    public void testAddAllArray() {
        Array<String> other = new Array<>();
        other.add("a");
        other.add("b");
        adapter.addAll(other);
        Assert.assertEquals(5, adapter.size());
        Assert.assertEquals("a", adapter.get(3));
        Assert.assertEquals("b", adapter.get(4));
    }

    @Test
    public void testAddAllVarargs() {
        adapter.addAll("x", "y");
        Assert.assertEquals(5, adapter.size());
        Assert.assertEquals("x", adapter.get(3));
        Assert.assertEquals("y", adapter.get(4));
    }

    @Test
    public void testSet() {
        adapter.set(1, "TWO");
        Assert.assertEquals("TWO", adapter.get(1));
    }

    @Test
    public void testInsert() {
        adapter.insert(1, "inserted");
        Assert.assertEquals(4, adapter.size());
        Assert.assertEquals("inserted", adapter.get(1));
        Assert.assertEquals("two", adapter.get(2));
    }

    @Test
    public void testSwap() {
        adapter.swap(0, 2);
        Assert.assertEquals("three", adapter.get(0));
        Assert.assertEquals("one", adapter.get(2));
    }

    @Test
    public void testRemoveValue() {
        boolean removed = adapter.removeValue("two", true);
        Assert.assertTrue(removed);
        Assert.assertEquals(2, adapter.size());
        Assert.assertEquals("one", adapter.get(0));
        Assert.assertEquals("three", adapter.get(1));
    }

    @Test
    public void testRemoveValueNotPresent() {
        boolean removed = adapter.removeValue("missing", true);
        Assert.assertFalse(removed);
        Assert.assertEquals(3, adapter.size());
    }

    @Test
    public void testRemoveIndex() {
        String removed = adapter.removeIndex(1);
        Assert.assertEquals("two", removed);
        Assert.assertEquals(2, adapter.size());
    }

    @Test
    public void testRemoveRange() {
        adapter.removeRange(1, 2);
        Assert.assertEquals(1, adapter.size());
        Assert.assertEquals("one", adapter.get(0));
    }

    @Test
    public void testClear() {
        adapter.clear();
        Assert.assertEquals(0, adapter.size());
    }

    @Test
    public void testReverse() {
        adapter.reverse();
        Assert.assertEquals("three", adapter.get(0));
        Assert.assertEquals("two", adapter.get(1));
        Assert.assertEquals("one", adapter.get(2));
    }

    @Test
    public void testPop() {
        String last = adapter.pop();
        Assert.assertEquals("three", last);
        Assert.assertEquals(2, adapter.size());
    }

    private static class TestArrayAdapter extends ArrayAdapter<String, VisTable> {
        TestArrayAdapter(Array<String> array) {
            super(array);
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
