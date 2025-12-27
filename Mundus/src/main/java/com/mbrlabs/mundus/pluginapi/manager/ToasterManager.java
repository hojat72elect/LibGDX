package com.mbrlabs.mundus.pluginapi.manager;

import com.mbrlabs.mundus.editorcommons.types.ToastType;

public interface ToasterManager {

    /**
     * Shows toaster message as info status for some seconds.
     *
     * @param text The text.
     */
    void info(String text);

    /**
     * Shows toaster message as error status for some seconds.
     *
     * @param text The text.
     */
    void error(String text);

    /**
     * Shows toaster message as success status for some seconds.
     *
     * @param text The text.
     */
    void success(String text);

    /**
     * Shows toaster message until the user close it.
     *
     * @param type The type of text.
     * @param text The text.
     */
    void sticky(ToastType type, String text);

}
