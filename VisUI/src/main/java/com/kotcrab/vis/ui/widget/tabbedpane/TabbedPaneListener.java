package com.kotcrab.vis.ui.widget.tabbedpane;

/**
 * Listener used to get events from {@link TabbedPane}.
 */
public interface TabbedPaneListener {
    /**
     * Called when TabbedPane switched to new tab.
     *
     * @param tab that TabbedPane switched to. May be null if all tabs were disabled or if {@link TabbedPane#setAllowTabDeselect(boolean)} was set to
     *            true and all tabs were deselected.
     */
    void switchedTab(Tab tab);

    /**
     * Called when Tab was removed TabbedPane.
     *
     * @param tab that was removed.
     */
    void removedTab(Tab tab);

    /**
     * Called when all tabs were removed from TabbedPane.
     */
    void removedAllTabs();
}
