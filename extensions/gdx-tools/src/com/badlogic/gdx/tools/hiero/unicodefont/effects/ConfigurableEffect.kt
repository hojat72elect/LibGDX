package com.badlogic.gdx.tools.hiero.unicodefont.effects

/**
 * An effect that has a number of configuration values. This allows the effect to be configured in the Hiero GUI and to be saved
 * and loaded to and from a file.
 */
interface ConfigurableEffect : Effect {

    /**
     * Returns the list of [Value]s for this effect. This list is not typically backed by the effect, so changes to the
     * values will not take affect until [.setValues] is called.
     */
    fun getValues(): List<Value>

    /**
     * Sets the list of [Value]s for this effect.
     */
    fun setValues(values: List<*>)

    /**
     * Represents a configurable value for an effect.
     */
    interface Value {
        /**
         * Returns the name of the value.
         */
        fun getName(): String

        /**
         * Gets the string representation of the value.
         */
        fun getString(): String

        /**
         * Sets the string representation of the value.
         */
        fun setString(value: String)

        /**
         * Gets the object representation of the value.
         */
        fun getObject(): Any

        /**
         * Shows a dialog allowing a user to configure this value.
         */
        fun showDialog()
    }
}
