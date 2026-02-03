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

class PositionLmlAttributeTest {

    private lateinit var attribute: PositionLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Actor

    @Before
    fun setUp() {
        attribute = PositionLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(Actor::class.java)
    }

    @Test
    fun `getHandledType returns Actor class`() {
        assertEquals(Actor::class.java, attribute.handledType)
    }

    @Test
    fun `process sets position correctly`() {
        val rawData = "10;20"
        `when`(parser.parseArray(rawData, actor)).thenReturn(arrayOf("10", "20"))

        attribute.process(parser, tag, actor, rawData)

        verify(actor).setPosition(10f, 20f)
    }

    @Test
    fun `process throws error when values are not numbers`() {
        val rawData = "x;y"
        `when`(parser.parseArray(rawData, actor)).thenReturn(arrayOf("x", "y"))

        attribute.process(parser, tag, actor, rawData)

        verify(parser).throwError(ArgumentMatchers.eq("Can't read position values from the array."), ArgumentMatchers.any(NumberFormatException::class.java))
    }
}
