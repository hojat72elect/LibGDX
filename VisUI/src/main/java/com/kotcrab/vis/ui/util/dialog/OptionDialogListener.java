package com.kotcrab.vis.ui.util.dialog;

/**
 * Used to get events from {@link Dialogs} option dialog.
 */
public interface OptionDialogListener {
    /**
     * Called when 'yes' button was pressed.
     */
    void yes();

    /**
     * Called when 'no' button was pressed.
     */
    void no();

    /**
     * Called when 'cancel' button was pressed.
     */
    void cancel();
}
