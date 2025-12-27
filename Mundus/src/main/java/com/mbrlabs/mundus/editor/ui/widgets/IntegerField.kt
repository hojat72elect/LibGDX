package com.mbrlabs.mundus.editor.ui.widgets

import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter
import com.kotcrab.vis.ui.widget.VisTextField

class IntegerField : VisTextField {

    constructor(allowNegative: Boolean) {
        textFieldFilter = IntDigitsOnlyFilter(allowNegative)
    }

    constructor() : super() {
        textFieldFilter = IntDigitsOnlyFilter(true)
    }

    fun isValid(): Boolean {
        // A failsafe catchall to see if its valid
        try {
            Integer.parseInt(text)
            isInputValid = true
            return true
        } catch (ex: NumberFormatException) {
            isInputValid = false
            return false
        }
    }

    val int: Int
        get() {
            if (isValid()) {
                return Integer.parseInt(text)
            }
            return 0
        }
}