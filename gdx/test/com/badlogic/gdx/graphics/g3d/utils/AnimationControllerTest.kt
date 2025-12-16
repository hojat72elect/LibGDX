package com.badlogic.gdx.graphics.g3d.utils

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.model.Animation
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc
import com.badlogic.gdx.utils.Array
import org.junit.Assert
import org.junit.Test

class AnimationControllerTest {

    @Test
    fun testGetFirstKeyframeIndexAtTimeNominal() {
        val keyFrames = Array<NodeKeyframe<String>>()
        keyFrames.add(NodeKeyframe<String>(0f, "1st"))
        keyFrames.add(NodeKeyframe<String>(3f, "2nd"))
        keyFrames.add(NodeKeyframe<String>(12f, "3rd"))
        keyFrames.add(NodeKeyframe<String>(13f, "4th"))
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, -1f).toLong())
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 0f).toLong())
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 2f).toLong())
        Assert.assertEquals(1, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 9f).toLong())
        Assert.assertEquals(2, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 12.5f).toLong())
        Assert.assertEquals(2, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 13f).toLong())
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 14f).toLong())
    }

    @Test
    fun testGetFirstKeyframeIndexAtTimeSingleKey() {
        val keyFrames = Array<NodeKeyframe<String>>()
        keyFrames.add(NodeKeyframe<String>(10f, "1st"))
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 9f).toLong())
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 10f).toLong())
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 11f).toLong())
    }

    @Test
    fun testGetFirstKeyframeIndexAtTimeEmpty() {
        val keyFrames = Array<NodeKeyframe<String>>()
        Assert.assertEquals(0, BaseAnimationController.getFirstKeyframeIndexAtTime(keyFrames, 3f).toLong())
    }

    @Test
    fun testEndUpActionAtDurationTime() {
        val loop = Animation()
        loop.id = "loop"
        loop.duration = 1f
        val action = Animation()
        action.id = "action"
        action.duration = 0.2f
        val modelInstance = ModelInstance(Model())
        modelInstance.animations.add(loop)
        modelInstance.animations.add(action)
        val animationController = AnimationController(modelInstance)
        animationController.setAnimation("loop", -1)
        assertSameAnimation(loop, animationController.current)
        animationController.update(1f)
        assertSameAnimation(loop, animationController.current)
        animationController.update(0.01f)
        assertSameAnimation(loop, animationController.current)
        animationController.action("action", 1, 1f, null, 0f)
        assertSameAnimation(action, animationController.current)
        animationController.update(0.2f)
        assertSameAnimation(loop, animationController.current)
    }

    @Test
    fun testEndUpActionAtDurationTimeReverse() {
        val loop = Animation()
        loop.id = "loop"
        loop.duration = 1f
        val action = Animation()
        action.id = "action"
        action.duration = 0.2f
        val modelInstance = ModelInstance(Model())
        modelInstance.animations.add(loop)
        modelInstance.animations.add(action)
        val animationController = AnimationController(modelInstance)
        animationController.setAnimation("loop", -1, -1f, null)
        assertSameAnimation(loop, animationController.current)
        animationController.update(1f)
        assertSameAnimation(loop, animationController.current)
        animationController.update(0.01f)
        assertSameAnimation(loop, animationController.current)
        animationController.action("action", 1, -1f, null, 0f)
        assertSameAnimation(action, animationController.current)
        animationController.update(0.2f)
        assertSameAnimation(loop, animationController.current)
    }

    companion object {
        private fun assertSameAnimation(expected: Animation, actual: AnimationDesc) {
            if (expected.id != actual.animation.id) {
                Assert.fail("expected: " + expected.id + ", actual: " + actual.animation.id)
            }
        }
    }
}
