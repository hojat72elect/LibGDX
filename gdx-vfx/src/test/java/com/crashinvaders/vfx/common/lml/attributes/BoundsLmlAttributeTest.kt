package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class BoundsLmlAttributeTest {

    private lateinit var attribute: BoundsLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Actor

    @Before
    fun setUp() {
        attribute = BoundsLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = Actor()
    }

    @Test
    fun `getHandledType returns Actor class`() {
        assertEquals(Actor::class.java, attribute.handledType)
    }

    @Test
    fun `process sets bounds correctly`() {
        val rawData = "10;20;100;200"
        `when`(parser.parseArray(rawData, actor)).thenReturn(arrayOf("10", "20", "100", "200"))

        attribute.process(parser, tag, actor, rawData)

        assertEquals(10f, actor.x, 0.001f)
        assertEquals(20f, actor.y, 0.001f)
        assertEquals(100f, actor.width, 0.001f)
        assertEquals(200f, actor.height, 0.001f)
    }

    @Test
    fun `process throws error when values are not numbers`() {
        val rawData = "10;20;width;height"
        `when`(parser.parseArray(rawData, actor)).thenReturn(arrayOf("10", "20", "width", "height"))

        attribute.process(parser, tag, actor, rawData)

        verify(parser).throwError(ArgumentMatchers.eq("Can't read bounds values from the array."), ArgumentMatchers.any(NumberFormatException::class.java))
    }
}
