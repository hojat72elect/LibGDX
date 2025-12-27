package com.mbrlabs.mundus.editor.tools

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import com.mbrlabs.mundus.commons.env.MundusEnvironment
import com.mbrlabs.mundus.editor.tools.picker.PickerColorEncoder
import com.mbrlabs.mundus.editor.tools.picker.PickerIDAttribute

/**
 * A tool handle is the visual part of a tool (e.g. x arrow handle of the
 * translate tool).
 *
 * Handles, can be (much like game objects) picked, using the same (color coding
 * technique).
 */
abstract class ToolHandle(val id: Int) : Disposable {

    val position: Vector3 = Vector3()
    val rotationEuler: Vector3 = Vector3()
    val rotation: Quaternion = Quaternion()
    val scale: Vector3 = Vector3(1f, 1f, 1f)
    val idAttribute: PickerIDAttribute = PickerIDAttribute()
    val environment: Environment = MundusEnvironment()

    init {
        PickerColorEncoder.encodeRaypickColorId(id, idAttribute)
    }

    abstract fun render(batch: ModelBatch)
    abstract fun renderPick(modelBatch: ModelBatch)
    abstract fun act()
    abstract fun applyTransform()

}
