package com.badlogic.gdx.scenes.scene2d.actions

/**
 * An interface for actions that can be finished manually.
 */
interface FinishableAction {

    /**
     * Manually finishes the action, performing necessary finalization steps.
     */
    fun finish()
}
