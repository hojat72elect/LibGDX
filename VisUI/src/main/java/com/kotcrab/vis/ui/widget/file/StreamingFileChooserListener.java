package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Implementation of {@link FileChooserListener} that streams chooser selection. Provides convenient
 * {@link #selected(FileHandle)} method that will be called for every selected file after user finished choosing files.
 * Before streaming starts {@link #begin()} is called, after streaming has finished {@link #end()} is called.
 *
 *  @since 1.0.0
 */
public abstract class StreamingFileChooserListener implements FileChooserListener {
    @Override
    public final void selected(Array<FileHandle> files) {
        begin();

        for (FileHandle file : files) {
            selected(file);
        }

        end();
    }

    /**
     * Called after user finished selecting files. If user picked multiple files this will be called separately
     * for every selected file.
     */
    public abstract void selected(FileHandle file);

    /**
     * Called after user finished selecting files, before streaming started.
     */
    public void begin() {

    }

    /**
     * Called after file selection streaming has finished.
     */
    public void end() {

    }

    @Override
    public void canceled() {

    }
}
