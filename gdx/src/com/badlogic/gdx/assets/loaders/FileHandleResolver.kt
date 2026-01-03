package com.badlogic.gdx.assets.loaders

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle

/**
 * Info : This interface was moved to Kerman game engine.
 *
 * Interface for classes which can map a file name to a [FileHandle]. Used to allow the [AssetManager] to load
 * resources from anywhere or implement caching strategies.
 */
interface FileHandleResolver {
    fun resolve(fileName: String): FileHandle
}
