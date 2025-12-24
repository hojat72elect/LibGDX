package com.talosvfx.talos.editor.addons.scene.apps;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public abstract class AEditorApp<T> {

    protected Table content;
    protected String identifier;
    protected T object;

    protected Array<AppListener> appListeners = new Array<>();

    public AEditorApp(T object) {
        this.object = object;
    }

    public void onHide() {

    }

    public boolean notifyClose() {

        for (AppListener appListener : appListeners) {
            appListener.closeRequested();
        }

        return true;
    }

    public abstract void initContent();

    public abstract String getTitle();

    public Table getContent() {
        return content;
    }

    public void addAppListener(AppListener listener) {
        this.appListeners.add(listener);
    }

    public enum AppOpenStrategy {
        WINDOW,
        BOTTOM_TAB,
        RIGHT_TAB
    }

    public interface AppListener {
        void closeRequested();
    }
}
