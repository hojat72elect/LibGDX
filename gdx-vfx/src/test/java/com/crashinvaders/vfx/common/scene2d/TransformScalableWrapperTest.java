package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TransformScalableWrapperTest {

    @Test
    public void testLayout() {
        Widget actor = mock(Widget.class);
        when(actor.getPrefWidth()).thenReturn(100f);
        when(actor.getPrefHeight()).thenReturn(50f);

        TransformScalableWrapper<Widget> wrapper = new TransformScalableWrapper<>(actor);
        wrapper.layout();

        verify(actor).setPosition(0f, 0f);
        verify(actor).setSize(100f, 50f);
    }

    @Test
    public void testPrefSizeWithScaling() {
        Widget actor = mock(Widget.class);
        when(actor.getPrefWidth()).thenReturn(100f);
        when(actor.getPrefHeight()).thenReturn(50f);

        TransformScalableWrapper<Widget> wrapper = new TransformScalableWrapper<>(actor);
        wrapper.setScale(2f, 0.5f);

        assertEquals(200f, wrapper.getPrefWidth(), 0.01f);
        assertEquals(25f, wrapper.getPrefHeight(), 0.01f);
    }

    @Test
    public void testHitWithScaling() {
        Actor actor = new Actor();
        actor.setSize(100, 100);
        TransformScalableWrapper<Actor> wrapper = new TransformScalableWrapper<>(actor);
        wrapper.setSize(200, 200);
        wrapper.setScale(2f, 2f);
        wrapper.setTouchable(Touchable.enabled);

        // Internal child size is 100x100 (unscaled)
        // Hit at 150, 150 in parent coordinates should be 75, 75 in child coordinates
        // if child occupies full wrapper
        // But layout sets child size to its pref size (or orig size).
        // If child size is 100x100 and wrapper size is 200x200, child only occupies
        // half of it?
        // Let's check layout(): actor.setSize(origWidth, origHeight) or pref size.

        actor.setSize(100, 100);
        wrapper.layout(); // actor size becomes 100, 100

        // parentToLocalCoordinates for child.
        // wrapper.hit(150, 150) -> child.hit(150, 150) ?
        // Scene2D transformation logic is used.

        assertNotNull(wrapper.hit(50, 50, true));
        assertNull(wrapper.hit(250, 250, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTransformUnsupported() {
        TransformScalableWrapper<Actor> wrapper = new TransformScalableWrapper<>();
        wrapper.setTransform(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorUnsupported() {
        TransformScalableWrapper<Actor> wrapper = new TransformScalableWrapper<>();
        wrapper.addActor(new Actor());
    }
}
