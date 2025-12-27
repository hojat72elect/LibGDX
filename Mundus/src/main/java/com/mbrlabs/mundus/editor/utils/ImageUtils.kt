package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.TextureData
import com.mbrlabs.mundus.commons.utils.MathUtils

object ImageUtils {

    fun validateImageFile(fileHandle: FileHandle): String? {
        return validateImageFile(fileHandle, requireSquare = false, requirePowerOfTwo = false)
    }

    fun validateImageFile(fileHandle: FileHandle, requireSquare: Boolean, requirePowerOfTwo: Boolean): String? {
        if (!fileHandle.exists()) {
            return "File does not exist or unable to import."
        }
        if (!isImage(fileHandle)) {
            return "Format not supported. Supported formats: png, jpg, jpeg, tga."
        }

        var data: TextureData? = null

        if (requireSquare) {
            data = TextureData.Factory.loadFromFile(fileHandle, false)
            if (data?.getWidth() != data?.getHeight()) {
                return "Image must be square."
            }
        }

        if (requirePowerOfTwo) {
            if (data == null) {
                data = TextureData.Factory.loadFromFile(fileHandle, false)
            }
            if (!MathUtils.isPowerOfTwo(data!!.getWidth()) || !MathUtils.isPowerOfTwo(data.getHeight())) {
                return "Image dimensions must be a power of two."
            }
        }

        return null
    }
}