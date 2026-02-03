package com.crashinvaders.vfx.common.scene2d.actions;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MoveByPathActionTest {

    @Test
    public void testUpdate() {
        MoveByPathAction action = new MoveByPathAction();
        Actor actor = new Actor();
        action.setActor(actor);

        Path<Vector2> path = mock(Path.class);
        Vector2 pos = new Vector2(10, 20);
        when(path.valueAt(any(Vector2.class), eq(0.5f))).thenReturn(pos);

        action.setPath(path);
        action.update(0.5f);

        assertEquals(10f, actor.getX(), 0.01f);
        assertEquals(20f, actor.getY(), 0.01f);
    }

    @Test
    public void testReset() {
        MoveByPathAction action = new MoveByPathAction();
        Path<Vector2> path = mock(Path.class);
        action.setPath(path);

        action.reset();

        try {
            action.update(0.5f);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
