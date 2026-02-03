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

class HorizontalGroupExpandLmlAttributeTest {

    private lateinit var attribute: HorizontalGroupExpandLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: HorizontalGroup

    @Before
    fun setUp() {
        attribute = HorizontalGroupExpandLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(HorizontalGroup::class.java)
    }

    @Test
    fun `getHandledType returns HorizontalGroup class`() {
        assertEquals(HorizontalGroup::class.java, attribute.handledType)
    }

    @Test
    fun `process sets expand correctly`() {
        val rawData = "true"
        `when`(parser.parseBoolean(rawData)).thenReturn(true)

        attribute.process(parser, tag, actor, rawData)

        verify(actor).expand(true)
    }
}
