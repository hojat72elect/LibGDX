package com.badlogic.gdx.utils

import com.badlogic.gdx.files.FileHandle
import java.io.InputStream

/**
 * Info : This interface was moved to Kerman game engine.
 */
interface BaseJsonReader {

    fun parse(input: InputStream): JsonValue
    fun parse(file: FileHandle): JsonValue
}
