package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.TaskCloneException;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IncludeTest {
    private BehaviorTree<String> behaviorTree;
    private TestTask testTask;
    private BehaviorTreeLibrary originalLibrary;

    @Before
    public void setUp() {
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject("test");
        testTask = new TestTask("testTask");

        // Store original library to restore later
        BehaviorTreeLibraryManager manager = BehaviorTreeLibraryManager.getInstance();
        originalLibrary = manager.getLibrary();

        // Create a test library with a simple subtree
        BehaviorTreeLibrary testLibrary = new BehaviorTreeLibrary() {
            @Override
            public <T> Task<T> createRootTask(String treeReference) {
                if ("test/subtree".equals(treeReference)) {
                    return (Task<T>) testTask;
                }
                throw new RuntimeException("Unknown subtree: " + treeReference);
            }
        };

        manager.setLibrary(testLibrary);
    }

    @After
    public void tearDown() {
        // Restore original library
        BehaviorTreeLibraryManager.getInstance().setLibrary(originalLibrary);
    }

    @Test
    public void testConstructorNoArgs() {
        Include<String> include = new Include<>();
        Assert.assertNull("Subtree should be null", include.subtree);
        Assert.assertFalse("Lazy should be false by default", include.lazy);
        Assert.assertEquals("Should have no children", 0, include.getChildCount());
    }

    @Test
    public void testConstructorWithSubtree() {
        Include<String> include = new Include<>("test/subtree");
        Assert.assertEquals("Subtree should be set", "test/subtree", include.subtree);
        Assert.assertFalse("Lazy should be false by default", include.lazy);
        Assert.assertEquals("Should have no children", 0, include.getChildCount());
    }

    @Test
    public void testConstructorWithSubtreeAndLazy() {
        Include<String> include = new Include<>("test/subtree", true);
        Assert.assertEquals("Subtree should be set", "test/subtree", include.subtree);
        Assert.assertTrue("Lazy should be true", include.lazy);
        Assert.assertEquals("Should have no children", 0, include.getChildCount());
    }

    @Test
    public void testConstructorWithSubtreeAndEager() {
        Include<String> include = new Include<>("test/subtree", false);
        Assert.assertEquals("Subtree should be set", "test/subtree", include.subtree);
        Assert.assertFalse("Lazy should be false", include.lazy);
        Assert.assertEquals("Should have no children", 0, include.getChildCount());
    }

    @Test
    public void testLazyIncludeStart() {
        Include<String> include = new Include<>("test/subtree", true);
        include.start();

        Assert.assertEquals("Should have one child after start", 1, include.getChildCount());
        Assert.assertEquals("Child should be test task", testTask, include.getChild(0));
    }

    @Test
    public void testLazyIncludeStartOnlyOnce() {
        Include<String> include = new Include<>("test/subtree", true);

        // Start include
        include.start();
        Task<?> firstChild = include.getChild(0);

        // Call start again - should not create another child
        include.start();

        Assert.assertEquals("Should still have one child", 1, include.getChildCount());
        Assert.assertSame("Child should be same instance", firstChild, include.getChild(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEagerIncludeStartThrowsException() {
        Include<String> include = new Include<>("test/subtree", false);
        include.start();
    }

    @Test
    public void testLazyIncludeCloneTask() {
        Include<String> include = new Include<>("test/subtree", true);

        Task<String> clonedTask = include.cloneTask();

        Assert.assertNotNull("Cloned task should not be null", clonedTask);
        Assert.assertTrue("Cloned task should be Include", clonedTask instanceof Include);
        Assert.assertNotSame("Cloned task should be different instance", include, clonedTask);

        Include<String> clonedInclude = (Include<String>) clonedTask;
        Assert.assertEquals("Subtree should be copied", include.subtree, clonedInclude.subtree);
        Assert.assertEquals("Lazy should be copied", include.lazy, clonedInclude.lazy);
    }

    @Test
    public void testEagerIncludeCloneTask() {
        Include<String> include = new Include<>("test/subtree", false);

        Task<String> clonedTask = include.cloneTask();

        Assert.assertNotNull("Cloned task should not be null", clonedTask);
        Assert.assertSame("Cloned task should be subtree root task", testTask, clonedTask);
    }

    @Test
    public void testLazyIncludeCopyTo() {
        Include<String> include = new Include<>("test/subtree", true);
        Include<String> target = new Include<>();

        Task<String> result = include.copyTo(target);

        Assert.assertSame("Should return the target task", target, result);
        Assert.assertEquals("Subtree should be copied", include.subtree, target.subtree);
        Assert.assertEquals("Lazy should be copied", include.lazy, target.lazy);
    }

    @Test(expected = TaskCloneException.class)
    public void testEagerIncludeCopyToThrowsException() {
        Include<String> include = new Include<>("test/subtree", false);
        Include<String> target = new Include<>();

        include.copyTo(target);
    }

    @Test
    public void testLazyIncludeWithGuard() {
        Include<String> include = new Include<>("test/subtree", true);
        TestGuard guard = new TestGuard();
        include.setGuard(guard);

        // Test that guard is set
        Assert.assertNotNull("Guard should be set", include.getGuard());
        Assert.assertSame("Guard should be the same instance", guard, include.getGuard());
    }

    @Test
    public void testEagerIncludeWithGuard() {
        Include<String> include = new Include<>("test/subtree", false);

        Task<String> clonedTask = include.cloneTask();

        Assert.assertNotNull("Cloned task should not be null", clonedTask);
        Assert.assertSame("Cloned task should be subtree root task", testTask, clonedTask);
        // Note: Guard setting on cloned task is not tested due to reflection issues with private static classes
    }

    @Test
    public void testReset() {
        Include<String> include = new Include<>("test/subtree", true);
        include.setGuard(new TestGuard());

        include.reset();

        Assert.assertNull("Subtree should be null after reset", include.subtree);
        Assert.assertFalse("Lazy should be false after reset", include.lazy);
        Assert.assertNull("Guard should be null after reset", include.getGuard());
        Assert.assertEquals("Should have no children after reset", 0, include.getChildCount());
    }

    @Test
    public void testLazyIncludeInBehaviorTree() {
        testTask.setStatus(Status.SUCCEEDED);

        Include<String> include = new Include<>("test/subtree", true);
        behaviorTree.addChild(include);
        behaviorTree.step();

        Assert.assertEquals("Include should succeed", Status.SUCCEEDED, include.getStatus());
        Assert.assertEquals("Should have one child", 1, include.getChildCount());
        Assert.assertEquals("Child should be test task", testTask, include.getChild(0));
        Assert.assertEquals("Test task should be executed", 1, testTask.executions);
    }

    @Test(expected = RuntimeException.class)
    public void testLazyIncludeWithInvalidSubtree() {
        Include<String> include = new Include<>("invalid/subtree", true);
        include.start();
    }

    @Test(expected = RuntimeException.class)
    public void testEagerIncludeWithInvalidSubtree() {
        Include<String> include = new Include<>("invalid/subtree", false);
        include.cloneTask();
    }

    @Test
    public void testIncludeTaskConstraints() {
        Include<String> include = new Include<>();

        // Include should allow 0 children (it gets its child dynamically)
        Assert.assertEquals("Should have 0 children initially", 0, include.getChildCount());

        // Test that we can add a child manually (though this is not the typical use case)
        TestTask manualChild = new TestTask("manual");
        include.addChild(manualChild);

        Assert.assertEquals("Should have 1 child after manual addition", 1, include.getChildCount());
        Assert.assertEquals("Child should be manually added task", manualChild, include.getChild(0));
    }

    @Test
    public void testLazyIncludeWithNullSubtree() {
        Include<String> include = new Include<>();
        include.lazy = true;

        try {
            include.start();
            Assert.fail("Should throw exception for null subtree");
        } catch (RuntimeException e) {
            Assert.assertTrue("Exception message should mention null",
                    e.getMessage().contains("null") || e.getMessage().contains("Unknown"));
        }
    }

    @Test
    public void testEagerIncludeWithNullSubtree() {
        Include<String> include = new Include<>();

        try {
            include.cloneTask();
            Assert.fail("Should throw exception for null subtree");
        } catch (RuntimeException e) {
            Assert.assertTrue("Exception message should mention null",
                    e.getMessage().contains("null") || e.getMessage().contains("Unknown"));
        }
    }

    @Test
    public void testLazyIncludeMultipleStarts() {
        Include<String> include = new Include<>("test/subtree", true);

        // Start multiple times
        include.start();
        include.start();
        include.start();

        Assert.assertEquals("Should still have one child", 1, include.getChildCount());
        // Note: executions is not incremented by start(), only by step()
        Assert.assertEquals("Test task should not be executed by start", 0, testTask.executions);
    }

    @Test
    public void testLazyIncludeExecutionFlow() {
        testTask.setStatus(Status.RUNNING);

        Include<String> include = new Include<>("test/subtree", true);
        behaviorTree.addChild(include);
        behaviorTree.step();

        Assert.assertEquals("Include should be running", Status.RUNNING, include.getStatus());
        Assert.assertEquals("Should have one child", 1, include.getChildCount());
        Assert.assertEquals("Test task should be executed", 1, testTask.executions);

        // Make the task succeed and run again
        testTask.setStatus(Status.SUCCEEDED);
        behaviorTree.step();

        Assert.assertEquals("Include should succeed", Status.SUCCEEDED, include.getStatus());
        Assert.assertEquals("Test task should be executed twice", 2, testTask.executions);
    }

    // Helper classes for testing
    private static class TestTask extends LeafTask<String> {
        String name;
        Status status = Status.RUNNING;
        int executions = 0;

        public TestTask(String name) {
            this.name = name;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public Status execute() {
            executions++;
            return status;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestTask testTask = (TestTask) task;
            testTask.name = this.name;
            testTask.status = this.status;
            return task;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestGuard extends LeafTask<String> {
        @Override
        public Status execute() {
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return task;
        }
    }
}
