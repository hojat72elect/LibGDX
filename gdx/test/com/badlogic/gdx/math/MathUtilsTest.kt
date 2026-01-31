package com.badlogic.gdx.math

import org.junit.Assert
import org.junit.Test
import kotlin.math.tan
import kotlin.math.withSign

class MathUtilsTest {
    @Test
    fun lerpAngle() {
        Assert.assertEquals(MathUtils.PI / 18F, MathUtils.lerpAngle(MathUtils.PI / 18F, MathUtils.PI / 6F, 0.0F), 0.01F)
        Assert.assertEquals(MathUtils.PI / 9F, MathUtils.lerpAngle(MathUtils.PI / 18F, MathUtils.PI / 6F, 0.5F), 0.01F)
        Assert.assertEquals(MathUtils.PI / 6F, MathUtils.lerpAngle(MathUtils.PI / 18F, MathUtils.PI / 6F, 1.0F), 0.01F)

        // checks both negative c, which should produce a result close to HALF_PI, and
        // positive c, which should be close to PI + HALF_PI.
        // intentionally skips where c == 0, because there are two equally-valid results for that case.
        var c = -1F
        while (c <= 1F) {
            Assert.assertEquals(MathUtils.PI + MathUtils.HALF_PI.withSign(c) + c, MathUtils.lerpAngle(0F, MathUtils.PI2 + MathUtils.PI + c + c, 0.5F), 0.01F)
            Assert.assertEquals(MathUtils.PI + MathUtils.HALF_PI.withSign(c) + c, MathUtils.lerpAngle(MathUtils.PI2 + MathUtils.PI + c + c, 0F, 0.5F), 0.01F)
            c += 0.003f
        }
    }

    @Test
    fun lerpAngleDeg() {
        Assert.assertEquals(10F, MathUtils.lerpAngleDeg(10F, 30F, 0.0F), 0.01F)
        Assert.assertEquals(20F, MathUtils.lerpAngleDeg(10F, 30F, 0.5F), 0.01F)
        Assert.assertEquals(30F, MathUtils.lerpAngleDeg(10F, 30F, 1.0F), 0.01F)

        // checks both negative c, which should produce a result close to 90, and
        // positive c, which should be close to 270.
        // intentionally skips where c == 0, because there are two equally-valid results for that case.
        var c = -80F
        while (c <= 80F) {
            Assert.assertEquals(180F + 90F.withSign(c) + c, MathUtils.lerpAngleDeg(0F, 540 + c + c, 0.5F), 0.01F)
            Assert.assertEquals(180F + 90F.withSign(c) + c, MathUtils.lerpAngleDeg(540 + c + c, 0F, 0.5F), 0.01F)
            c += 0.3f
        }
    }

    @Test
    fun lerpAngleDegCrossingZero() {
        Assert.assertEquals(350F, MathUtils.lerpAngleDeg(350F, 10F, 0.0F), 0.01F)
        Assert.assertEquals(0F, MathUtils.lerpAngleDeg(350F, 10F, 0.5F), 0.01F)
        Assert.assertEquals(10F, MathUtils.lerpAngleDeg(350F, 10F, 1.0F), 0.01F)
    }

    @Test
    fun lerpAngleDegCrossingZeroBackwards() {
        Assert.assertEquals(10F, MathUtils.lerpAngleDeg(10F, 350F, 0.0F), 0.01F)
        Assert.assertEquals(0F, MathUtils.lerpAngleDeg(10F, 350F, 0.5F), 0.01F)
        Assert.assertEquals(350F, MathUtils.lerpAngleDeg(10F, 350F, 1.0F), 0.01F)
    }

    @Test
    fun testNorm() {
        Assert.assertEquals(-1.0F, MathUtils.norm(10F, 20F, 0F), 0.01F)
        Assert.assertEquals(0.0F, MathUtils.norm(10F, 20F, 10F), 0.01F)
        Assert.assertEquals(0.5F, MathUtils.norm(10F, 20F, 15F), 0.01F)
        Assert.assertEquals(1.0F, MathUtils.norm(10F, 20F, 20F), 0.01F)
        Assert.assertEquals(2.0F, MathUtils.norm(10F, 20F, 30F), 0.01F)
    }

    @Test
    fun testMap() {
        Assert.assertEquals(0F, MathUtils.map(10F, 20F, 100F, 200F, 0F), 0.01F)
        Assert.assertEquals(100F, MathUtils.map(10F, 20F, 100F, 200F, 10F), 0.01F)
        Assert.assertEquals(150F, MathUtils.map(10F, 20F, 100F, 200F, 15F), 0.01F)
        Assert.assertEquals(200F, MathUtils.map(10F, 20F, 100F, 200F, 20F), 0.01F)
        Assert.assertEquals(300F, MathUtils.map(10F, 20F, 100F, 200F, 30F), 0.01F)
    }

    @Test
    fun testRandomLong() {
        var r: Long
        for (i in 0..511) {
            Assert.assertTrue((MathUtils.random(1L, 5L).also { r = it }) >= 1L && r <= 5L)
            Assert.assertTrue((MathUtils.random(6L, 1L).also { r = it }) >= 1L && r <= 6L)
            Assert.assertTrue((MathUtils.random(-1L, -7L).also { r = it }) <= -1L && r >= -7L)
            Assert.assertTrue((MathUtils.random(-8L, -1L).also { r = it }) <= -1L && r >= -8L)
        }
    }

    @Test
    fun testSinDeg() {
        Assert.assertEquals(0F, MathUtils.sinDeg(0F), 0F)
        Assert.assertEquals(1F, MathUtils.sinDeg(90F), 0F)
        Assert.assertEquals(0F, MathUtils.sinDeg(180F), 0F)
        Assert.assertEquals(-1F, MathUtils.sinDeg(270F), 0F)
    }

    @Test
    fun testCosDeg() {
        Assert.assertEquals(1F, MathUtils.cosDeg(0F), 0F)
        Assert.assertEquals(0F, MathUtils.cosDeg(90F), 0F)
        Assert.assertEquals(-1F, MathUtils.cosDeg(180F), 0F)
        Assert.assertEquals(0F, MathUtils.cosDeg(270F), 0F)
    }

    @Test
    fun testTanDeg() {
        Assert.assertEquals(0F, MathUtils.tanDeg(0F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(tan(Math.toRadians(45.0)), MathUtils.tanDeg(45F).toDouble(), MathUtils.FLOAT_ROUNDING_ERROR.toDouble())
        Assert.assertEquals(tan(Math.toRadians(135.0)), MathUtils.tanDeg(135F).toDouble(), MathUtils.FLOAT_ROUNDING_ERROR.toDouble())
        Assert.assertEquals(0F, MathUtils.tanDeg(180F), MathUtils.FLOAT_ROUNDING_ERROR)
    }

    @Test
    fun testAtan2Deg360() {
        Assert.assertEquals(0F, MathUtils.atan2Deg360(0F, 1F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(45F, MathUtils.atan2Deg360(1F, 1F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(90F, MathUtils.atan2Deg360(1F, 0F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(135F, MathUtils.atan2Deg360(1F, -1F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(180F, MathUtils.atan2Deg360(0F, -1F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(225F, MathUtils.atan2Deg360(-1F, -1F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(270F, MathUtils.atan2Deg360(-1F, 0F), MathUtils.FLOAT_ROUNDING_ERROR)
        Assert.assertEquals(315F, MathUtils.atan2Deg360(-1F, 1F), MathUtils.FLOAT_ROUNDING_ERROR)
    }
}
