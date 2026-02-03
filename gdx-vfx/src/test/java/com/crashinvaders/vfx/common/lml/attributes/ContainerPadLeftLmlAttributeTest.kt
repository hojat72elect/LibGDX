package com.crashinvaders.vfx.common.lml.attributes

import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.tag.LmlTag
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ContainerPadLeftLmlAttributeTest {

    private lateinit var attribute: TestableContainerPadLeftLmlAttribute
    private lateinit var parser: LmlParser
    private lateinit var tag: LmlTag
    private lateinit var parentTag: LmlTag
    private lateinit var actor: Container<*>
    private lateinit var cell: Cell<*>

    @Before
    fun setUp() {
        attribute = TestableContainerPadLeftLmlAttribute()
        parser = mock(LmlParser::class.java)
        tag = mock(LmlTag::class.java)
        parentTag = mock(LmlTag::class.java)
        actor = mock(Container::class.java)
        cell = mock(Cell::class.java)

        `when`(tag.parent).thenReturn(parentTag)
    }

    @Test
    fun `applyToCell sets padLeft on cell from actor`() {
        val padValue = 20f
        `when`(actor.padLeft).thenReturn(padValue)

        attribute.publicApplyToCell(actor, cell)

        verify(cell).padLeft(padValue)
    }

    class TestableContainerPadLeftLmlAttribute : ContainerPadLeftLmlAttribute() {
        fun publicApplyToContainer(parser: LmlParser, tag: LmlTag, actor: Container<*>, rawAttributeData: String) {
            applyToContainer(parser, tag, actor, rawAttributeData)
        }

        fun publicApplyToCell(actor: Container<*>, cell: Cell<*>) {
            applyToCell(actor, cell)
        }
    }
}
