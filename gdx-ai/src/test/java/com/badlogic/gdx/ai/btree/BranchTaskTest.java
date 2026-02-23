package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.utils.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BranchTaskTest {
    private final BehaviorTree<String> behaviorTree = new BehaviorTree<>();
    private final TestTask task1 = new TestTask("task1");
    private final TestTask task2 = new TestTask("task2");
    private final TestTask task3 = new TestTask("task3");
    private final Array<Task<String>> tasks = new Array<>();

    @Before
    public void setUp() {
        behaviorTree.setObject("test");
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
    }

    @Test
    public void testConstructorNoChildren() {
        TestBranchTask branchTask = new TestBranchTask();
        Assert.assertEquals(0, branchTask.getChildCount());
        Assert.assertNotNull(branchTask.children);
        Assert.assertEquals(0, branchTask.children.size);
    }

    @Test
    public void testConstructorWithArray() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        Assert.assertEquals(3, branchTask.getChildCount());
        Assert.assertEquals(task1, branchTask.getChild(0));
        Assert.assertEquals(task2, branchTask.getChild(1));
        Assert.assertEquals(task3, branchTask.getChild(2));
    }

    @Test
    public void testConstructorWithEmptyArray() {
        Array<Task<String>> emptyTasks = new Array<>();
        TestBranchTask branchTask = new TestBranchTask(emptyTasks);
        Assert.assertEquals(0, branchTask.getChildCount());
        Assert.assertNotNull(branchTask.children);
        Assert.assertEquals(0, branchTask.children.size);
    }

    @Test
    public void testAddChildToTask() {
        TestBranchTask branchTask = new TestBranchTask();
        int index = branchTask.addChildToTask(task1);
        Assert.assertEquals(0, index);
        Assert.assertEquals(1, branchTask.getChildCount());
        Assert.assertEquals(task1, branchTask.getChild(0));

        index = branchTask.addChildToTask(task2);
        Assert.assertEquals(1, index);
        Assert.assertEquals(2, branchTask.getChildCount());
        Assert.assertEquals(task1, branchTask.getChild(0));
        Assert.assertEquals(task2, branchTask.getChild(1));
    }

    @Test
    public void testGetChildCount() {
        TestBranchTask branchTask = new TestBranchTask();
        Assert.assertEquals(0, branchTask.getChildCount());

        branchTask.addChildToTask(task1);
        Assert.assertEquals(1, branchTask.getChildCount());

        branchTask.addChildToTask(task2);
        Assert.assertEquals(2, branchTask.getChildCount());

        branchTask.addChildToTask(task3);
        Assert.assertEquals(3, branchTask.getChildCount());
    }

    @Test
    public void testGetChild() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        Assert.assertEquals(task1, branchTask.getChild(0));
        Assert.assertEquals(task2, branchTask.getChild(1));
        Assert.assertEquals(task3, branchTask.getChild(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildInvalidIndex() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        branchTask.getChild(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildNegativeIndex() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        branchTask.getChild(-1);
    }

    @Test
    public void testCopyTo() {
        TestBranchTask original = new TestBranchTask(tasks);
        TestBranchTask copy = new TestBranchTask();

        original.copyTo(copy);

        Assert.assertEquals(original.getChildCount(), copy.getChildCount());
        Assert.assertNotSame(original.children, copy.children);
        Assert.assertNotSame(original.getChild(0), copy.getChild(0));
        Assert.assertNotSame(original.getChild(1), copy.getChild(1));
        Assert.assertNotSame(original.getChild(2), copy.getChild(2));

        // Verify copied tasks have the same properties
        Assert.assertEquals(task1.name, ((TestTask) copy.getChild(0)).name);
        Assert.assertEquals(task2.name, ((TestTask) copy.getChild(1)).name);
        Assert.assertEquals(task3.name, ((TestTask) copy.getChild(2)).name);
    }

    @Test
    public void testCopyToWithNullChildren() {
        TestBranchTask original = new TestBranchTask();
        original.children = null; // Simulate null children
        TestBranchTask copy = new TestBranchTask();

        original.copyTo(copy);

        Assert.assertEquals(0, copy.getChildCount());
        Assert.assertNotNull(copy.children);
    }

    @Test
    public void testResetClearsChildren() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        Assert.assertEquals(3, branchTask.getChildCount());

        branchTask.reset();

        Assert.assertEquals(0, branchTask.getChildCount());
        Assert.assertNotNull(branchTask.children);
        Assert.assertEquals(0, branchTask.children.size);
    }

    @Test
    public void testInheritanceFromTask() {
        TestBranchTask branchTask = new TestBranchTask();
        
        // Test inherited methods from Task
        Assert.assertEquals(Status.FRESH, branchTask.getStatus());
        Assert.assertNull(branchTask.getGuard());
        
        // Test guard functionality
        TestTask guard = new TestTask("guard");
        branchTask.setGuard(guard);
        Assert.assertEquals(guard, branchTask.getGuard());
    }

    @Test
    public void testTaskConstraintAnnotation() {
        // Test that BranchTask has the correct constraint annotation
        Class<?> branchTaskClass = TestBranchTask.class.getSuperclass();
        Assert.assertNotNull(branchTaskClass);
        
        TaskConstraint constraint = branchTaskClass.getAnnotation(TaskConstraint.class);
        Assert.assertNotNull(constraint);
        Assert.assertEquals(1, constraint.minChildren());
    }

    @Test
    public void testCloneTask() {
        TestBranchTask original = new TestBranchTask(tasks);
        TestBranchTask cloned = (TestBranchTask) original.cloneTask();
        
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getChildCount(), cloned.getChildCount());
    }

    @Test
    public void testCloneTaskWithNullGuard() {
        TestBranchTask original = new TestBranchTask(tasks);
        TestBranchTask cloned = (TestBranchTask) original.cloneTask();
        
        Assert.assertNull(cloned.getGuard());
    }

    @Test
    public void testMultipleCopyOperations() {
        TestBranchTask original = new TestBranchTask(tasks);
        TestBranchTask copy1 = new TestBranchTask();
        TestBranchTask copy2 = new TestBranchTask();

        original.copyTo(copy1);
        original.copyTo(copy2);

        Assert.assertEquals(copy1.getChildCount(), copy2.getChildCount());
        Assert.assertNotSame(copy1.children, copy2.children);
        Assert.assertNotSame(copy1.getChild(0), copy2.getChild(0));
    }

    @Test
    public void testAbstractMethodsImplementation() {
        TestBranchTask branchTask = new TestBranchTask(tasks);
        
        // Test that abstract methods are properly implemented
        Assert.assertTrue(branchTask.getChildCount() >= 0);
        Assert.assertNotNull(branchTask.getChild(0));
        
        // Test copyTo returns the correct type
        TestBranchTask copy = new TestBranchTask();
        Task<String> result = branchTask.copyTo(copy);
        Assert.assertSame(copy, result);
    }

    // Test implementation of BranchTask for testing purposes
    public static class TestBranchTask extends BranchTask<String> {
        public Status status = Status.FRESH;
        public BehaviorTree<String> tree;
        public Task<String> guard;

        public TestBranchTask() {
            super();
        }

        public TestBranchTask(Array<Task<String>> tasks) {
            super(tasks);
        }

        @Override
        public void childSuccess(Task<String> task) {
            // Implementation for testing
        }

        @Override
        public void childFail(Task<String> task) {
            // Implementation for testing
        }

        @Override
        public void childRunning(Task<String> runningTask, Task<String> reporter) {
            // Implementation for testing
        }

        @Override
        public void run() {
            // Implementation for testing
        }

        @Override
        public Task<String> getGuard() {
            return guard;
        }

        @Override
        public void setGuard(Task<String> guard) {
            this.guard = guard;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return super.copyTo(task);
        }
    }

    // Test task implementation
    public static class TestTask extends LeafTask<String> {
        String name;
        Status status = Status.RUNNING;
        int executions = 0;

        public TestTask() {
            this.name = "default";
        }

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
}
