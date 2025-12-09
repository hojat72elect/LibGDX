package com.badlogic.gdx.utils

import com.badlogic.gdx.files.FileHandle
import java.io.InputStream

interface BaseJsonReader {

    fun parse(input: InputStream): JsonValue
    fun parse(file: FileHandle): JsonValue
}
