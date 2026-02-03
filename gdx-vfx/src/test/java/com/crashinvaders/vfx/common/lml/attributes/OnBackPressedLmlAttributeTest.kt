package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.action.ActorConsumer
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class OnBackPressedLmlAttributeTest {

    private lateinit var attribute: OnBackPressedLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Actor
    private lateinit var action: ActorConsumer<Any, Actor>

    @Before
    fun setUp() {
        attribute = OnBackPressedLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(Actor::class.java)
        action = mock(ActorConsumer::class.java) as ActorConsumer<Any, Actor>
    }

    @Test
    fun `getHandledType returns Actor class`() {
        assertEquals(Actor::class.java, attribute.handledType)
    }

    @Test
    fun `process adds input listener correctly`() {
        val rawData = "someAction"
        `when`(parser.parseAction(rawData, actor)).thenReturn(action)

        attribute.process(parser, tag, actor, rawData)

        val captor = ArgumentCaptor.forClass(InputListener::class.java)
        verify(actor).addListener(captor.capture())

        val listener = captor.value
        val event = mock(InputEvent::class.java)

        // Test BACK key
        listener.keyDown(event, Input.Keys.BACK)
        verify(action).consume(actor)

        // Test ESCAPE key
        listener.keyDown(event, Input.Keys.ESCAPE)
        verify(action, org.mockito.Mockito.times(2)).consume(actor)
    }

    @Test
    fun `process throws error when action is null`() {
        val rawData = "invalidAction"
        `when`(parser.parseAction(rawData, actor)).thenReturn(null)

        attribute.process(parser, tag, actor, rawData)

        verify(parser).throwError("Could not find action for: $rawData with actor: $actor")
    }
}
