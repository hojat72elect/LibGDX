package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kotcrab.vis.ui.widget.Draggable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link DragPane}.
 */
public class DragPaneTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }
    }

    @Test
    public void testDefaultConstructorIsHorizontal() {
        DragPane pane = new DragPane();
        Assert.assertNotNull(pane.getActor());
        Assert.assertTrue(pane.isHorizontal());
        Assert.assertFalse(pane.isVertical());
        Assert.assertNotNull(pane.getGroup());
        Assert.assertSame(pane.getActor(), pane.getGroup());
    }

    @Test
    public void testConstructorVerticalTrue() {
        DragPane pane = new DragPane(true);
        Assert.assertTrue(pane.isVertical());
        Assert.assertFalse(pane.isHorizontal());
        Assert.assertTrue(pane.getActor() instanceof VerticalGroup);
    }

    @Test
    public void testConstructorVerticalFalse() {
        DragPane pane = new DragPane(false);
        Assert.assertTrue(pane.isHorizontal());
        Assert.assertFalse(pane.isVertical());
        Assert.assertTrue(pane.getActor() instanceof HorizontalGroup);
    }

    @Test
    public void testConstructorWithGroup() {
        HorizontalGroup group = new HorizontalGroup();
        DragPane pane = new DragPane(group);
        Assert.assertSame(group, pane.getActor());
        Assert.assertSame(group, pane.getGroup());
        Assert.assertTrue(pane.isHorizontal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGroupThrows() {
        new DragPane((WidgetGroup) null);
    }

    @Test
    public void testGetChildrenDelegatesToGroup() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        Assert.assertEquals(1, pane.getChildren().size);
        Assert.assertTrue(pane.getChildren().contains(a, true));
    }

    @Test
    public void testAddActor() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        Assert.assertTrue(pane.contains(a));
        Assert.assertSame(pane.getActor(), a.getParent());
    }

    @Test
    public void testAddActorAfter() {
        DragPane pane = new DragPane();
        Actor first = new Actor();
        Actor second = new Actor();
        pane.addActor(first);
        pane.addActorAfter(first, second); // add second after first -> [first, second]
        Assert.assertEquals(2, pane.getChildren().size);
        Assert.assertEquals(0, pane.getChildren().indexOf(first, true));
        Assert.assertEquals(1, pane.getChildren().indexOf(second, true));
    }

    @Test
    public void testAddActorBefore() {
        DragPane pane = new DragPane();
        Actor first = new Actor();
        Actor second = new Actor();
        pane.addActor(first);
        pane.addActorBefore(first, second); // add second before first -> [second, first]
        Assert.assertEquals(2, pane.getChildren().size);
        Assert.assertEquals(0, pane.getChildren().indexOf(second, true));
        Assert.assertEquals(1, pane.getChildren().indexOf(first, true));
    }

    @Test
    public void testAddActorAt() {
        DragPane pane = new DragPane();
        pane.addActor(new Actor());
        Actor atZero = new Actor();
        pane.addActorAt(0, atZero);
        Assert.assertEquals(0, pane.getChildren().indexOf(atZero, true));
    }

    @Test
    public void testRemoveActor() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        Assert.assertTrue(pane.removeActor(a));
        Assert.assertFalse(pane.contains(a));
        Assert.assertEquals(0, pane.getChildren().size);
    }

    @Test
    public void testRemoveActorNotInPaneReturnsFalse() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        Assert.assertFalse(pane.removeActor(a));
    }

    @Test
    public void testRemoveActorWithUnfocus() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        Assert.assertTrue(pane.removeActor(a, true));
        Assert.assertFalse(pane.contains(a));
    }

    @Test
    public void testClear() {
        DragPane pane = new DragPane();
        pane.addActor(new Actor());
        pane.addActor(new Actor());
        pane.clear();
        Assert.assertEquals(0, pane.getChildren().size);
    }

    @Test
    public void testContainsWhenInGroup() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        Assert.assertTrue(pane.contains(a));
    }

    @Test
    public void testContainsWhenNotInGroup() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        Assert.assertFalse(pane.contains(a));
    }

    @Test
    public void testSetGroupMovesChildren() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        pane.addActor(a);
        VerticalGroup newGroup = new VerticalGroup();
        pane.setGroup(newGroup);
        Assert.assertSame(newGroup, pane.getActor());
        Assert.assertTrue(newGroup.getChildren().contains(a, true));
        Assert.assertSame(newGroup, a.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetActorNullThrows() {
        DragPane pane = new DragPane();
        pane.setActor(null);
    }

    @Test
    public void testGetHorizontalGroup() {
        DragPane pane = new DragPane(false);
        Assert.assertNotNull(pane.getHorizontalGroup());
        Assert.assertSame(pane.getActor(), pane.getHorizontalGroup());
    }

    @Test(expected = ClassCastException.class)
    public void testGetHorizontalGroupWhenVerticalThrows() {
        DragPane pane = new DragPane(true);
        pane.getHorizontalGroup();
    }

    @Test
    public void testGetVerticalGroup() {
        DragPane pane = new DragPane(true);
        Assert.assertNotNull(pane.getVerticalGroup());
        Assert.assertSame(pane.getActor(), pane.getVerticalGroup());
    }

    @Test(expected = ClassCastException.class)
    public void testGetVerticalGroupWhenHorizontalThrows() {
        DragPane pane = new DragPane();
        pane.getVerticalGroup();
    }

    @Test
    public void testSetGroupWithGridGroup() {
        DragPane pane = new DragPane(new GridGroup());
        Assert.assertTrue(pane.isGrid());
        Assert.assertNotNull(pane.getGridGroup());
    }

    @Test
    public void testSetGroupWithFloatingGroup() {
        DragPane pane = new DragPane(new FloatingGroup());
        Assert.assertTrue(pane.isFloating());
        Assert.assertNotNull(pane.getFloatingGroup());
    }

    @Test
    public void testSetGroupWithHorizontalFlowGroup() {
        DragPane pane = new DragPane(new HorizontalFlowGroup());
        Assert.assertTrue(pane.isHorizontalFlow());
        Assert.assertNotNull(pane.getHorizontalFlowGroup());
    }

    @Test
    public void testSetGroupWithVerticalFlowGroup() {
        DragPane pane = new DragPane(new VerticalFlowGroup());
        Assert.assertTrue(pane.isVerticalFlow());
        Assert.assertNotNull(pane.getVerticalFlowGroup());
    }

    @Test
    public void testSetDraggableGetDraggable() {
        DragPane pane = new DragPane();
        Draggable d = new Draggable();
        Assert.assertNull(pane.getDraggable());
        pane.setDraggable(d);
        Assert.assertSame(d, pane.getDraggable());
    }

    @Test
    public void testSetBoundsPropagatesToGroup() {
        DragPane pane = new DragPane();
        pane.setBounds(10f, 20f, 100f, 50f);
        Assert.assertEquals(100f, pane.getActor().getWidth(), 0.001f);
        Assert.assertEquals(50f, pane.getActor().getHeight(), 0.001f);
    }

    @Test
    public void testSetWidthSetHeightPropagateToGroup() {
        DragPane pane = new DragPane();
        pane.setWidth(200f);
        pane.setHeight(80f);
        Assert.assertEquals(200f, pane.getActor().getWidth(), 0.001f);
        Assert.assertEquals(80f, pane.getActor().getHeight(), 0.001f);
    }

    @Test
    public void testFindActor() {
        DragPane pane = new DragPane();
        Actor a = new Actor();
        a.setName("named");
        pane.addActor(a);
        Assert.assertSame(a, pane.findActor("named"));
    }

    @Test
    public void testSetListenerAcceptOwnChildren() {
        DragPane pane = new DragPane();
        pane.setListener(new DragPane.DragPaneListener.AcceptOwnChildren());
        Actor child = new Actor();
        pane.addActor(child);
        Assert.assertTrue(pane.contains(child));
        // accept() returns true for own children
        Assert.assertTrue(pane.accept(child));
    }

    @Test
    public void testLimitChildrenListener() {
        DragPane pane = new DragPane();
        pane.setListener(new DragPane.DragPaneListener.LimitChildren(2));
        Actor a1 = new Actor();
        Actor a2 = new Actor();
        Actor a3 = new Actor();
        pane.addActor(a1);
        pane.addActor(a2);
        Assert.assertTrue(pane.accept(a1)); // own child
        Assert.assertTrue(pane.accept(a2)); // own child
        Assert.assertFalse(pane.accept(a3)); // would exceed max
    }

    @Test
    public void testDragPaneListenerConstants() {
        Assert.assertTrue(DragPane.DragPaneListener.ACCEPT);
        Assert.assertFalse(DragPane.DragPaneListener.REFUSE);
    }

    @Test
    public void testDefaultDragListenerPolicyAllowRemoval() {
        DragPane.DefaultDragListener.DefaultPolicy policy =
                DragPane.DefaultDragListener.DefaultPolicy.ALLOW_REMOVAL;
        DragPane pane = new DragPane();
        Actor actor = new Actor();
        Assert.assertTrue(policy.accept(pane, actor));
    }

    @Test
    public void testDefaultDragListenerPolicyKeepChildren() {
        DragPane.DefaultDragListener.DefaultPolicy policy =
                DragPane.DefaultDragListener.DefaultPolicy.KEEP_CHILDREN;
        DragPane pane = new DragPane();
        Actor ownChild = new Actor();
        pane.addActor(ownChild);
        Actor stranger = new Actor();
        Assert.assertTrue(policy.accept(pane, ownChild));
        Assert.assertFalse(policy.accept(pane, stranger));
    }

    @Test
    public void testDefaultDragListenerOnStartReturnsApprove() {
        DragPane.DefaultDragListener listener = new DragPane.DefaultDragListener();
        Draggable d = new Draggable();
        Actor a = new Actor();
        Assert.assertTrue(listener.onStart(d, a, 0f, 0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultDragListenerSetPolicyNullThrows() {
        DragPane.DefaultDragListener listener = new DragPane.DefaultDragListener();
        listener.setPolicy(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultDragListenerConstructorNullPolicyThrows() {
        new DragPane.DefaultDragListener(null);
    }
}
