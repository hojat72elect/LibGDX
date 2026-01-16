package com.badlogic.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/**
 * Info : This class was moved to Kerman game engine.
 */
public class ClasspathFileHandleResolver implements FileHandleResolver {
    @Override
    public FileHandle resolve(String fileName) {
        return Gdx.files.classpath(fileName);
    }
}
