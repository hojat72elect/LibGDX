package com.kotcrab.vis.ui.util.dialog;

/**
 * Used to get events from {@link Dialogs} input dialog.
 */
public interface InputDialogListener {
    /**
     * Called when input dialog has finished.
     *
     * @param input text entered by user.
     */
    void finished(String input);

    /**
     * Called when user canceled dialog or pressed 'close' button. This won't be ever called if dialog is not
     * cancelable.
     */
    void canceled();
}
