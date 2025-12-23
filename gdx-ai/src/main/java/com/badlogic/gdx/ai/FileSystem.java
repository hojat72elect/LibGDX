package com.badlogic.gdx.ai;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

/**
 * The {@code FileSystem} interface exposes the underlying file system(s).
 */
public interface FileSystem {

    FileHandleResolver newResolver(FileType fileType);

    FileHandle newFileHandle(String fileName);

    FileHandle newFileHandle(File file);

    FileHandle newFileHandle(String fileName, FileType type);

    FileHandle newFileHandle(File file, FileType type);
}
