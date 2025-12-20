package com.kotcrab.vis.ui.widget.file.internal;

import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.widget.file.FileUtils;

public class FileHandleMetadata {
    private final String name;
    private final boolean directory;
    private final long lastModified;
    private final long length;
    private final String readableFileSize;

    private FileHandleMetadata(FileHandle file) {
        this.name = file.name();
        this.directory = file.isDirectory();
        this.lastModified = file.lastModified();
        this.length = file.length();
        this.readableFileSize = FileUtils.readableFileSize(length);
    }

    public static FileHandleMetadata of(FileHandle file) {
        return new FileHandleMetadata(file);
    }

    public String name() {
        return name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public long lastModified() {
        return lastModified;
    }

    public long length() {
        return length;
    }

    public String readableFileSize() {
        return readableFileSize;
    }
}
