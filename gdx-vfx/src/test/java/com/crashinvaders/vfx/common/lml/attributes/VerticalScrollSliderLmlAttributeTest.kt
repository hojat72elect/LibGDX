package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.utils.ObjectMap
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class VerticalScrollSliderLmlAttributeTest {

    private lateinit var attribute: VerticalScrollSliderLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var slider: Slider

    @Before
    fun setUp() {
        attribute = VerticalScrollSliderLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        slider = mock(Slider::class.java)
    }

    @Test
    fun `getHandledType returns Slider class`() {
        assertEquals(Slider::class.java, attribute.handledType)
    }

    @Test
    fun `process successfully registers listeners when actor is a ScrollPane`() {
        val actorId = "scrollPane"
        val scrollPane = mock(ScrollPane::class.java)
        val actorsMap = ObjectMap<String, Actor>()
        actorsMap.put(actorId, scrollPane)
        `when`(parser.actorsMappedByIds).thenReturn(actorsMap)

        // Mock ScrollPane.getActor() to avoid NPE if any internal logic calls it
        val scrollActor = mock(Actor::class.java)
        `when`(scrollPane.actor).thenReturn(scrollActor)

        attribute.process(parser, tag, slider, actorId)

        verify(scrollPane).addAction(ArgumentMatchers.any())
        verify(slider).addListener(ArgumentMatchers.any())
    }

}
