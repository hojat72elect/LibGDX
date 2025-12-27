package com.mbrlabs.mundus.editor.ui.widgets

import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter

class IntegerFieldWithLabel : TextFieldWithLabel {

    constructor(labelText: String, width: Int, allowNegative: Boolean) : super(labelText, width) {
        textField.textFieldFilter = IntDigitsOnlyFilter(allowNegative)
    }

    constructor(labelText: String, width: Int) : super(labelText, width) {
        textField.textFieldFilter = IntDigitsOnlyFilter(true)
    }

    fun isValid(): Boolean {
        // A failsafe catchall to see if its valid
        try {
            Integer.parseInt(textField.text)
            textField.isInputValid = true
            return true
        } catch (ex: NumberFormatException) {
            textField.isInputValid = false
            return false
        }
    }

    val int: Int
        get() {
            if (isValid()) {
                return Integer.parseInt(textField.text)
            }
            return 0
        }

}
