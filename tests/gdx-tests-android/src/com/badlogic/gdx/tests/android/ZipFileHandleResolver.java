package com.badlogic.gdx.tests.android;

import androidx.annotation.NonNull;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.backends.android.AndroidZipFileHandle;
import com.badlogic.gdx.files.FileHandle;

public class ZipFileHandleResolver implements FileHandleResolver {

    @NonNull
    @Override
    public FileHandle resolve(@NonNull String fileName) {
        return new AndroidZipFileHandle(fileName);
    }
}
