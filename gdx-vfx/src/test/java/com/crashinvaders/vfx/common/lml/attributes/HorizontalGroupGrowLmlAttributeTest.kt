package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class HorizontalGroupGrowLmlAttributeTest {

    private lateinit var attribute: HorizontalGroupGrowLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: HorizontalGroup

    @Before
    fun setUp() {
        attribute = HorizontalGroupGrowLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(HorizontalGroup::class.java)
    }

    @Test
    fun `getHandledType returns HorizontalGroup class`() {
        assertEquals(HorizontalGroup::class.java, attribute.handledType)
    }

    @Test
    fun `process sets grow true correctly`() {
        val rawData = "true"
        `when`(parser.parseBoolean(rawData)).thenReturn(true)

        attribute.process(parser, tag, actor, rawData)

        verify(actor).expand(true)
        verify(actor).fill(1.0f)
    }

    @Test
    fun `process sets grow false correctly`() {
        val rawData = "false"
        `when`(parser.parseBoolean(rawData)).thenReturn(false)

        attribute.process(parser, tag, actor, rawData)

        verify(actor).expand(false)
        verify(actor).fill(0.0f)
    }
}
