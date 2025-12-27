package com.mbrlabs.mundus.editor.history.commands

import com.badlogic.gdx.math.Vector3
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.history.Command
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent

class ScaleCommand(private var go: GameObject?) : Command {

    companion object {
        private val modEvent = GameObjectModifiedEvent(null)
    }

    private var before: Vector3 = Vector3()
    private var after: Vector3 = Vector3()

    fun setBefore(before: Vector3) {
        this.before.set(before)
    }

    fun setAfter(after: Vector3) {
        this.after.set(after)
    }

    fun setGo(go: GameObject) {
        this.go = go
    }

    override fun execute() {
        go!!.setLocalScale(after.x, after.y, after.z)
        modEvent.gameObject = go
        Mundus.postEvent(modEvent)
    }

    override fun undo() {
        go!!.setLocalScale(before.x, before.y, before.z)
        modEvent.gameObject = go
        Mundus.postEvent(modEvent)
    }

}