package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScalarScalableWrapperTest {

    @Test
    public void testSetActor() {
        ScalarScalableWrapper<Actor> wrapper = new ScalarScalableWrapper<>();
        Actor actor = new Actor();
        actor.setSize(100, 50);

        wrapper.setActor(actor);
        assertEquals(actor, wrapper.getActor());
        assertTrue(wrapper.getChildren().contains(actor, true));
    }

    @Test
    public void testLayout() {
        Actor actor = new Actor();
        ScalarScalableWrapper<Actor> wrapper = new ScalarScalableWrapper<>(actor);
        wrapper.setSize(200, 100);

        wrapper.layout();

        assertEquals(0f, actor.getX(), 0.01f);
        assertEquals(0f, actor.getY(), 0.01f);
        assertEquals(200f, actor.getWidth(), 0.01f);
        assertEquals(100f, actor.getHeight(), 0.01f);
    }

    @Test
    public void testPrefSizeWithScaling() {
        Widget widget = mock(Widget.class);
        when(widget.getPrefWidth()).thenReturn(100f);
        when(widget.getPrefHeight()).thenReturn(50f);

        ScalarScalableWrapper<Widget> wrapper = new ScalarScalableWrapper<>(widget);
        wrapper.setScale(2f, 0.5f);

        assertEquals(200f, wrapper.getPrefWidth(), 0.01f);
        assertEquals(25f, wrapper.getPrefHeight(), 0.01f);
    }

    @Test
    public void testScaleMethodsTriggerInvalidate() {
        ScalarScalableWrapper<Actor> wrapper = spy(new ScalarScalableWrapper<>());

        wrapper.setScaleX(2f);
        verify(wrapper).invalidateHierarchy();

        wrapper.setScaleY(3f);
        verify(wrapper, times(2)).invalidateHierarchy();

        wrapper.setScale(1.5f);
        verify(wrapper, times(3)).invalidateHierarchy();

        wrapper.scaleBy(0.1f);
        verify(wrapper, times(4)).invalidateHierarchy();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorUnsupported() {
        ScalarScalableWrapper<Actor> wrapper = new ScalarScalableWrapper<>();
        wrapper.addActor(new Actor());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTransformUnsupported() {
        ScalarScalableWrapper<Actor> wrapper = new ScalarScalableWrapper<>();
        wrapper.setTransform(true);
    }
}
