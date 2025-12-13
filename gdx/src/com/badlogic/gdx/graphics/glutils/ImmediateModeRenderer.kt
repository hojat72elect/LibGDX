package com.badlogic.gdx.graphics.glutils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Matrix4

interface ImmediateModeRenderer {
    fun begin(projModelView: Matrix4?, primitiveType: Int)

    fun flush()

    fun color(color: Color?)

    fun color(r: Float, g: Float, b: Float, a: Float)

    fun color(colorBits: Float)

    fun texCoord(u: Float, v: Float)

    fun normal(x: Float, y: Float, z: Float)

    fun vertex(x: Float, y: Float, z: Float)

    fun end()

    fun getNumVertices(): Int

    fun getMaxVertices(): Int

    fun dispose()
}
