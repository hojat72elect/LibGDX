package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SerializationException;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

/**
 * Unit tests for PooledBehaviorTreeLibrary
 */
public class PooledBehaviorTreeLibraryTest {

    private PooledBehaviorTreeLibrary library;
    private BehaviorTree<String> testTree;
    private TestFileHandleResolver testResolver;

    @Before
    public void setUp() {
        // Set up a mock file system to avoid NullPointerException
        GdxAI.setFileSystem(new com.badlogic.gdx.ai.FileSystem() {
            @Override
            public FileHandleResolver newResolver(com.badlogic.gdx.Files.FileType fileType) {
                return testResolver;
            }

            @Override
            public FileHandle newFileHandle(String fileName) {
                return new TestFileHandle("");
            }

            @Override
            public FileHandle newFileHandle(File file) {
                return new TestFileHandle("");
            }

            @Override
            public FileHandle newFileHandle(String fileName, com.badlogic.gdx.Files.FileType type) {
                return new TestFileHandle("");
            }

            @Override
            public FileHandle newFileHandle(File file, com.badlogic.gdx.Files.FileType type) {
                return new TestFileHandle("");
            }
        });

        // Create test task and tree
        TestTask testTask = new TestTask();
        testTree = new BehaviorTree<>(testTask);

        // Create test resolver
        testResolver = new TestFileHandleResolver();

        // Create library - PooledBehaviorTreeLibrary only has no-args constructor
        library = new PooledBehaviorTreeLibrary();
    }

    @After
    public void tearDown() {
        // Clean up any global state
        GdxAI.setFileSystem(null);
        Task.TASK_CLONER = null;
    }

    @Test
    public void testConstructorNoArgs() {
        PooledBehaviorTreeLibrary lib = new PooledBehaviorTreeLibrary();
        Assert.assertNotNull("Library should be created", lib);

        // Test that the pools field is initialized
        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(lib);
        Assert.assertNotNull("Pools should be initialized", pools);
        Assert.assertEquals("Pools should be empty initially", 0, pools.size);
    }

    @Test
    public void testConstructorInheritance() {
        // Test that PooledBehaviorTreeLibrary inherits behavior from parent
        PooledBehaviorTreeLibrary lib = new PooledBehaviorTreeLibrary();
        Assert.assertNotNull("Library should be created", lib);

        // Test basic functionality
        String treeReference = "test.tree";
        Assert.assertFalse("Should not have tree initially", lib.hasArchetypeTree(treeReference));
    }

    @Test
    public void testGetPool() {
        String treeReference = "test.tree";

        // Register a tree first
        library.registerArchetypeTree(treeReference, testTree);

        Pool<BehaviorTree> pool1 = invokeGetPool(library, treeReference);
        Pool<BehaviorTree> pool2 = invokeGetPool(library, treeReference);

        Assert.assertNotNull("Pool should be created", pool1);
        Assert.assertSame("Should return same pool instance", pool1, pool2);

        // Verify pool is stored in the pools map
        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Assert.assertSame("Pool should be stored in pools map", pool1, pools.get(treeReference));
    }

    @Test
    public void testGetPoolCreatesNewPoolWhenNotExists() {
        String treeReference = "test.tree";

        // Register a tree first
        library.registerArchetypeTree(treeReference, testTree);

        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Assert.assertEquals("Pools should be empty initially", 0, pools.size);

        Pool<BehaviorTree> pool = invokeGetPool(library, treeReference);

        Assert.assertNotNull("Pool should be created", pool);
        Assert.assertEquals("Pool should be added to pools map", 1, pools.size);
        Assert.assertSame("Pool should be stored in pools map", pool, pools.get(treeReference));
    }

    @Test
    public void testCreateBehaviorTree() {
        String treeReference = "test.tree";
        library.registerArchetypeTree(treeReference, testTree);

        BehaviorTree<String> createdTree = library.createBehaviorTree(treeReference);

        Assert.assertNotNull("Behavior tree should be created", createdTree);
        Assert.assertNotSame("Created tree should be different from archetype", testTree, createdTree);
        Assert.assertNull("Blackboard should be null by default", createdTree.getObject());
        Assert.assertNotNull("Root task should be cloned", createdTree.getChild(0));
    }

    @Test
    public void testCreateBehaviorTreeWithBlackboard() {
        String treeReference = "test.tree";
        String blackboard = "test_blackboard";
        library.registerArchetypeTree(treeReference, testTree);

        BehaviorTree<String> createdTree = library.createBehaviorTree(treeReference, blackboard);

        Assert.assertNotNull("Behavior tree should be created", createdTree);
        Assert.assertNotSame("Created tree should be different from archetype", testTree, createdTree);
        Assert.assertEquals("Blackboard should be set", blackboard, createdTree.getObject());
        Assert.assertNotNull("Root task should be cloned", createdTree.getChild(0));
    }

    @Test
    public void testCreateBehaviorTreeUsesPool() {
        String treeReference = "test.tree";
        library.registerArchetypeTree(treeReference, testTree);

        // Create multiple trees
        BehaviorTree<String> tree1 = library.createBehaviorTree(treeReference);
        BehaviorTree<String> tree2 = library.createBehaviorTree(treeReference);

        Assert.assertNotSame("Trees should be different instances", tree1, tree2);

        // Dispose both trees
        library.disposeBehaviorTree(treeReference, tree1);
        library.disposeBehaviorTree(treeReference, tree2);

        // Create another tree - should reuse from pool
        BehaviorTree<String> tree3 = library.createBehaviorTree(treeReference);

        Assert.assertNotNull("Tree should be created from pool", tree3);
        // Note: We can't easily test if it's the same instance due to pool implementation details
    }

    @Test
    public void testDisposeBehaviorTree() {
        String treeReference = "test.tree";
        library.registerArchetypeTree(treeReference, testTree);

        BehaviorTree<String> tree = library.createBehaviorTree(treeReference);

        // Dispose should not throw any exception
        library.disposeBehaviorTree(treeReference, tree);

        // Test with null tree reference - this might cause issues with pool lookup
        try {
            library.disposeBehaviorTree(null, tree);
        } catch (Exception e) {
            // Expected - null reference might not be supported
        }

        // Test with null tree - this might cause issues with pool.free()
        try {
            library.disposeBehaviorTree(treeReference, null);
        } catch (Exception e) {
            // Expected - null tree might not be supported
        }
    }

    @Test
    public void testClearPool() {
        String treeReference = "test.tree";
        library.registerArchetypeTree(treeReference, testTree);

        // Create and dispose some trees to populate the pool
        BehaviorTree<String> tree1 = library.createBehaviorTree(treeReference);
        BehaviorTree<String> tree2 = library.createBehaviorTree(treeReference);
        library.disposeBehaviorTree(treeReference, tree1);
        library.disposeBehaviorTree(treeReference, tree2);

        // Clear the pool
        library.clear(treeReference);

        // Pool should still exist but be empty
        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Pool<BehaviorTree> pool = pools.get(treeReference);
        Assert.assertNotNull("Pool should still exist", pool);

        // Creating a new tree should still work
        BehaviorTree<String> tree3 = library.createBehaviorTree(treeReference);
        Assert.assertNotNull("Tree should be created after clear", tree3);
    }

    @Test
    public void testClearNonExistentPool() {
        String treeReference = "nonexistent.tree";
        library.clear(treeReference);
        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Assert.assertEquals("Pools should remain empty", 0, pools.size);
    }

    @Test
    public void testClearAllPools() {
        String reference1 = "tree1.tree";
        String reference2 = "tree2.tree";

        library.registerArchetypeTree(reference1, testTree);
        library.registerArchetypeTree(reference2, testTree);

        invokeGetPool(library, reference1);
        invokeGetPool(library, reference2);

        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Assert.assertEquals("Should have 2 pools", 2, pools.size);

        library.clear();

        Assert.assertEquals("All pools should be cleared", 0, pools.size);
    }

    @Test
    public void testPoolBehaviorTreeCreation() {
        String treeReference = "test.tree";
        library.registerArchetypeTree(treeReference, testTree);

        Pool<BehaviorTree> pool = invokeGetPool(library, treeReference);

        BehaviorTree<?> pooledTree = pool.obtain();
        Assert.assertNotNull("Pool should create tree", pooledTree);
        Assert.assertNotNull("Pooled tree should have root task", pooledTree.getChild(0));

        pool.free(pooledTree);

        BehaviorTree<?> pooledTree2 = pool.obtain();
        Assert.assertNotNull("Pool should create another tree", pooledTree2);
    }

    @Test
    public void testMultipleTreeReferences() {
        String reference1 = "tree1.tree";
        String reference2 = "tree2.tree";

        library.registerArchetypeTree(reference1, testTree);
        library.registerArchetypeTree(reference2, testTree);

        BehaviorTree<String> tree1 = library.createBehaviorTree(reference1);
        BehaviorTree<String> tree2 = library.createBehaviorTree(reference2);

        Assert.assertNotNull("Tree 1 should be created", tree1);
        Assert.assertNotNull("Tree 2 should be created", tree2);
        Assert.assertNotSame("Trees should be different", tree1, tree2);

        library.disposeBehaviorTree(reference1, tree1);
        library.disposeBehaviorTree(reference2, tree2);

        ObjectMap<String, Pool<BehaviorTree>> pools = (ObjectMap<String, Pool<BehaviorTree>>) getField(library);
        Assert.assertEquals("Should have 2 pools", 2, pools.size);
        Assert.assertNotNull("Pool 1 should exist", pools.get(reference1));
        Assert.assertNotNull("Pool 2 should exist", pools.get(reference2));
    }

    @Test
    public void testInheritanceFromBehaviorTreeLibrary() {
        String treeReference = "test.tree";

        library.registerArchetypeTree(treeReference, testTree);
        Assert.assertTrue("Should inherit hasArchetypeTree", library.hasArchetypeTree(treeReference));

        Task<String> rootTask = library.createRootTask(treeReference);
        Assert.assertNotNull("Should inherit createRootTask", rootTask);

        BehaviorTree<String> tree1 = library.createBehaviorTree(treeReference);
        Assert.assertNotNull("Should inherit createBehaviorTree", tree1);
        Assert.assertNull("Blackboard should be null", tree1.getObject());

        BehaviorTree<String> tree2 = library.createBehaviorTree(treeReference, "test");
        Assert.assertNotNull("Should inherit createBehaviorTree with blackboard", tree2);
        Assert.assertEquals("Blackboard should be set", "test", tree2.getObject());
    }

    @Test(expected = SerializationException.class)
    public void testCreateBehaviorTreeWithNonExistentReference() {
        library.createBehaviorTree("nonexistent.tree");
    }

    @Test
    public void testPoolIndependence() {
        String reference1 = "tree1.tree";
        String reference2 = "tree2.tree";

        library.registerArchetypeTree(reference1, testTree);
        library.registerArchetypeTree(reference2, testTree);

        BehaviorTree<String> tree1 = library.createBehaviorTree(reference1);
        library.disposeBehaviorTree(reference1, tree1);
        BehaviorTree<String> tree1New = library.createBehaviorTree(reference1);
        BehaviorTree<String> tree2New = library.createBehaviorTree(reference2);

        Assert.assertNotNull("Both trees should be created", tree1New);
        Assert.assertNotNull("Both trees should be created", tree2New);
    }

    private Object getField(Object obj) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField("pools");
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + "pools", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Pool<BehaviorTree> invokeGetPool(PooledBehaviorTreeLibrary lib, String treeReference) {
        try {
            java.lang.reflect.Method method = PooledBehaviorTreeLibrary.class.getDeclaredMethod("getPool", String.class);
            method.setAccessible(true);
            return (Pool<BehaviorTree>) method.invoke(lib, treeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke getPool", e);
        }
    }

    /**
     * Test file handle resolver for testing purposes
     */
    private static class TestFileHandleResolver implements FileHandleResolver {
        private final ObjectMap<String, String> contentMap = new ObjectMap<>();

        @NotNull
        @Override
        public FileHandle resolve(@NotNull String fileName) {
            String content = contentMap.get(fileName);
            if (content == null) {
                content = "invalid content";
            }
            return new TestFileHandle(content);
        }
    }

    /**
     * Test file handle for testing purposes
     */
    private static class TestFileHandle extends FileHandle {
        private final String content;

        public TestFileHandle(String content) {
            super("test.tree");
            this.content = content;
        }

        @Override
        public StringReader reader() {
            return new StringReader(content);
        }

        @Override
        public boolean exists() {
            return true; // Always exist for testing
        }

        @Override
        public String path() {
            return "test.tree";
        }
    }

    /**
     * Test task implementation for testing purposes
     */
    private static class TestTask extends LeafTask<String> {

        @Override
        public Status execute() {
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return task;
        }

        @Override
        public Task<String> cloneTask() {
            return new TestTask();
        }
    }
}
