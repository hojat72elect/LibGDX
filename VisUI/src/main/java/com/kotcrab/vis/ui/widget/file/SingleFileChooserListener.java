package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;

/**
 * Implementation of {@link FileChooserListener} that can be used when user picks only one file. Provides convenient
 * {@link #selected(FileHandle)} method. If user picked more than one file (note that chooser must be in multiple select
 * mode for that to happen, see {@link FileChooser#setSelectionMode(SelectionMode)}), that method
 * will be called only for first selected file and remaining files will be ignored.
 *
 * @since 1.0.0
 */
public abstract class SingleFileChooserListener implements FileChooserListener {
    @Override
    public final void selected(Array<FileHandle> files) {
        selected(files.first());
    }

    /**
     * Called for first file in selection. See {@link SingleFileChooserListener}.
     */
    protected abstract void selected(FileHandle file);

    @Override
    public void canceled() {

    }
}
