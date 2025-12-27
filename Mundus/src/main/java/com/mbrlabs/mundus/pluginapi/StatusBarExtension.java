package com.mbrlabs.mundus.pluginapi;

import com.mbrlabs.mundus.pluginapi.ui.LabelWidget;
import org.pf4j.ExtensionPoint;

public interface StatusBarExtension extends ExtensionPoint {

    /**
     * @param label The creates label widget on status bar.
     */
    void createStatusBarLabel(LabelWidget label);
}
