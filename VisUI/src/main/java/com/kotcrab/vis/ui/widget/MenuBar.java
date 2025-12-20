package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;

/**
 * Bar with expandable menus available after pressing button, usually displayed on top of the stage.
 */
public class MenuBar {
    private final Table mainTable;
    private final Table menuItems;

    private Menu currentMenu;
    private final Array<Menu> menus = new Array<Menu>();

    private MenuBarListener menuListener;

    public MenuBar() {
        this("default");
    }

    public MenuBar(String styleName) {
        this(VisUI.getSkin().get(styleName, MenuBarStyle.class));
    }

    public MenuBar(MenuBarStyle style) {
        menuItems = new VisTable();

        mainTable = new VisTable() {
            @Override
            protected void sizeChanged() {
                super.sizeChanged();
                closeMenu();
            }
        };

        mainTable.left();
        mainTable.add(menuItems);
        mainTable.setBackground(style.background);
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        menu.setMenuBar(this);
        menuItems.add(menu.getOpenButton());
    }

    public boolean removeMenu(Menu menu) {
        boolean removed = menus.removeValue(menu, true);

        if (removed) {
            menu.setMenuBar(null);
            menuItems.removeActor(menu.getOpenButton());
        }

        return removed;
    }

    public void insertMenu(int index, Menu menu) {
        menus.insert(index, menu);
        menu.setMenuBar(this);
        rebuild();
    }

    private void rebuild() {
        menuItems.clear();
        for (Menu menu : menus)
            menuItems.add(menu.getOpenButton());
    }

    /**
     * Closes currently opened menu (if any). Used by framework and typically there is no need to call this manually
     */
    public void closeMenu() {
        if (currentMenu != null) {
            currentMenu.deselectButton();
            currentMenu.remove();
            currentMenu = null;
        }
    }

    Menu getCurrentMenu() {
        return currentMenu;
    }

    void setCurrentMenu(Menu newMenu) {
        if (currentMenu == newMenu) return;
        if (currentMenu != null) {
            currentMenu.deselectButton();
            if (menuListener != null) menuListener.menuClosed(currentMenu);
        }
        if (newMenu != null) {
            newMenu.selectButton();
            if (menuListener != null) menuListener.menuOpened(newMenu);
        }
        currentMenu = newMenu;
    }

    public void setMenuListener(MenuBarListener menuListener) {
        this.menuListener = menuListener;
    }

    /**
     * Returns table containing all menus that should be added to Stage, typically with expandX and fillX properties.
     */
    public Table getTable() {
        return mainTable;
    }

    public interface MenuBarListener {
        void menuOpened(Menu menu);

        void menuClosed(Menu menu);
    }

    public static class MenuBarStyle {
        public Drawable background;

        public MenuBarStyle() {
        }

        public MenuBarStyle(MenuBarStyle style) {
            this.background = style.background;
        }

        public MenuBarStyle(Drawable background) {
            this.background = background;
        }
    }
}
