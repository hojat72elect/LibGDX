package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LabelFontScaleLmlAttributeTest {

    private lateinit var attribute: LabelFontScaleLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Label

    @Before
    fun setUp() {
        attribute = LabelFontScaleLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(Label::class.java)
    }

    @Test
    fun `getHandledType returns Label class`() {
        assertEquals(Label::class.java, attribute.handledType)
    }

    @Test
    fun `process sets font scale correctly`() {
        val rawData = "1.5"
        `when`(parser.parseFloat(rawData, actor)).thenReturn(1.5f)

        attribute.process(parser, tag, actor, rawData)

        verify(actor).setFontScale(1.5f)
    }
}
