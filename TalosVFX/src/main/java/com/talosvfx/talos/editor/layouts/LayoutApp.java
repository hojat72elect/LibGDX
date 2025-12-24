package com.talosvfx.talos.editor.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface LayoutApp {

//	change tis to focus

    boolean isTabActive();

    void setTabActive(boolean active);

    boolean isTabFocused();

    void setTabFocused(boolean focused);

    String getUniqueIdentifier();

    void setUniqueIdentifier(String uuid);

    String getFriendlyName();

    Actor getTabWidget();

    Actor copyTabWidget();

    Actor getMainContent();

    Actor getCopyMainContent();

    DestroyCallback getDestroyCallback();

    void setDestroyCallback(DestroyCallback destroyCallback);

    void setScrollFocus();

    void onInputProcessorAdded();

    void onInputProcessorRemoved();

    void updateTabName(String name);

    LayoutContent getLayoutContent();

    void setLayoutContent(LayoutContent layoutContent);

    void actInBackground(float delta);

    boolean hasPreferredWidth();

    boolean hasPreferredHeight();
}
