package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShrinkContainerTest {

    @Test
    public void testShrinkWhenInvisible() {
        Actor actor = new Actor();
        actor.setSize(100, 50);
        ShrinkContainer<Actor> container = new ShrinkContainer<>(actor);
        container.prefWidth(100).prefHeight(50);

        assertTrue(container.isVisible());
        assertEquals(100f, container.getPrefWidth(), 0.01f);
        assertEquals(50f, container.getPrefHeight(), 0.01f);

        container.setVisible(false);
        assertFalse(container.isVisible());
        assertEquals(0f, container.getPrefWidth(), 0.01f);
        assertEquals(0f, container.getPrefHeight(), 0.01f);
        assertEquals(0f, container.getMinWidth(), 0.01f);
        assertEquals(0f, container.getMinHeight(), 0.01f);
    }

    @Test
    public void testInvalidateHierarchyOnVisibleChange() {
        ShrinkContainer<Actor> container = spy(new ShrinkContainer<>());
        container.setVisible(false);
        verify(container).invalidateHierarchy();
    }
}
