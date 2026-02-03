package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.github.czyzby.lml.parser.LmlData
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ButtonCheckedImageLmlAttributeTest {

    private lateinit var attribute: ButtonCheckedImageLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: ImageButton
    private lateinit var lmlData: LmlData
    private lateinit var skin: Skin
    private lateinit var drawable: Drawable

    @Before
    fun setUp() {
        attribute = ButtonCheckedImageLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(ImageButton::class.java)
        lmlData = mock(LmlData::class.java)
        skin = mock(Skin::class.java)
        drawable = mock(Drawable::class.java)

        `when`(parser.data).thenReturn(lmlData)
        `when`(lmlData.defaultSkin).thenReturn(skin)
    }

    @Test
    fun `process sets imageChecked from skin`() {
        val rawData = "some-drawable"
        val parsedData = "some-drawable"
        val originalStyle = ImageButtonStyle()
        
        `when`(actor.style).thenReturn(originalStyle)
        `when`(parser.parseString(rawData, actor)).thenReturn(parsedData)
        `when`(skin.getDrawable(parsedData)).thenReturn(drawable)

        attribute.process(parser, tag, actor, rawData)

        val styleCaptor = ArgumentCaptor.forClass(ImageButtonStyle::class.java)
        verify(actor).style = styleCaptor.capture()
        
        val newStyle = styleCaptor.value
        assertEquals(drawable, newStyle.imageChecked)
        assertNotSame(originalStyle, newStyle)
    }
}
