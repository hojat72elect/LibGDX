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
        anim.duration = 1F
        anim.listener = null
        anim.loopCount = 1
        anim.offset = 0F
        anim.speed = 1F
        anim.time = 0F
    }

    @Test
    fun testUpdateNominal() {
        Assert.assertEquals(-1F, anim.update(.75f), EPSILON)
        Assert.assertEquals(.5F, anim.update(.75f), EPSILON)
        Assert.assertEquals(.75F, anim.update(.75f), EPSILON)
    }

    @Test
    fun testUpdateJustEnd() {
        Assert.assertEquals(-1F, anim.update(.5f), EPSILON)
        Assert.assertEquals(0F, anim.update(.5f), EPSILON)
        Assert.assertEquals(.5F, anim.update(.5f), EPSILON)
    }

    @Test
    fun testUpdateBigDelta() {
        Assert.assertEquals(4.2F, anim.update(5.2f), EPSILON)
        Assert.assertEquals(7.3F, anim.update(7.3f), EPSILON)
    }

    @Test
    fun testUpdateZeroDelta() {
        Assert.assertEquals(-1F, anim.update(0f), EPSILON)
        Assert.assertEquals(0F, anim.time, EPSILON)
    }

    @Test
    fun testUpdateReverseNominal() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1F, anim.update(.75f), EPSILON)
        Assert.assertEquals(.5F, anim.update(.75f), EPSILON)
        Assert.assertEquals(.75F, anim.update(.75f), EPSILON)
    }

    @Test
    fun testUpdateReverseJustEnd() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1F, anim.update(.5f), EPSILON)
        Assert.assertEquals(0F, anim.update(.5f), EPSILON)
        Assert.assertEquals(.5F, anim.update(.5f), EPSILON)
    }

    @Test
    fun testUpdateReverseBigDelta() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(4.2F, anim.update(5.2f), EPSILON)
        Assert.assertEquals(7.3F, anim.update(7.3f), EPSILON)
    }

    @Test
    fun testUpdateReverseZeroDelta() {
        anim.speed = -1f
        anim.time = anim.duration

        Assert.assertEquals(-1F, anim.update(0f), EPSILON)
        Assert.assertEquals(anim.duration, anim.time, EPSILON)
    }

    companion object {
        private const val EPSILON = 1e-6F
    }
}
