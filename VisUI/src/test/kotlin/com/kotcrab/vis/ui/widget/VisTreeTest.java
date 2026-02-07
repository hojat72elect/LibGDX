package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Focusable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VisTree}.
 */
public class VisTreeTest {

    @Mock
    private Stage mockStage;
    @Mock
    private Tree.TreeStyle mockTreeStyle;
    @Mock
    private Focusable mockFocusable;
    @Mock
    private Actor mockActor;

    private VisTree<Tree.Node, Object> tree;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Create tree with mock style
        tree = new VisTree<>(mockTreeStyle);
    }

    @Test
    public void testConstructorWithNoArgs() {
        VisTree<Tree.Node, Object> newTree = new VisTree<>();
        assertNotNull("Tree should be created", newTree);
    }

    @Test
    public void testConstructorWithStyleName() {
        VisTree<Tree.Node, Object> newTree = new VisTree<>("default");
        assertNotNull("Tree should be created", newTree);
    }

    @Test
    public void testConstructorWithStyle() {
        VisTree<Tree.Node, Object> newTree = new VisTree<>(mockTreeStyle);
        assertNotNull("Tree should be created", newTree);
        assertEquals("Style should be set", mockTreeStyle, newTree.getStyle());
    }

    @Test
    public void testTouchDownWithNonActorFocusable() {
        // Reset FocusManager state
        resetFocusManager();
        
        // Set up FocusManager to return a non-actor focusable
        setFocusedWidget(mockFocusable);
        
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        tree.fire(event);
        
        // Verify that FocusManager.resetFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithActorFocusableNotDescendant() {
        // Reset FocusManager state
        resetFocusManager();
        
        // Set up FocusManager to return an actor focusable that is not a descendant
        setFocusedWidget(mockFocusable);
        when(mockFocusable instanceof Actor).thenReturn(true);
        when(tree.isAscendantOf(mockActor)).thenReturn(false);
        
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        tree.fire(event);
        
        // Verify that FocusManager.resetFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithActorFocusableDescendant() {
        // Reset FocusManager state
        resetFocusManager();
        
        // Set up FocusManager to return an actor focusable that is a descendant
        setFocusedWidget(mockFocusable);
        when(mockFocusable instanceof Actor).thenReturn(true);
        when(mockFocusable instanceof Actor).thenReturn(true);
        when(tree.isAscendantOf((Actor) mockFocusable)).thenReturn(true);
        
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        tree.fire(event);
        
        // Verify that FocusManager.resetFocus was NOT called
        verify(mockStage, never()).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithNoFocusedWidget() {
        // Reset FocusManager state
        resetFocusManager();
        
        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);
        
        // Simulate touch down event
        tree.fire(event);
        
        // Verify that FocusManager.resetFocus was called even with no focused widget
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTreeOperations() {
        // Test basic tree operations
        Tree.Node node1 = new Tree.Node(new VisLabel("Node 1"));
        Tree.Node node2 = new Tree.Node(new VisLabel("Node 2"));
        
        tree.add(node1);
        tree.add(node2);
        
        assertEquals("Tree should have 2 root nodes", 2, tree.getNodes().size);
        assertTrue("Tree should contain node1", tree.getNodes().contains(node1, true));
        assertTrue("Tree should contain node2", tree.getNodes().contains(node2, true));
    }

    @Test
    public void testNodeHierarchy() {
        Tree.Node parent = new Tree.Node(new VisLabel("Parent"));
        Tree.Node child = new Tree.Node(new VisLabel("Child"));
        
        tree.add(parent);
        parent.add(child);
        
        assertTrue("Child should be descendant of parent", parent.getChildren().contains(child, true));
        assertEquals("Parent should have 1 child", 1, parent.getChildren().size);
    }

    @Test
    public void testNodeSelection() {
        Tree.Node node = new Tree.Node(new VisLabel("Test Node"));
        tree.add(node);
        
        tree.getSelection().add(node);
        assertTrue("Node should be selected", tree.getSelection().contains(node));
        
        tree.getSelection().removeAll(tree.getSelection());
        assertFalse("Node should not be selected", tree.getSelection().contains(node));
    }

    @Test
    public void testNodeExpansion() {
        Tree.Node parent = new Tree.Node(new VisLabel("Parent"));
        Tree.Node child = new Tree.Node(new VisLabel("Child"));
        
        tree.add(parent);
        parent.add(child);
        
        // Test expansion state
        parent.setExpanded(true);
        assertTrue("Parent should be expanded", parent.isExpanded());
        
        parent.setExpanded(false);
        assertFalse("Parent should not be expanded", parent.isExpanded());
    }

    /**
     * Helper method to reset FocusManager static state.
     */
    private void resetFocusManager() {
        try {
            java.lang.reflect.Field field = FocusManager.class.getDeclaredField("focusedWidget");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }

    /**
     * Helper method to set the focused widget in FocusManager.
     */
    private void setFocusedWidget(Focusable focusable) {
        try {
            java.lang.reflect.Field field = FocusManager.class.getDeclaredField("focusedWidget");
            field.setAccessible(true);
            field.set(null, focusable);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }
}
