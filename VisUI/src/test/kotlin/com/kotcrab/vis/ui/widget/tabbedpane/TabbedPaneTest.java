package com.kotcrab.vis.ui.widget.tabbedpane;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton.VisImageButtonStyle;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TabbedPaneTest {
    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return new com.badlogic.gdx.files.FileHandle("test");
                        }
                        return null;
                    });
        }
        if (Gdx.app == null) {
            Gdx.app = (Application) Proxy.newProxyInstance(
                    Application.class.getClassLoader(),
                    new Class[]{Application.class},
                    (proxy, method, args) -> null);
        }
    }

    private static Skin createMinimalSkin() {
        Skin skin = new Skin();

        Sizes sizes = new Sizes();
        sizes.scaleFactor = 1f;
        skin.add("default", sizes, Sizes.class);


        com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData data = new com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData();
        com.badlogic.gdx.graphics.g2d.BitmapFont font = new com.badlogic.gdx.graphics.g2d.BitmapFont(data, new com.badlogic.gdx.graphics.g2d.TextureRegion(), false);
        skin.add("default-font", font, com.badlogic.gdx.graphics.g2d.BitmapFont.class);

        BaseDrawable drawable = new BaseDrawable();

        VisTextButtonStyle tabButtonStyle = new VisTextButtonStyle(drawable, drawable, drawable, font);
        skin.add("default", tabButtonStyle, VisTextButtonStyle.class);

        VisImageButtonStyle closeStyle = new VisImageButtonStyle();
        closeStyle.up = drawable;
        closeStyle.down = drawable;
        closeStyle.checked = drawable;
        skin.add("close", closeStyle, VisImageButtonStyle.class);

        VisImageButtonStyle closeActiveStyle = new VisImageButtonStyle();
        closeActiveStyle.up = drawable;
        closeActiveStyle.down = drawable;
        closeActiveStyle.checked = drawable;
        skin.add("close-active-tab", closeActiveStyle, VisImageButtonStyle.class);

        TabbedPane.TabbedPaneStyle paneStyle = new TabbedPane.TabbedPaneStyle();
        paneStyle.buttonStyle = tabButtonStyle;
        skin.add("default", paneStyle, TabbedPane.TabbedPaneStyle.class);

        return skin;
    }

    private static TabbedPane.TabbedPaneStyle createPaneStyle() {
        TabbedPane.TabbedPaneStyle style = new TabbedPane.TabbedPaneStyle((TabbedPane.TabbedPaneStyle) VisUI.getSkin().get(TabbedPane.TabbedPaneStyle.class));
        style.draggable = false;
        style.vertical = false;
        return style;
    }

    private static String getTabButtonText(TabbedPane pane, int index) {
        com.badlogic.gdx.scenes.scene2d.Actor buttonTable = pane.getTabsPane().getChildren().get(index);
        com.badlogic.gdx.scenes.scene2d.Group group = (com.badlogic.gdx.scenes.scene2d.Group) buttonTable;
        com.kotcrab.vis.ui.widget.VisTextButton button = (com.kotcrab.vis.ui.widget.VisTextButton) group.getChildren().first();
        return button.getText().toString();
    }

    @Before
    public void setupSkin() {
        VisUI.dispose();
        VisUI.setSkipGdxVersionCheck(true);
        VisUI.load(createMinimalSkin());
    }

    @After
    public void tearDown() {
        VisUI.dispose();
    }

    @Test
    public void testAddSetsPaneAndActivatesTab() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        TrackingListener listener = new TrackingListener();
        pane.addListener(listener);

        TrackingTab tab = new TrackingTab("A");
        pane.add(tab);

        assertSame(pane, tab.getPane());
        assertSame(tab, pane.getActiveTab());
        assertTrue(tab.isActiveTab());
        assertEquals(1, tab.shown);
        assertEquals(0, tab.hidden);

        assertEquals(1, listener.switched.size);
        assertSame(tab, listener.switched.get(0));
    }

    @Test
    public void testSwitchTabCallsHideShowAndNotifiesListener() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        TrackingListener listener = new TrackingListener();
        pane.addListener(listener);

        TrackingTab tab1 = new TrackingTab("A");
        TrackingTab tab2 = new TrackingTab("B");
        pane.add(tab1);
        pane.add(tab2);

        assertSame(tab2, pane.getActiveTab());
        assertEquals(1, tab1.hidden);
        assertEquals(1, tab2.shown);

        pane.switchTab(tab1);

        assertSame(tab1, pane.getActiveTab());
        assertEquals(1, tab2.hidden);
        assertEquals(2, tab1.shown);

        assertSame(tab1, listener.switched.peek());
    }

    @Test
    public void testUpdateTabTitleAddsDirtyMarker() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        TrackingTab tab = new TrackingTab("A", true, true);
        pane.add(tab);

        assertEquals("A", getTabButtonText(pane, 0));

        tab.setDirty(true);
        assertTrue(tab.isDirty());
        assertEquals("*A", getTabButtonText(pane, 0));
    }

    @Test
    public void testDisableActiveTabSwitchesToFirstEnabled() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        TrackingListener listener = new TrackingListener();
        pane.addListener(listener);

        TrackingTab tab1 = new TrackingTab("A");
        TrackingTab tab2 = new TrackingTab("B");
        pane.add(tab1);
        pane.add(tab2);

        assertSame(tab2, pane.getActiveTab());

        pane.disableTab(tab2, true);

        assertSame(tab1, pane.getActiveTab());
        assertTrue(pane.isTabDisabled(tab2));
        assertFalse(pane.isTabDisabled(tab1));
        assertSame(tab1, listener.switched.peek());
    }

    @Test
    public void testDisableOnlyTabResultsInNoActiveTabWhenAllowDeselect() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        pane.setAllowTabDeselect(true);
        TrackingListener listener = new TrackingListener();
        pane.addListener(listener);

        TrackingTab tab = new TrackingTab("A");
        pane.add(tab);
        assertSame(tab, pane.getActiveTab());

        pane.disableTab(tab, true);

        assertNull(pane.getActiveTab());
        assertSame(null, listener.switched.peek());
    }

    @Test
    public void testRemoveCallsLifecycleAndNotifiesListeners() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        TrackingListener listener = new TrackingListener();
        pane.addListener(listener);

        TrackingTab tab1 = new TrackingTab("A");
        TrackingTab tab2 = new TrackingTab("B");
        pane.add(tab1);
        pane.add(tab2);

        assertTrue(pane.remove(tab2));
        assertNull(tab2.getPane());
        assertEquals(1, tab2.hidden);
        assertEquals(1, tab2.disposed);
        assertSame(tab2, listener.removed.peek());
        assertSame(tab1, pane.getActiveTab());

        pane.remove(tab1);
        assertEquals(1, listener.removedAll);
        assertNull(pane.getActiveTab());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTabDisabledThrowsForForeignTab() {
        TabbedPane pane = new TabbedPane(createPaneStyle());
        pane.isTabDisabled(new TrackingTab("X"));
    }

    private static class TrackingTab extends Tab {
        private final String title;
        private final com.badlogic.gdx.scenes.scene2d.ui.Table content = org.mockito.Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Table.class);

        int shown;
        int hidden;
        int disposed;

        TrackingTab(String title) {
            super(false);
            this.title = title;
        }

        TrackingTab(String title, boolean savable, boolean closeableByUser) {
            super(savable, closeableByUser);
            this.title = title;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public com.badlogic.gdx.scenes.scene2d.ui.Table getContentTable() {
            return content;
        }

        @Override
        public void onShow() {
            super.onShow();
            shown++;
        }

        @Override
        public void onHide() {
            super.onHide();
            hidden++;
        }

        @Override
        public void dispose() {
            disposed++;
        }
    }

    private static class TrackingListener implements TabbedPaneListener {
        final Array<Tab> switched = new Array<>();
        final Array<Tab> removed = new Array<>();
        int removedAll;

        @Override
        public void switchedTab(Tab tab) {
            switched.add(tab);
        }

        @Override
        public void removedTab(Tab tab) {
            removed.add(tab);
        }

        @Override
        public void removedAllTabs() {
            removedAll++;
        }
    }
}
