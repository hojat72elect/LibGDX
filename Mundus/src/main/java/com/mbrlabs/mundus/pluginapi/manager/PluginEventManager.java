package com.mbrlabs.mundus.pluginapi.manager;

import com.mbrlabs.mundus.editorcommons.EventListener;

public interface PluginEventManager {

    /**
     * Registers event listeners in Editor.
     *
     * @param listener The event listener.
     */
    void registerEventListener(EventListener listener);
}
