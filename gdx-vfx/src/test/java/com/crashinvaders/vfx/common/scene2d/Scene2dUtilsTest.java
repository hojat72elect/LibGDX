package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Scene2dUtilsTest {

    @Test
    public void testSetColorRecursively() {
        Group group = new Group();
        Actor child1 = new Actor();
        Group subGroup = new Group();
        Actor child2 = new Actor();

        group.addActor(child1);
        group.addActor(subGroup);
        subGroup.addActor(child2);

        Color color = Color.RED;
        Scene2dUtils.setColorRecursively(group, color);

        assertEquals(color, group.getColor());
        assertEquals(color, child1.getColor());
        assertEquals(color, subGroup.getColor());
        assertEquals(color, child2.getColor());
    }

    @Test
    public void testFindTopMostLayout() {
        WidgetGroup root = new WidgetGroup();
        WidgetGroup inner = new WidgetGroup();
        Actor actor = new Actor();

        root.addActor(inner);
        inner.addActor(actor);

        Layout result = Scene2dUtils.findTopMostLayout(actor);
        assertEquals(root, result);
    }

    @Test
    public void testInjectActorFields() {
        class DummyTarget {
            @InjectActor("actor1")
            Actor field1;
            @InjectActor
            Actor actor2; // Name matches field name
            Actor field3; // No annotation
        }

        DummyTarget target = new DummyTarget();
        Group group = new Group();
        Actor a1 = new Actor();
        a1.setName("actor1");
        Actor a2 = new Actor();
        a2.setName("actor2");
        Actor a3 = new Actor();
        a3.setName("field3");

        group.addActor(a1);
        group.addActor(a2);
        group.addActor(a3);

        Scene2dUtils.injectActorFields(target, group);

        assertEquals(a1, target.field1);
        assertEquals(a2, target.actor2);
        assertNull(target.field3);
    }

    @Test
    public void testSetPositionRelative() {
        Actor src = new Actor();
        src.setBounds(100, 100, 50, 50); // Center at 125, 125

        Actor dst = new Actor();
        dst.setSize(20, 20);

        // src.getX(Align.center) = 100 + 50/2 = 125
        // dstAlign center -> pos.x -= 20/2 = 10
        // result x = 125 - 10 = 115

        Scene2dUtils.setPositionRelative(src, Align.center, dst, Align.center, 0, 0, false);

        assertEquals(115f, dst.getX(), 0.01f);
        assertEquals(115f, dst.getY(), 0.01f);
    }
}
