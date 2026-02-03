package com.crashinvaders.vfx.common.scene2d.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActionsExtTest {

    @BeforeClass
    public static void beforeClass() {
        Actions.registerAction(CustomTargetAction.class, CustomTargetAction::new);
        Actions.registerAction(PostAction.class, PostAction::new);
        Actions.registerAction(ActAction.class, ActAction::new);
        Actions.registerAction(TransformAction.class, TransformAction::new);
        Actions.registerAction(UnfocusAction.class, UnfocusAction::new);
        Actions.registerAction(OriginAlignAction.class, OriginAlignAction::new);
        Actions.registerAction(RemoveChildAction.class, RemoveChildAction::new);
        Actions.registerAction(OptionalAction.class, OptionalAction::new);
        Actions.registerAction(MoveByPathAction.class, MoveByPathAction::new);
        Actions.registerAction(TimeModulationAction.class, TimeModulationAction::new);
    }

    @Test
    public void testTarget() {
        Actor target = new Actor();
        Action wrapped = Actions.moveBy(10, 10);
        Action action = ActionsExt.target(target, wrapped);

        assertTrue(action instanceof CustomTargetAction);
        assertEquals(target, action.getTarget());
        assertEquals(wrapped, ((CustomTargetAction) action).getAction());
        assertEquals(target, wrapped.getTarget());
    }

    @Test
    public void testPost() {
        Actor target = new Actor();
        Action wrapped = Actions.moveBy(10, 10);
        PostAction action = ActionsExt.post(wrapped);
        action.setActor(target);

        assertEquals(wrapped, action.getAction());
        assertFalse("Should skip 1 frame", action.act(0.1f));
        action.act(0.1f);
        assertEquals(target, wrapped.getTarget());
    }

    @Test
    public void testPostWithSkipFrames() {
        PostAction action = ActionsExt.post(5);
        assertNull(action.getAction());
        for (int i = 0; i < 5; i++) {
            assertFalse("Frame " + i + " should be skipped", action.act(0.1f));
        }
        assertTrue("Frame 6 should not be skipped", action.act(0.1f));
    }

    @Test
    public void testAct() {
        // ActAction uses Gdx.app.postRunnable, tough to test behavior without mocking
        // Gdx.app
        ActAction action = ActionsExt.act(0.5f);
        assertNotNull(action);
    }

    @Test
    public void testTransform() {
        TransformAction action = ActionsExt.transform(true);
        assertTrue(action.isTransform());
    }

    @Test
    public void testUnfocus() {
        Action action = ActionsExt.unfocus();
        assertTrue(action instanceof UnfocusAction);
    }

    @Test
    public void testOrigin() {
        OriginAlignAction action = ActionsExt.origin(1);
        Actor target = new Actor();
        action.setTarget(target);
        action.setActor(target);
        action.act(0.1f);
    }

    @Test
    public void testRemoveChild() {
        Actor child = new Actor();
        Group parent = new Group();
        parent.addActor(child);

        RemoveChildAction action = ActionsExt.removeChild(child);
        action.setActor(parent);
        action.act(0.1f);

        assertFalse(parent.getChildren().contains(child, true));
    }

    @Test
    public void testOptional() {
        OptionalAction.Condition condition = mock(OptionalAction.Condition.class);
        Action wrapped = Actions.moveBy(10, 10);
        OptionalAction action = ActionsExt.optional(condition, wrapped);

        assertEquals(wrapped, action.getAction());
    }

    @Test
    public void testMoveByPath() {
        Path<Vector2> path = mock(Path.class);
        Interpolation interpolation = Interpolation.linear;
        MoveByPathAction action = ActionsExt.moveByPath(path, 1.5f, interpolation);

        assertEquals(1.5f, action.getDuration(), 0.01f);
        assertEquals(interpolation, action.getInterpolation());
    }

    @Test
    public void testTimeModulation() {
        Action wrapped = mock(Action.class);
        TimeModulationAction action = ActionsExt.timeModulation(2.0f, wrapped);

        assertEquals(wrapped, action.getAction());
        assertEquals(2.0f, action.getTimeFactor(), 0.01f);

        action.act(0.1f);
        verify(wrapped).act(0.2f);
    }
}
