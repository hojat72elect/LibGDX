package com.kotcrab.vis.ui.widget.tabbedpane;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TabTest {
	@BeforeClass
	public static void setupGdx() {
		if (Gdx.files == null) {
			Gdx.files = (Files) Proxy.newProxyInstance(
					Files.class.getClassLoader(),
					new Class[]{Files.class},
					(proxy, method, args) -> null);
		}
	}

	private static class TestTab extends Tab {
		private final String title;
		private final Table content = mock(Table.class);
		boolean disposed;

		TestTab(String title, boolean savable) {
			super(savable);
			this.title = title;
		}

		@Override
		public String getTabTitle() {
			return title;
		}

		@Override
		public Table getContentTable() {
			return content;
		}

		@Override
		public void dispose() {
			disposed = true;
		}
	}

	@Test
	public void testOnShowOnHideUpdatesActiveFlag() {
		TestTab tab = new TestTab("t", false);
		assertFalse(tab.isActiveTab());

		tab.onShow();
		assertTrue(tab.isActiveTab());

		tab.onHide();
		assertFalse(tab.isActiveTab());
	}

	@Test(expected = IllegalStateException.class)
	public void testSetDirtyThrowsWhenNotSavable() {
		TestTab tab = new TestTab("t", false);
		tab.setDirty(true);
	}

	@Test
	public void testSetDirtyUpdatesAndNotifiesPane() {
		TestTab tab = new TestTab("t", true);
		TabbedPane pane = mock(TabbedPane.class);
		tab.setPane(pane);

		tab.setDirty(true);

		assertTrue(tab.isDirty());
		assertSame(pane, tab.getPane());
		verify(pane).updateTabTitle(tab);
	}

	@Test
	public void testSetDirtyNoChangeDoesNotNotifyPane() {
		TestTab tab = new TestTab("t", true);
		TabbedPane pane = mock(TabbedPane.class);
		tab.setPane(pane);

		tab.setDirty(false);
		assertFalse(tab.isDirty());
		verify(pane, never()).updateTabTitle(tab);
	}

	@Test
	public void testRemoveFromTabPaneDelegatesToPaneRemove() {
		TestTab tab = new TestTab("t", false);
		TabbedPane pane = mock(TabbedPane.class);
		tab.setPane(pane);

		tab.removeFromTabPane();

		verify(pane).remove(tab);
	}

	@Test(expected = IllegalStateException.class)
	public void testSaveThrowsWhenNotSavable() {
		TestTab tab = new TestTab("t", false);
		tab.save();
	}

	@Test
	public void testContentTableStable() {
		TestTab tab = new TestTab("t", false);
		assertNotNull(tab.getContentTable());
		assertSame(tab.getContentTable(), tab.getContentTable());
	}
}
