package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ArrowShapeBuilder
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

class Compass(private var worldCam: Camera?) : Disposable {

    private val ARROW_LENGTH = 0.05f
    private val ARROW_THIKNESS = 0.4f
    private val ARROW_CAP_SIZE = 0.2f
    private val ARROW_DIVISIONS = 5

    private val ownCam: PerspectiveCamera = PerspectiveCamera()
    private val compassModel: Model
    private val compassInstance: ModelInstance

    private val tempV3 = Vector3()

    init {

        val modelBuilder = ModelBuilder()
        modelBuilder.begin()

        val builder = modelBuilder.part(
            "compass", GL20.GL_TRIANGLES,
            (VertexAttributes.Usage.Position or VertexAttributes.Usage.ColorUnpacked).toLong(), Material()
        )
        builder.setColor(Color.RED)
        ArrowShapeBuilder.build(builder, 0f, 0f, 0f, ARROW_LENGTH, 0f, 0f, ARROW_CAP_SIZE, ARROW_THIKNESS, ARROW_DIVISIONS)
        builder.setColor(Color.GREEN)
        ArrowShapeBuilder.build(builder, 0f, 0f, 0f, 0f, ARROW_LENGTH, 0f, ARROW_CAP_SIZE, ARROW_THIKNESS, ARROW_DIVISIONS)
        builder.setColor(Color.BLUE)
        ArrowShapeBuilder.build(builder, 0f, 0f, 0f, 0f, 0f, ARROW_LENGTH, ARROW_CAP_SIZE, ARROW_THIKNESS, ARROW_DIVISIONS)
        compassModel = modelBuilder.end()
        compassInstance = ModelInstance(compassModel)

        // translate to top left corner
        compassInstance.transform.translate(0.93f, 0.94f, 0f)
    }

    fun setWorldCam(cam: Camera) {
        this.worldCam = cam
    }

    fun render(batch: ModelBatch, env: Environment) {
        update()
        batch.begin(ownCam)
        batch.render(compassInstance, env)
        batch.end()
    }

    private fun update() {
        compassInstance.transform.getTranslation(tempV3)
        compassInstance.transform.set(worldCam!!.view)
        compassInstance.transform.setTranslation(tempV3)
    }

    override fun dispose() {
        compassModel.dispose()
    }

}
