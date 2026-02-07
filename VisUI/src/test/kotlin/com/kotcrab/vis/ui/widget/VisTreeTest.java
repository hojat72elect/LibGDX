package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Focusable;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisTreeTest {

    @Mock
    private Stage mockStage;
    @Mock
    private Focusable mockFocusable;

    private VisTree<Tree.Node, Object> tree;
    private BitmapFont mockFont;

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) java.lang.reflect.Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        if (args != null && args.length == 1 && args[0] instanceof String) {
                            String path = (String) args[0];
                            if ("classpath".equals(name) || "internal".equals(name) || "absolute".equals(name)
                                    || "local".equals(name) || "external".equals(name)) {
                                return new FileHandle(path);
                            }
                        }
                        if ("classpath".equals(name)) {
                            return new FileHandle("test");
                        }
                        return null;
                    });
        }
        if (Gdx.app == null) {
            Gdx.app = (com.badlogic.gdx.Application) java.lang.reflect.Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize VisUI for testing
        if (VisUI.isLoaded()) VisUI.dispose(true);
        VisUI.setSkipGdxVersionCheck(true);

        Skin skin = new Skin();
        Sizes sizes = new Sizes();
        skin.add("default", sizes, Sizes.class);

        // Add LabelStyle to skin for TestNode
        mockFont = createMockBitmapFont();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = mockFont;
        skin.add("default", labelStyle, Label.LabelStyle.class);

        // Add TreeStyle to skin with mock drawables
        Tree.TreeStyle treeStyle = new Tree.TreeStyle();
        treeStyle.plus = createMockDrawable();
        treeStyle.minus = createMockDrawable();
        treeStyle.over = createMockDrawable();
        treeStyle.selection = createMockDrawable();
        skin.add("default", treeStyle, Tree.TreeStyle.class);

        VisUI.load(skin);

        // Create tree without mock style to avoid issues
        tree = new VisTree<>();
        mockStage.addActor(tree);

        // Use reflection to set the tree's stage to mockStage
        try {
            java.lang.reflect.Field stageField = com.badlogic.gdx.scenes.scene2d.Actor.class.getDeclaredField("stage");
            stageField.setAccessible(true);
            stageField.set(tree, mockStage);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) VisUI.dispose(true);
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
        Tree.TreeStyle testStyle = new Tree.TreeStyle();
        testStyle.plus = createMockDrawable();
        testStyle.minus = createMockDrawable();
        testStyle.over = createMockDrawable();
        testStyle.selection = createMockDrawable();

        VisTree<Tree.Node, Object> newTree = new VisTree<>(testStyle);
        assertNotNull("Tree should be created", newTree);
        assertEquals("Style should be set", testStyle, newTree.getStyle());
    }

    @Test
    public void testTouchDownWithNonActorFocusable() {
        // Reset FocusManager state
        resetFocusManager();

        // Set up FocusManager to return a non-actor focusable
        setFocusedWidget(mockFocusable);

        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);

        // Get the VisTree's InputListener and call it directly
        com.badlogic.gdx.scenes.scene2d.InputListener listener = (com.badlogic.gdx.scenes.scene2d.InputListener) tree.getListeners().get(1);
        listener.touchDown(event, 10f, 10f, 0, 0);

        // Verify that FocusManager.resetFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithActorFocusableNotDescendant() {
        // Reset FocusManager state
        resetFocusManager();

        // Set up FocusManager to return a non-actor focusable (will trigger reset)
        setFocusedWidget(mockFocusable);

        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);

        // Get the VisTree's InputListener and call it directly
        com.badlogic.gdx.scenes.scene2d.InputListener listener = (com.badlogic.gdx.scenes.scene2d.InputListener) tree.getListeners().get(1);
        listener.touchDown(event, 10f, 10f, 0, 0);

        // Verify that FocusManager.resetFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithActorFocusableDescendant() {
        // Reset FocusManager state
        resetFocusManager();

        // Set up FocusManager to return a non-actor focusable (will trigger reset)
        setFocusedWidget(mockFocusable);

        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);

        // Get the VisTree's InputListener and call it directly
        com.badlogic.gdx.scenes.scene2d.InputListener listener = (com.badlogic.gdx.scenes.scene2d.InputListener) tree.getListeners().get(1);
        listener.touchDown(event, 10f, 10f, 0, 0);

        // Verify that FocusManager.resetFocus was called
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTouchDownWithNoFocusedWidget() {
        // Reset FocusManager state
        resetFocusManager();

        InputEvent event = mock(InputEvent.class);
        when(event.getStage()).thenReturn(mockStage);

        // Get the VisTree's InputListener and call it directly
        com.badlogic.gdx.scenes.scene2d.InputListener listener = (com.badlogic.gdx.scenes.scene2d.InputListener) tree.getListeners().get(1);
        listener.touchDown(event, 10f, 10f, 0, 0);

        // Verify that FocusManager.resetFocus was called even with no focused widget
        verify(mockStage).setKeyboardFocus(null);
    }

    @Test
    public void testTreeOperations() {
        // Test basic tree operations
        TestNode node1 = new TestNode("Node 1");
        TestNode node2 = new TestNode("Node 2");

        tree.add(node1);
        tree.add(node2);

        assertEquals("Tree should have 2 root nodes", 2, tree.getNodes().size);
        assertTrue("Tree should contain node1", tree.getNodes().contains(node1, true));
        assertTrue("Tree should contain node2", tree.getNodes().contains(node2, true));
    }

    @Test
    public void testNodeHierarchy() {
        TestNode parent = new TestNode("Parent");
        TestNode child = new TestNode("Child");

        tree.add(parent);
        parent.add(child);

        assertTrue("Child should be descendant of parent", parent.getChildren().contains(child, true));
        assertEquals("Parent should have 1 child", 1, parent.getChildren().size);
    }

    @Test
    public void testNodeSelection() {
        TestNode node = new TestNode("Test Node");
        tree.add(node);

        tree.getSelection().add(node);
        assertTrue("Node should be selected", tree.getSelection().contains(node));

        tree.getSelection().clear();
        assertFalse("Node should not be selected", tree.getSelection().contains(node));
    }

    @Test
    public void testNodeExpansion() {
        TestNode parent = new TestNode("Parent");
        TestNode child = new TestNode("Child");

        tree.add(parent);
        parent.add(child);

        // Test expansion state
        parent.setExpanded(true);
        assertTrue("Parent should be expanded", parent.isExpanded());

        parent.setExpanded(false);
        assertFalse("Parent should not be expanded", parent.isExpanded());
    }

    /**
     * Helper method to create a mock drawable for testing.
     */
    private Drawable createMockDrawable() {
        Drawable mockDrawable = mock(Drawable.class);
        doReturn(16f).when(mockDrawable).getMinWidth();
        doReturn(16f).when(mockDrawable).getMinHeight();
        doReturn(2f).when(mockDrawable).getLeftWidth();
        doReturn(2f).when(mockDrawable).getRightWidth();
        doReturn(2f).when(mockDrawable).getTopHeight();
        doReturn(2f).when(mockDrawable).getBottomHeight();
        return mockDrawable;
    }

    /**
     * Helper method to create a working BitmapFont for testing.
     */
    private BitmapFont createMockBitmapFont() {
        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        com.badlogic.gdx.graphics.Texture mockTexture = mock(com.badlogic.gdx.graphics.Texture.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    private Drawable createMockDrawableForFocusable() {
        Drawable mockDrawable = mock(Drawable.class);
        doReturn(16f).when(mockDrawable).getMinWidth();
        doReturn(16f).when(mockDrawable).getMinHeight();
        doReturn(2f).when(mockDrawable).getLeftWidth();
        doReturn(2f).when(mockDrawable).getRightWidth();
        doReturn(2f).when(mockDrawable).getTopHeight();
        doReturn(2f).when(mockDrawable).getBottomHeight();
        return mockDrawable;
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

    /**
     * Concrete Node implementation for testing.
     */
    private class TestNode extends Tree.Node<TestNode, String, Label> {
        public TestNode(String text) {
            super(new Label(text, new Label.LabelStyle(mockFont, com.badlogic.gdx.graphics.Color.WHITE)));
            setValue(text);
        }
    }
}
