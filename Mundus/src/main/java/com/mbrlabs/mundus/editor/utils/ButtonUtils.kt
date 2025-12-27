package com.mbrlabs.mundus.editor.utils

object ButtonUtils {

    fun buttonToString(buttonCode: Int): String {
        return when (buttonCode) {
            0 -> "LEFT"
            1 -> "RIGHT"
            2 -> "MIDDLE"
            3 -> "BACK"
            4 -> "FORWARD"
            else -> "UNKNOWN"
        }
    }

    fun buttonStringToButtonKey(text: String): Int {
        return when (text) {
            "LEFT" -> 0
            "RIGHT" -> 1
            "MIDDLE" -> 2
            "BACK" -> 3
            "FORWARD" -> 4
            else -> -1
        }
    }
}
