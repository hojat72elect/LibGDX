package com.mbrlabs.mundus.pluginapi;

import com.mbrlabs.mundus.pluginapi.manager.ToasterManager;
import org.pf4j.ExtensionPoint;

public interface ToasterExtension extends ExtensionPoint {

    void toasterManager(ToasterManager toasterManager);
}
