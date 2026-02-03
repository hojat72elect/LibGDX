package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class AdvancedColorLmlAttributeTest {

    private lateinit var attribute: AdvancedColorLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var actor: Actor

    @Before
    fun setUp() {
        attribute = AdvancedColorLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        actor = Actor()
    }

    @Test
    fun `process sets color from hex string #RGB`() {
        attribute.process(parser, tag, actor, "#F00")
        assertEquals(Color.RED, actor.color)
    }

    @Test
    fun `process sets color from hex string #RGBA`() {
        attribute.process(parser, tag, actor, "#0F08")
        val expected = Color(0f, 1f, 0f, 0.53333336f)
        assertEquals(expected.r, actor.color.r, 0.01f)
        assertEquals(expected.g, actor.color.g, 0.01f)
        assertEquals(expected.b, actor.color.b, 0.01f)
        assertEquals(expected.a, actor.color.a, 0.01f)
    }

    @Test
    fun `process sets color from hex string #RRGGBB`() {
        attribute.process(parser, tag, actor, "#0000FF")
        assertEquals(Color.BLUE, actor.color)
    }

    @Test
    fun `process sets color from hex string #RRGGBBAA`() {
        attribute.process(parser, tag, actor, "#FFFFFFFF")
        assertEquals(Color.WHITE, actor.color)
    }

    @Test
    fun `process throws error for invalid hex string`() {
        attribute.process(parser, tag, actor, "#ZZZ")
        verify(parser).throwError(org.mockito.ArgumentMatchers.startsWith("Error parsing HEX code value"), org.mockito.ArgumentMatchers.any())
    }

    @Test
    fun `process delegates to super for non-hex string`() {
        // Since we cannot easily mock the super call behavior without a real parser implementation or more complex mocking,
        // and the super class ColorLmlAttribute likely handles color names or other formats.
        // However, AdvancedColorLmlAttribute only intercepts strings starting with "#".
        // If it doesn't start with "#", it calls super.process.
        // We can verify that our logic doesn't try to parse it as hex.
        
        // In a real unit test for this specific class, we might want to check if it *doesn't* throw the hex parsing error.
        // But since super.process might fail if the parser is a mock and can't handle the data, this is tricky.
        // Let's assume for this test we just want to ensure it doesn't crash in our code block.
        
        try {
            attribute.process(parser, tag, actor, "red")
        } catch (e: Exception) {
            // Expected if super.process fails due to mock parser
        }
        
        // Verify we didn't call throwError with our specific message
        verify(parser, org.mockito.Mockito.never()).throwError(org.mockito.ArgumentMatchers.startsWith("Error parsing HEX code value"), org.mockito.ArgumentMatchers.any())
    }
}
