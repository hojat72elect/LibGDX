package com.crashinvaders.vfx.common.lml.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.crashinvaders.vfx.common.scene2d.TransformScalableWrapper;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.LmlStyleSheet;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransformScalableWrapperLmlTagTest {

    private LmlParser parser;
    private LmlTag parentTag;
    private StringBuilder rawTagData;
    private TransformScalableWrapperLmlTag lmlTag;

    @Before
    public void setUp() {
        parser = mock(LmlParser.class);
        LmlSyntax syntax = mock(LmlSyntax.class);
        LmlStyleSheet styleSheet = mock(LmlStyleSheet.class);
        when(parser.getSyntax()).thenReturn(syntax);
        when(parser.getStyleSheet()).thenReturn(styleSheet);
        parentTag = mock(LmlTag.class);
        rawTagData = new StringBuilder("transformScalableWrapper");
        lmlTag = new TransformScalableWrapperLmlTag(parser, parentTag, rawTagData);
    }

    @Test
    public void testGetNewInstanceOfGroup() {
        LmlActorBuilder builder = mock(LmlActorBuilder.class);
        Group group = lmlTag.getNewInstanceOfGroup(builder);

        assertTrue(group instanceof TransformScalableWrapper);
    }

    @Test
    public void testAddChild() {
        TransformScalableWrapperLmlTag spyTag = spy(lmlTag);
        TransformScalableWrapper<Actor> wrapper = new TransformScalableWrapper<>();
        doReturn(wrapper).when(spyTag).getActor();

        Actor child = new Actor();
        spyTag.addChild(child);

        assertEquals(child, wrapper.getActor());
    }

    @Test
    public void testAddChildThrowsErrorWhenAlreadyHasChild() {
        TransformScalableWrapperLmlTag spyTag = spy(lmlTag);
        TransformScalableWrapper<Actor> wrapper = new TransformScalableWrapper<>();
        Actor firstChild = new Actor();
        wrapper.setActor(firstChild);
        doReturn(wrapper).when(spyTag).getActor();

        Actor secondChild = new Actor();
        spyTag.addChild(secondChild);

        verify(parser).throwErrorIfStrict("TransformScalableWrapper widget can manage only one child.");
        // The wrapper will still have the second child set because addChild doesn't
        // return early if throwErrorIfStrict doesn't throw an exception.
        assertEquals(secondChild, wrapper.getActor());
    }

    @Test
    public void testTagProvider() {
        TransformScalableWrapperLmlTag.TagProvider provider = new TransformScalableWrapperLmlTag.TagProvider();
        LmlTag createdTag = provider.create(parser, parentTag, rawTagData);

        assertNotNull(createdTag);
        assertTrue(createdTag instanceof TransformScalableWrapperLmlTag);
    }
}
