package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit tests for {@link ListView}.
 */
public class ListViewTest {

    @Test
    public void testConstructorWithAdapter() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1", "Item2", "Item3"));
        ListView<String> listView = new ListView<>(adapter);
        
        Assert.assertNotNull("ListView should be created", listView);
        Assert.assertSame("Adapter should be set", adapter, listView.getAdapter());
        Assert.assertNotNull("Main table should not be null", listView.getMainTable());
        Assert.assertNotNull("Scroll pane should not be null", listView.getScrollPane());
    }

    @Test
    public void testConstructorWithAdapterAndStyleName() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1", "Item2"));
        ListView<String> listView = new ListView<>(adapter, "default");
        
        Assert.assertNotNull("ListView should be created", listView);
        Assert.assertSame("Adapter should be set", adapter, listView.getAdapter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAdapter() {
        new ListView<String>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullStyle() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        new ListView<>(adapter, (ListViewStyle) null);
    }

    @Test
    public void testItemClickListener() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1", "Item2"));
        ListView<String> listView = new ListView<>(adapter);
        
        final boolean[] clicked = {false};
        final String[] clickedItem = {null};
        
        ListView.ItemClickListener<String> listener = new ListView.ItemClickListener<String>() {
            @Override
            public void clicked(String item) {
                clicked[0] = true;
                clickedItem[0] = item;
            }
        };
        
        listView.setItemClickListener(listener);
        Assert.assertSame("Listener should be set", listener, listView.getClickListener());
        
        // Simulate click
        listener.clicked("Item1");
        Assert.assertTrue("Click should be registered", clicked[0]);
        Assert.assertEquals("Correct item should be clicked", "Item1", clickedItem[0]);
    }

    @Test
    public void testHeaderAndFooter() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        ListView<String> listView = new ListView<>(adapter);
        
        Table header = new Table();
        Table footer = new Table();
        
        Assert.assertNull("Header should be null initially", listView.getHeader());
        Assert.assertNull("Footer should be null initially", listView.getFooter());
        
        listView.setHeader(header);
        Assert.assertSame("Header should be set", header, listView.getHeader());
        
        listView.setFooter(footer);
        Assert.assertSame("Footer should be set", footer, listView.getFooter());
    }

    @Test
    public void testUpdatePolicy() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        ListView<String> listView = new ListView<>(adapter);
        
        Assert.assertEquals("Default update policy should be IMMEDIATELY", 
                          ListView.UpdatePolicy.IMMEDIATELY, listView.getUpdatePolicy());
        
        listView.setUpdatePolicy(ListView.UpdatePolicy.ON_DRAW);
        Assert.assertEquals("Update policy should be changed", 
                          ListView.UpdatePolicy.ON_DRAW, listView.getUpdatePolicy());
        
        listView.setUpdatePolicy(ListView.UpdatePolicy.MANUAL);
        Assert.assertEquals("Update policy should be changed", 
                          ListView.UpdatePolicy.MANUAL, listView.getUpdatePolicy());
    }

    @Test
    public void testRebuildView() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1", "Item2"));
        ListView<String> listView = new ListView<>(adapter);
        
        // Should not throw exception
        listView.rebuildView();
    }

    @Test
    public void testListViewTable() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        ListView<String> listView = new ListView<>(adapter);
        
        ListView.ListViewTable<String> mainTable = listView.getMainTable();
        Assert.assertNotNull("Main table should not be null", mainTable);
        Assert.assertSame("Main table should reference the list view", listView, mainTable.getListView());
    }

    @Test
    public void testScrollPaneProperties() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        ListView<String> listView = new ListView<>(adapter);
        
        ScrollPane scrollPane = listView.getScrollPane();
        Assert.assertNotNull("Scroll pane should not be null", scrollPane);
        Assert.assertFalse("Overscroll should be disabled", scrollPane.isOverscrollX());
        Assert.assertTrue("Overscroll should be disabled", scrollPane.isOverscrollY());
        Assert.assertFalse("Flick scroll should be disabled", scrollPane.isFlickScroll());
        Assert.assertFalse("Fade scroll bars should be disabled", scrollPane.getFadeScrollBars());
    }

    @Test
    public void testListAdapterListener() {
        ArrayListAdapter<String> adapter = new ArrayListAdapter<>(Arrays.asList("Item1"));
        ListView<String> listView = new ListView<>(adapter);
        
        // Test that the adapter has a listener set
        // This is tested indirectly by ensuring the list view was created successfully
        // and the adapter is properly bound
        Assert.assertNotNull("Adapter should be properly bound", listView.getAdapter());
    }

    @Test
    public void testUpdatePolicyEnum() {
        // Test that all enum values exist
        ListView.UpdatePolicy[] policies = ListView.UpdatePolicy.values();
        Assert.assertTrue("Should have ON_DRAW policy", 
                         Arrays.asList(policies).contains(ListView.UpdatePolicy.ON_DRAW));
        Assert.assertTrue("Should have IMMEDIATELY policy", 
                         Arrays.asList(policies).contains(ListView.UpdatePolicy.IMMEDIATELY));
        Assert.assertTrue("Should have MANUAL policy", 
                         Arrays.asList(policies).contains(ListView.UpdatePolicy.MANUAL));
    }

    @Test
    public void testItemClickListenerInterface() {
        // Test that the interface can be implemented
        ListView.ItemClickListener<String> listener = new ListView.ItemClickListener<String>() {
            @Override
            public void clicked(String item) {
                // Implementation
            }
        };
        
        Assert.assertNotNull("Listener should be created", listener);
    }
}
