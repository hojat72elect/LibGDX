package com.kotcrab.vis.ui.util.dialog;

/**
 * Used to get events from {@link Dialogs} confirm dialog.
 */
public interface ConfirmDialogListener<T> {
    /**
     * Called when dialog button was pressed, type of results is generic and depends on created dialog.
     */
    void result(T result);
}
