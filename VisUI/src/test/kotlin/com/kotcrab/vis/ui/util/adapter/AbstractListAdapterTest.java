package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.ListViewStyle;
import com.kotcrab.vis.ui.widget.VisTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.Comparator;

/**
 * Unit tests for {@link AbstractListAdapter}. Uses a concrete {@link ArrayAdapter} subclass for testing.
 */
public class AbstractListAdapterTest {

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
        if (!VisUI.isLoaded()) {
            VisUI.load(new Skin());
        }
    }

    private TestArrayAdapter adapter;
    private ListView<String> listView;

    @Before
    public void setUp() {
        Array<String> array = new Array<>();
        array.add("a");
        array.add("b");
        adapter = new TestArrayAdapter(array);
        ListViewStyle style = new ListViewStyle();
        style.scrollPaneStyle = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
        listView = new ListView<>(adapter, style);
    }

    @Test
    public void testGetSelectionModeDefault() {
        Assert.assertEquals(AbstractListAdapter.SelectionMode.DISABLED, adapter.getSelectionMode());
    }

    @Test
    public void testSetSelectionModeGetSelectionMode() {
        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.SINGLE);
        Assert.assertEquals(AbstractListAdapter.SelectionMode.SINGLE, adapter.getSelectionMode());
        adapter.setSelectionMode(AbstractListAdapter.SelectionMode.MULTIPLE);
        Assert.assertEquals(AbstractListAdapter.SelectionMode.MULTIPLE, adapter.getSelectionMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectionModeNullThrows() {
        adapter.setSelectionMode(null);
    }

    @Test
    public void testGetSelectionEmptyByDefault() {
        Assert.assertNotNull(adapter.getSelection());
        Assert.assertEquals(0, adapter.getSelection().size);
    }

    @Test
    public void testGetSelectionManager() {
        Assert.assertNotNull(adapter.getSelectionManager());
    }

    @Test
    public void testSetItemsSorterGetItemsSorter() {
        Comparator<String> comp = String::compareTo;
        adapter.setItemsSorter(comp);
        Assert.assertSame(comp, adapter.getItemsSorter());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetListViewTwiceThrows() {
        Array<String> arr = new Array<>();
        TestArrayAdapter a = new TestArrayAdapter(arr);
        ListViewStyle style = new ListViewStyle();
        style.scrollPaneStyle = new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle();
        ListView<String> firstView = new ListView<>(a, style);
        a.setListView(firstView, firstView.new ListAdapterListener());
    }

    @Test
    public void testFillTableAddsViews() {
        VisTable table = new VisTable();
        adapter.fillTable(table);
        Assert.assertEquals(2, table.getCells().size);
    }

    @Test
    public void testListSelectionDefaultKey() {
        Assert.assertEquals(-1, AbstractListAdapter.ListSelection.DEFAULT_KEY);
    }

    @Test
    public void testListSelectionGetMultiSelectKeySetMultiSelectKey() {
        adapter.getSelectionManager().setMultiSelectKey(1);
        Assert.assertEquals(1, adapter.getSelectionManager().getMultiSelectKey());
    }

    @Test
    public void testListSelectionGetGroupMultiSelectKeySetGroupMultiSelectKey() {
        adapter.getSelectionManager().setGroupMultiSelectKey(2);
        Assert.assertEquals(2, adapter.getSelectionManager().getGroupMultiSelectKey());
    }

    @Test
    public void testListSelectionProgrammaticChangeEvents() {
        Assert.assertTrue(adapter.getSelectionManager().isProgrammaticChangeEvents());
        adapter.getSelectionManager().setProgrammaticChangeEvents(false);
        Assert.assertFalse(adapter.getSelectionManager().isProgrammaticChangeEvents());
    }

    @Test
    public void testListSelectionSetListener() {
        AbstractListAdapter.ListSelectionListener<String, VisTable> listener =
                new AbstractListAdapter.ListSelectionListener<String, VisTable>() {
                    @Override
                    public void selected(String item, VisTable view) {}
                    @Override
                    public void deselected(String item, VisTable view) {}
                };
        adapter.getSelectionManager().setListener(listener);
        Assert.assertSame(listener, adapter.getSelectionManager().getListener());
    }

    @Test
    public void testListSelectionSetListenerNullUsesAdapter() {
        adapter.getSelectionManager().setListener(null);
        Assert.assertNotNull(adapter.getSelectionManager().getListener());
    }

    @Test
    public void testSelectionModeEnumValues() {
        Assert.assertNotNull(AbstractListAdapter.SelectionMode.DISABLED);
        Assert.assertNotNull(AbstractListAdapter.SelectionMode.SINGLE);
        Assert.assertNotNull(AbstractListAdapter.SelectionMode.MULTIPLE);
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
        protected void selectView(VisTable view) {
            // no-op for test
        }

        @Override
        protected void deselectView(VisTable view) {
            // no-op for test
        }
    }
}
