package com.badlogic.gdx.graphics.g3d.utils

import com.badlogic.gdx.graphics.g3d.model.Animation
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AnimationDescTest {
    private lateinit var anim: AnimationDesc

    @Before
    fun setup() {
        anim = AnimationDesc()
        anim.animation = Animation()
        anim.duration = 1f
        anim.listener = null
        anim.loopCount = 1
        anim.offset = 0f
        anim.speed = 1f
        anim.time = 0f
    }

    @Test
    fun testUpdateNominal() {
        Assert.assertEquals(-1f, anim.update(.75f), EPSILON)
        Assert.assertEquals(.5f, anim.update(.75f), EPSILON)
        Assert.assertEquals(.75f, anim.update(.75f), EPSILON)
    }

    @Test
    fun testUpdateJustEnd() {
        Assert.assertEquals(-1f, anim.update(.5f), EPSILON)
        Assert.assertEquals(0f, anim.update(.5f), EPSILON)
        Assert.assertEquals(.5f, anim.update(.5f), EPSILON)
    }

    @Test
    fun testUpdateBigDelta() {
        Assert.assertEquals(4.2f, anim.update(5.2f), EPSILON)
        Assert.assertEquals(7.3f, anim.update(7.3f), EPSILON)
    }

    @Test
    fun testUpdateZeroDelta() {
        Assert.assertEquals(-1f, anim.update(0f), EPSILON)
        Assert.assertEquals(0f, anim.time, EPSILON)
    }

    @Test
    fun testUpdateReverseNominal() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1f, anim.update(.75f), EPSILON)
        Assert.assertEquals(.5f, anim.update(.75f), EPSILON)
        Assert.assertEquals(.75f, anim.update(.75f), EPSILON)
    }

    @Test
    fun testUpdateReverseJustEnd() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1f, anim.update(.5f), EPSILON)
        Assert.assertEquals(0f, anim.update(.5f), EPSILON)
        Assert.assertEquals(.5f, anim.update(.5f), EPSILON)
    }

    @Test
    fun testUpdateReverseBigDelta() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(4.2f, anim.update(5.2f), EPSILON)
        Assert.assertEquals(7.3f, anim.update(7.3f), EPSILON)
    }

    @Test
    fun testUpdateReverseZeroDelta() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1f, anim.update(0f), EPSILON)
        Assert.assertEquals(anim.duration, anim.time, EPSILON)
    }

    companion object {
        private const val EPSILON = 1e-6F
    }
}
