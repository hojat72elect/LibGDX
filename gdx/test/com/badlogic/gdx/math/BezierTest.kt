package com.badlogic.gdx.math

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import kotlin.math.sqrt
import com.badlogic.gdx.utils.Array as GdxArray

@RunWith(Parameterized::class)
class BezierTest {

    enum class ImportType {
        LibGDXArrays, JavaArrays, JavaVarArgs
    }

    @JvmField
    @Parameter(0)
    var type: ImportType? = null

    /** use constructor or setter  */
    @JvmField
    @Parameter(1)
    var useSetter: Boolean = false

    private var bezier: Bezier<Vector2>? = null

    @Before
    fun setup() {
        bezier = null
    }

    private fun create(points: Array<Vector2>): Array<Vector2> {
        val b: Bezier<Vector2>
        if (useSetter) {
            b = Bezier()
            when (type) {
                ImportType.LibGDXArrays -> b.set(GdxArray(points), 0, points.size)
                ImportType.JavaArrays -> b.set(points, 0, points.size)
                else -> b.set(*points)
            }
        } else {
            b = when (type) {
                ImportType.LibGDXArrays -> Bezier(GdxArray(points), 0, points.size)
                ImportType.JavaArrays -> Bezier(points, 0, points.size)
                else -> Bezier(*points)
            }
        }
        bezier = b
        return points
    }

    @Test
    fun testLinear2D() {
        create(arrayOf(Vector2(0f, 0f), Vector2(1f, 1f)))

        val len = bezier!!.approxLength(2)
        Assert.assertEquals(sqrt(2.0), len.toDouble(), EPSILON_APPROXIMATIONS.toDouble())

        val d = bezier!!.derivativeAt(Vector2(), 0.5f)
        Assert.assertEquals(1.0, d.x.toDouble(), EPSILON.toDouble())
        Assert.assertEquals(1.0, d.y.toDouble(), EPSILON.toDouble())

        val v = bezier!!.valueAt(Vector2(), 0.5f)
        Assert.assertEquals(0.5, v.x.toDouble(), EPSILON.toDouble())
        Assert.assertEquals(0.5, v.y.toDouble(), EPSILON.toDouble())

        val t = bezier!!.approximate(Vector2(.5f, .5f))
        Assert.assertEquals(.5, t.toDouble(), EPSILON_APPROXIMATIONS.toDouble())

        val l = bezier!!.locate(Vector2(.5f, .5f))
        Assert.assertEquals(.5, l.toDouble(), EPSILON.toDouble())
    }

    companion object {
        private const val EPSILON = java.lang.Float.MIN_NORMAL
        private const val EPSILON_APPROXIMATIONS = 1e-6f

        @JvmStatic
        @Parameters(name = "imported type {0} use setter {1}")
        fun parameters(): Collection<Array<Any>> {
            val parameters = ArrayList<Array<Any>>()
            for (type in ImportType.values()) {
                parameters.add(arrayOf(type, true))
                parameters.add(arrayOf(type, false))
            }
            return parameters
        }
    }
}
