package com.crashinvaders.vfx.common.lml.attributes

import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlActorBuilder
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class DummyBuildingLmlAttributeTest {

    private lateinit var attribute: DummyBuildingLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var builder: LmlActorBuilder

    @Before
    fun setUp() {
        attribute = DummyBuildingLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        builder = mock(LmlActorBuilder::class.java)
    }

    @Test
    fun `getBuilderType returns LmlActorBuilder class`() {
        assertEquals(LmlActorBuilder::class.java, attribute.builderType)
    }

    @Test
    fun `process returns true`() {
        val result = attribute.process(parser, tag, builder, "someData")
        assertTrue(result)
    }
}
