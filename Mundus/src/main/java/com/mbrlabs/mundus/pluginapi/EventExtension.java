package com.mbrlabs.mundus.pluginapi;

import com.mbrlabs.mundus.pluginapi.manager.PluginEventManager;
import org.pf4j.ExtensionPoint;

public interface EventExtension extends ExtensionPoint {

    /**
     * Here can register events via plugin event manager.
     *
     * @param pluginEventManager The plugin event manager.
     */
    void manageEvents(PluginEventManager pluginEventManager);
}
