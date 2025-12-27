package com.mbrlabs.mundus.editor.core.io;

/**
 * Provider for IOManager. Needed because dependency injection with interfaces does
 * not seem to work, so we provide this POJO to inject the IOManager.
 */
public class IOManagerProvider {
    private final IOManager ioManager;

    public IOManagerProvider(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public IOManager getIOManager() {
        return ioManager;
    }
}
