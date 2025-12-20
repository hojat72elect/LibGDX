package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;

/**
 * Menu used in {@link MenuBar}, it is a standard {@link PopupMenu} with tittle displayed in MenuBar.
 */
public class Menu extends PopupMenu {
    public VisTextButton openButton;
    public Drawable buttonDefault;
    private MenuBar menuBar;
    private final String title;

    public Menu(String title) {
        this(title, "default");
    }

    public Menu(String title, String styleName) {
        this(title, VisUI.getSkin().get(styleName, MenuStyle.class));
    }

    public Menu(String title, MenuStyle style) {
        super(style);
        this.title = title;

        openButton = new VisTextButton(title, new VisTextButtonStyle(style.openButtonStyle));
        buttonDefault = openButton.getStyle().up;

        openButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (menuBar.getCurrentMenu() == Menu.this) {
                    menuBar.closeMenu();
                    return true;
                }

                switchMenu();
                event.stop();
                return true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (menuBar.getCurrentMenu() != null && menuBar.getCurrentMenu() != Menu.this) switchMenu();
            }
        });
    }

    public String getTitle() {
        return title;
    }

    private void switchMenu() {
        menuBar.closeMenu();
        showMenu();
    }

    private void showMenu() {
        Vector2 pos = openButton.localToStageCoordinates(new Vector2(0, 0));
        setPosition(pos.x, pos.y - getHeight());
        openButton.getStage().addActor(this);
        menuBar.setCurrentMenu(this);
    }

    @Override
    public boolean remove() {
        boolean result = super.remove();
        menuBar.setCurrentMenu(null);
        return result;
    }

    /**
     * Called by MenuBar when this menu is added to it
     */
    void setMenuBar(MenuBar menuBar) {
        if (this.menuBar != null && menuBar != null) throw new IllegalStateException("Menu was already added to MenuBar");
        this.menuBar = menuBar;
    }

    TextButton getOpenButton() {
        return openButton;
    }

    void selectButton() {
        openButton.getStyle().up = openButton.getStyle().over;
    }

    void deselectButton() {
        openButton.getStyle().up = buttonDefault;
    }

    public static class MenuStyle extends PopupMenuStyle {
        public VisTextButtonStyle openButtonStyle;

        public MenuStyle() {
        }

        public MenuStyle(MenuStyle style) {
            super(style);
            this.openButtonStyle = style.openButtonStyle;
        }

        public MenuStyle(Drawable background, Drawable border, VisTextButtonStyle openButtonStyle) {
            super(background, border);
            this.openButtonStyle = openButtonStyle;
        }
    }
}
