@file:JvmName("StringUtils")

package com.mbrlabs.mundus.editor.utils

import java.util.Locale


fun formatFloat(f: Float, digits: Int): String {
    return String.format(Locale.US, "%." + digits + "f", f)
}


