package com.kotcrab.vis.ui;

/**
 * Implemented by objects that can acquire VisUI focus.
 *
 * @see FocusManager
 */
public interface Focusable {
    /**
     * Called by VisUI when object lost focus. Don not cally manually, see {@link FocusManager}.
     */
    void focusLost();

    /**
     * Called by VisUI when object gained focus. Don not cally manually, see {@link FocusManager}.
     */
    void focusGained();
}
