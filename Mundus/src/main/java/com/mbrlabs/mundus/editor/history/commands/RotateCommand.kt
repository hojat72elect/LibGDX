package com.mbrlabs.mundus.editor.history.commands

import com.badlogic.gdx.math.Quaternion
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.history.Command
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent

class RotateCommand(private val go: GameObject) : Command {

    companion object {
        private val modEvent = GameObjectModifiedEvent(null)
    }

    private var before: Quaternion = Quaternion()
    private var after: Quaternion = Quaternion()

    fun setBefore(before: Quaternion) {
        this.before.set(before)
    }

    fun setAfter(after: Quaternion) {
        this.after.set(after)
    }

    override fun execute() {
        go.setLocalRotation(after.x, after.y, after.z, after.w)
        modEvent.gameObject = go
        Mundus.postEvent(modEvent)
    }

    override fun undo() {
        go.setLocalRotation(before.x, before.y, before.z, before.w)
        modEvent.gameObject = go
        Mundus.postEvent(modEvent)
    }

}
