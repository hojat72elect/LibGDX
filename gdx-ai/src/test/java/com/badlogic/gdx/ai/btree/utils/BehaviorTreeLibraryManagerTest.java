package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.TaskCloner;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SerializationException;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * Unit tests for BehaviorTreeLibraryManager
 */
public class BehaviorTreeLibraryManagerTest {

    private BehaviorTreeLibraryManager manager;
    private TestTask testTask;
    private BehaviorTree<String> testTree;
    private TestFileHandleResolver testResolver;
    private BehaviorTreeLibrary originalLibrary;

    @Before
    public void setUp() {
        // Store original library to restore later
        manager = BehaviorTreeLibraryManager.getInstance();
        originalLibrary = manager.getLibrary();

        // Create test task and tree
        testTask = new TestTask();
        testTree = new BehaviorTree<>(testTask);

        // Create test resolver
        testResolver = new TestFileHandleResolver();

        // Create new library with test resolver
        BehaviorTreeLibrary newLibrary = new BehaviorTreeLibrary(testResolver, BehaviorTreeParser.DEBUG_NONE);
        manager.setLibrary(newLibrary);
    }

    @After
    public void tearDown() {
        // Restore original library
        manager.setLibrary(originalLibrary);
        // Clean up any global state
        GdxAI.setFileSystem(null);
        Task.TASK_CLONER = null;
    }

    @Test
    public void testGetInstance() {
        BehaviorTreeLibraryManager instance1 = BehaviorTreeLibraryManager.getInstance();
        BehaviorTreeLibraryManager instance2 = BehaviorTreeLibraryManager.getInstance();

        Assert.assertSame("Should return same singleton instance", instance1, instance2);
    }

    @Test
    public void testDefaultLibraryInitialization() {
        // Create a new manager instance to test default initialization
        BehaviorTreeLibraryManager newManager = BehaviorTreeLibraryManager.getInstance();
        BehaviorTreeLibrary library = newManager.getLibrary();

        Assert.assertNotNull("Library should be initialized by default", library);
        Assert.assertNotNull("Repository should be initialized", getField(library, "repository"));
        Assert.assertNotNull("Resolver should be initialized", getField(library, "resolver"));
        Assert.assertNotNull("Parser should be initialized", getField(library, "parser"));
    }

    @Test
    public void testGetLibrary() {
        BehaviorTreeLibrary library = manager.getLibrary();

        Assert.assertNotNull("Library should not be null", library);
        Assert.assertSame("Should return the same library instance", library, manager.getLibrary());
    }

    @Test
    public void testSetLibrary() {
        BehaviorTreeLibrary newLibrary = new BehaviorTreeLibrary(testResolver, BehaviorTreeParser.DEBUG_HIGH);

        manager.setLibrary(newLibrary);

        Assert.assertSame("Library should be updated", newLibrary, manager.getLibrary());
    }

    @Test
    public void testCreateRootTask() {
        String treeReference = "test.tree";
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);

        Task<String> rootTask = manager.createRootTask(treeReference);

        Assert.assertNotNull("Root task should be created", rootTask);
        Assert.assertNotSame("Root task should be a clone", testTask, rootTask);
        Assert.assertEquals("Root task should have same type", testTask.getClass(), rootTask.getClass());
    }

    @Test(expected = SerializationException.class)
    public void testCreateRootTaskWithNonExistentReference() {
        // This should trigger parsing which will fail with invalid content
        manager.createRootTask("nonexistent.tree");
    }

    @Test
    public void testCreateBehaviorTree() {
        String treeReference = "test.tree";
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);

        BehaviorTree<String> createdTree = manager.createBehaviorTree(treeReference);

        Assert.assertNotNull("Behavior tree should be created", createdTree);
        Assert.assertNotSame("Created tree should be a clone", testTree, createdTree);
        Assert.assertNull("Blackboard should be null by default", createdTree.getObject());
        Assert.assertNotNull("Root task should be cloned", createdTree.getChild(0));
    }

    @Test
    public void testCreateBehaviorTreeWithBlackboard() {
        String treeReference = "test.tree";
        String blackboard = "test_blackboard";
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);

        BehaviorTree<String> createdTree = manager.createBehaviorTree(treeReference, blackboard);

        Assert.assertNotNull("Behavior tree should be created", createdTree);
        Assert.assertNotSame("Created tree should be a clone", testTree, createdTree);
        Assert.assertEquals("Blackboard should be set", blackboard, createdTree.getObject());
        Assert.assertNotNull("Root task should be cloned", createdTree.getChild(0));
    }

    @Test(expected = SerializationException.class)
    public void testCreateBehaviorTreeWithNonExistentReference() {
        // This should trigger parsing which will fail with invalid content
        manager.createBehaviorTree("nonexistent.tree");
    }

    @Test
    public void testDisposeBehaviorTree() {
        String treeReference = "test.tree";
        BehaviorTree<String> treeToDispose = new BehaviorTree<>(new TestTask());

        // This should not throw any exception
        manager.disposeBehaviorTree(treeReference, treeToDispose);

        // Test with null tree reference
        manager.disposeBehaviorTree(null, treeToDispose);

        // Test with null tree
        manager.disposeBehaviorTree(treeReference, null);
    }

    @Test
    public void testDisposeBehaviorTreeWithCloner() {
        String treeReference = "test.tree";
        BehaviorTree<String> treeToDispose = new BehaviorTree<>(new TestTask());

        // Set up a test cloner
        TestTaskCloner testCloner = new TestTaskCloner();
        Task.TASK_CLONER = testCloner;

        manager.disposeBehaviorTree(treeReference, treeToDispose);
        Assert.assertTrue("Cloner should have been called", testCloner.freeTaskCalled);
        Assert.assertSame("Correct tree should be disposed", treeToDispose, testCloner.disposedTree);
    }

    @Test
    public void testManagerDelegation() {
        // Test that the manager properly delegates to the library
        String treeReference = "test.tree";
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);

        // Test all delegation methods
        Task<String> rootTask = manager.createRootTask(treeReference);
        BehaviorTree<String> tree1 = manager.createBehaviorTree(treeReference);
        BehaviorTree<String> tree2 = manager.createBehaviorTree(treeReference, "blackboard");

        Assert.assertNotNull("Root task should be created", rootTask);
        Assert.assertNotNull("Tree 1 should be created", tree1);
        Assert.assertNotNull("Tree 2 should be created", tree2);
        Assert.assertEquals("Tree 2 should have blackboard", "blackboard", tree2.getObject());

        // Test disposal
        manager.disposeBehaviorTree(treeReference, tree1);
    }

    @Test
    public void testManagerWithNullLibrary() {
        manager.setLibrary(null);

        String treeReference = "test.tree";

        // All operations should throw NullPointerException
        try {
            manager.createRootTask(treeReference);
            Assert.fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            manager.createBehaviorTree(treeReference);
            Assert.fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            manager.createBehaviorTree(treeReference, "blackboard");
            Assert.fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            manager.disposeBehaviorTree(treeReference, testTree);
            Assert.fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMultipleManagerInstances() {
        // All instances should be the same singleton
        BehaviorTreeLibraryManager manager1 = BehaviorTreeLibraryManager.getInstance();
        BehaviorTreeLibraryManager manager2 = BehaviorTreeLibraryManager.getInstance();
        BehaviorTreeLibraryManager manager3 = BehaviorTreeLibraryManager.getInstance();

        Assert.assertSame("All instances should be the same", manager1, manager2);
        Assert.assertSame("All instances should be the same", manager2, manager3);

        // Setting library on one should affect all since they're the same instance
        BehaviorTreeLibrary newLibrary = new BehaviorTreeLibrary(testResolver, BehaviorTreeParser.DEBUG_NONE);
        manager1.setLibrary(newLibrary);

        Assert.assertSame("Library should be shared across instances", newLibrary, manager2.getLibrary());
        Assert.assertSame("Library should be shared across instances", newLibrary, manager3.getLibrary());
    }

    @Test
    public void testManagerStateConsistency() {
        String treeReference = "test.tree";
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);

        // Create multiple instances and verify they are independent
        BehaviorTree<String> tree1 = manager.createBehaviorTree(treeReference);
        BehaviorTree<String> tree2 = manager.createBehaviorTree(treeReference, "blackboard");
        Task<String> rootTask = manager.createRootTask(treeReference);

        Assert.assertNotSame("Trees should be independent", tree1, tree2);
        Assert.assertNotSame("Root task should be independent", tree1.getChild(0), rootTask);
        Assert.assertNull("Tree1 should have null blackboard", tree1.getObject());
        Assert.assertEquals("Tree2 should have blackboard", "blackboard", tree2.getObject());
    }

    @Test
    public void testManagerLibraryChange() {
        String treeReference = "test.tree";

        // Register tree in current library
        manager.getLibrary().registerArchetypeTree(treeReference, testTree);
        Assert.assertTrue("Tree should be in current library", manager.getLibrary().hasArchetypeTree(treeReference));

        // Change library
        BehaviorTreeLibrary newLibrary = new BehaviorTreeLibrary(testResolver, BehaviorTreeParser.DEBUG_NONE);
        manager.setLibrary(newLibrary);

        // Tree should no longer be available
        Assert.assertFalse("Tree should not be in new library", manager.getLibrary().hasArchetypeTree(treeReference));

        // Register in new library
        newLibrary.registerArchetypeTree(treeReference, testTree);
        Assert.assertTrue("Tree should be in new library", manager.getLibrary().hasArchetypeTree(treeReference));

        // Should be able to create tree from new library
        BehaviorTree<String> createdTree = manager.createBehaviorTree(treeReference);
        Assert.assertNotNull("Tree should be created from new library", createdTree);
    }

    // Helper methods for reflection access to private fields and methods

    private Object getField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
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
                content = "invalid content"; // Default invalid content
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
     * Test task cloner for testing purposes
     */
    private static class TestTaskCloner implements TaskCloner {
        boolean freeTaskCalled = false;
        Task<?> disposedTree;

        @Override
        public <T> Task<T> cloneTask(Task<T> task) {
            return task.cloneTask();
        }

        @Override
        public <T> void freeTask(Task<T> task) {
            freeTaskCalled = true;
            disposedTree = task;
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
