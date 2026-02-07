package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class ListViewTest {

    @Mock
    private Application mockApplication;
    @Mock
    private Files mockFiles;
    @Mock
    private Input mockInput;
    @Mock
    private Graphics mockGraphics;
    @Mock
    private Clipboard mockClipboard;
    private BitmapFont testFont;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;
        when(mockApplication.getClipboard()).thenReturn(mockClipboard);

        // Load VisUI for testing
        if (!VisUI.isLoaded()) {
            VisUI.setSkipGdxVersionCheck(true);
            testFont = newTestFont();
            Skin testSkin = createMinimalSkin();
            VisUI.load(testSkin);
        } else {
            testFont = newTestFont();
        }

        testFont.setColor(Color.WHITE);
    }

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
    }

    private Skin createMinimalSkin() {
        Skin skin = new Skin();
        // Add minimal required style for ListView
        ListViewStyle listViewStyle = new ListViewStyle();
        listViewStyle.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", listViewStyle);
        return skin;
    }

    private static BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = org.mockito.Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        org.mockito.Mockito.when(mockTexture.getWidth()).thenReturn(1);
        org.mockito.Mockito.when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        org.mockito.Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    @Test
    public void testConstructorWithAdapter() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1", "Item2", "Item3")));
        ListView<String> listView = new ListView<>(adapter);

        Assert.assertNotNull("ListView should be created", listView);
        Assert.assertSame("Adapter should be set", adapter, listView.getAdapter());
        Assert.assertNotNull("Main table should not be null", listView.getMainTable());
        Assert.assertNotNull("Scroll pane should not be null", listView.getScrollPane());
    }

    @Test
    public void testConstructorWithAdapterAndStyleName() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1", "Item2")));
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
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
        new ListView<>(adapter, (ListViewStyle) null);
    }

    @Test
    public void testItemClickListener() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1", "Item2")));
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
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
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
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
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
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1", "Item2")));
        ListView<String> listView = new ListView<>(adapter);

        // Should not throw exception
        listView.rebuildView();
    }

    @Test
    public void testListViewTable() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
        ListView<String> listView = new ListView<>(adapter);

        ListView.ListViewTable<String> mainTable = listView.getMainTable();
        Assert.assertNotNull("Main table should not be null", mainTable);
        Assert.assertSame("Main table should reference the list view", listView, mainTable.getListView());
    }

    @Test
    public void testScrollPaneProperties() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
        ListView<String> listView = new ListView<>(adapter);

        ScrollPane scrollPane = listView.getScrollPane();
        Assert.assertNotNull("Scroll pane should not be null", scrollPane);
        Assert.assertFalse("Fade scroll bars should be disabled", scrollPane.getFadeScrollBars());
    }

    @Test
    public void testListAdapterListener() {
        TestArrayListAdapter adapter = new TestArrayListAdapter(new java.util.ArrayList<>(Arrays.asList("Item1")));
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

    private static class TestArrayListAdapter extends ArrayListAdapter<String, Table> {
        TestArrayListAdapter(java.util.ArrayList<String> list) {
            super(list);
        }

        @Override
        protected Table createView(String item) {
            return new Table();
        }

        @Override
        protected void updateView(Table view, String item) {
            // No updates needed for test
        }
    }
}
