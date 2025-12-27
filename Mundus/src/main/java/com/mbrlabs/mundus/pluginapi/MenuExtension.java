package com.mbrlabs.mundus.pluginapi;

import com.mbrlabs.mundus.pluginapi.ui.RootWidget;
import org.pf4j.ExtensionPoint;

public interface MenuExtension extends ExtensionPoint {

    /**
     * @return The item name of plugin in Plugins menu.
     */
    String getMenuName();

    /**
     * Here can setup root widget for plugin.
     */
    void setupDialogRootWidget(RootWidget rootWidget);
}
