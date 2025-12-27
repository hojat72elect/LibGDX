package com.mbrlabs.mundus.pluginapi;

import com.mbrlabs.mundus.pluginapi.manager.AssetManager;
import org.pf4j.ExtensionPoint;

public interface AssetExtension extends ExtensionPoint {

    void assetManager(AssetManager assetManager);
}
