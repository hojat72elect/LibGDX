package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.action.ActorConsumer
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ImageDrawableLmlAttributeTest {

    private lateinit var attribute: ImageDrawableLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var image: Image
    private lateinit var action: ActorConsumer<Drawable, Image>
    private lateinit var drawable: Drawable

    @Before
    fun setUp() {
        attribute = ImageDrawableLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        image = mock(Image::class.java)
        action = mock(ActorConsumer::class.java) as ActorConsumer<Drawable, Image>
        drawable = mock(Drawable::class.java)
    }

    @Test
    fun `getHandledType returns Image class`() {
        assertEquals(Image::class.java, attribute.handledType)
    }

    @Test
    fun `process sets drawable correctly`() {
        val rawData = "someAction"
        `when`(parser.parseAction(rawData, image)).thenReturn(action)
        `when`(action.consume(image)).thenReturn(drawable)

        attribute.process(parser, tag, image, rawData)

        verify(image).drawable = drawable
    }

    @Test
    fun `process throws error when action is null`() {
        val rawData = "invalidAction"
        `when`(parser.parseAction(rawData, image)).thenReturn(null)

        attribute.process(parser, tag, image, rawData)

        verify(parser).throwError("Cannot find action: $rawData")
    }
}
