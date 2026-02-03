package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CheckBoxTextSpaceLmlAttributeTest {

    private lateinit var attribute: CheckBoxTextSpaceLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: CheckBox
    private lateinit var cell: Cell<Label>

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setUp() {
        attribute = CheckBoxTextSpaceLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(CheckBox::class.java)
        cell = mock(Cell::class.java) as Cell<Label>

        `when`(actor.labelCell).thenReturn(cell)
    }

    @Test
    fun `process sets padLeft on label cell`() {
        val rawData = "10"
        val parsedValue = 10
        
        `when`(parser.parseInt(rawData, actor)).thenReturn(parsedValue)

        attribute.process(parser, tag, actor, rawData)

        // Cell.padLeft takes a float in LibGDX
        verify(cell).padLeft(parsedValue.toFloat())
    }
}
