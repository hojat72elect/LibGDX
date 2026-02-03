package com.crashinvaders.vfx.common.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomTargetActionTest {

    @Test
    public void testSetTargetRecursive() {
        CustomTargetAction customTargetAction = new CustomTargetAction();
        Actor target = new Actor();

        Action simpleAction = spy(Actions.rotateBy(90));
        ParallelAction parallelAction = spy(Actions.parallel(Actions.moveBy(10, 10), Actions.scaleBy(2, 2)));
        Action nestedAction = spy(Actions.alpha(0.5f));
        ((ParallelAction) parallelAction).getActions().add(nestedAction);

        customTargetAction.setAction(parallelAction);
        customTargetAction.setTarget(target);

        assertEquals(target, customTargetAction.getTarget());
        assertEquals(target, parallelAction.getTarget());
        for (Action a : parallelAction.getActions()) {
            assertEquals(target, a.getTarget());
        }
        assertEquals(target, nestedAction.getTarget());
    }

    @Test
    public void testSkipNestedCustomTargetAction() {
        CustomTargetAction outer = new CustomTargetAction();
        CustomTargetAction inner = spy(new CustomTargetAction());
        Actor outerTarget = new Actor();
        Actor innerTarget = new Actor();

        Action wrappedAction = spy(Actions.moveBy(10, 10));
        inner.setAction(wrappedAction);
        inner.setTarget(innerTarget);

        outer.setAction(inner);
        outer.setTarget(outerTarget);

        assertEquals(outerTarget, outer.getTarget());
        // Inner should KEEP its own target and NOT be updated by outer
        assertEquals(innerTarget, inner.getTarget());
        assertEquals(innerTarget, wrappedAction.getTarget());
    }

    @Test
    public void testDelegate() {
        CustomTargetAction customTargetAction = new CustomTargetAction();
        Action wrappedAction = mock(Action.class);
        customTargetAction.setAction(wrappedAction);

        when(wrappedAction.act(0.1f)).thenReturn(true);
        assertTrue(customTargetAction.delegate(0.1f));
        verify(wrappedAction).act(0.1f);
    }
}
