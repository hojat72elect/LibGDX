package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class OriginLmlAttributeTest {

    private lateinit var attribute: OriginLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Actor

    @Before
    fun setUp() {
        attribute = OriginLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = mock(Actor::class.java)
    }

    @Test
    fun `getHandledType returns Actor class`() {
        assertEquals(Actor::class.java, attribute.handledType)
    }
}
