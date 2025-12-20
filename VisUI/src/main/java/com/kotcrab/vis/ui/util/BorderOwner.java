package com.kotcrab.vis.ui.util;

/**
 * Implemented by actors that has VisUI focus border, actor implementing this interface must support disabling its border.
 */
public interface BorderOwner {
    boolean isFocusBorderEnabled();

    void setFocusBorderEnabled(boolean focusBorderEnabled);
}
