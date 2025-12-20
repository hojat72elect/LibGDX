package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Used to get events from {@link FileChooser}.
 */
public interface FileChooserListener {
    /**
     * Called when user finished selecting files. It is guaranteed that array will contain at least one file.
     */
    void selected(Array<FileHandle> files);

    /**
     * Called when selection dialog was canceled by user.
     */
    void canceled();
}
