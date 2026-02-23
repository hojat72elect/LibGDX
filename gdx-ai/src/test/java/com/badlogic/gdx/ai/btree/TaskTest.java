package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {
    private TestTask task;
    private TestTask childTask;
    private TestTask guardTask;
    private BehaviorTree<String> behaviorTree;
    private TestControl control;
    private TaskCloner originalTaskCloner;

    @Before
    public void setUp() {
        task = new TestTask("task");
        childTask = new TestTask("child");
        guardTask = new TestTask("guard");
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject("test");
        control = new TestControl();
        originalTaskCloner = Task.TASK_CLONER;
        Task.TASK_CLONER = null;
    }

    @After
    public void tearDown() {
        Task.TASK_CLONER = originalTaskCloner;
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(Status.FRESH, task.getStatus());
        Assert.assertNull(task.getGuard());
        Assert.assertNull(task.control);
        Assert.assertNull(task.tree);
    }

    @Test
    public void testGetStatus() {
        Assert.assertEquals(Status.FRESH, task.getStatus());
        task.status = Status.RUNNING;
        Assert.assertEquals(Status.RUNNING, task.getStatus());
        task.status = Status.SUCCEEDED;
        Assert.assertEquals(Status.SUCCEEDED, task.getStatus());
        task.status = Status.FAILED;
        Assert.assertEquals(Status.FAILED, task.getStatus());
        task.status = Status.CANCELLED;
        Assert.assertEquals(Status.CANCELLED, task.getStatus());
    }

    @Test
    public void testGetGuard() {
        Assert.assertNull(task.getGuard());
        task.guard = guardTask;
        Assert.assertEquals(guardTask, task.getGuard());
    }

    @Test
    public void testSetGuard() {
        Assert.assertNull(task.getGuard());
        task.setGuard(guardTask);
        Assert.assertEquals(guardTask, task.getGuard());
    }

    @Test
    public void testGetObject() {
        try {
            task.getObject();
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("This task has never run", e.getMessage());
        }

        task.tree = behaviorTree;
        Assert.assertEquals("test", task.getObject());
    }

    @Test
    public void testSetControl() {
        Assert.assertNull(task.control);
        Assert.assertNull(task.tree);

        control.tree = behaviorTree;
        task.setControl(control);

        Assert.assertEquals(control, task.control);
        Assert.assertEquals(behaviorTree, task.tree);
    }

    @Test
    public void testStart() {
        task.start();
    }

    @Test
    public void testEnd() {
        task.end();
    }

    @Test
    public void testRunning() {
        task.tree = behaviorTree;
        task.control = control;

        task.running();

        Assert.assertEquals(Status.RUNNING, task.getStatus());
        Assert.assertEquals(1, control.childRunningCallCount);
        Assert.assertEquals(task, control.lastRunningTask);
        Assert.assertEquals(task, control.lastReporter);
    }

    @Test
    public void testSuccess() {
        task.tree = behaviorTree;
        task.control = control;

        task.success();

        Assert.assertEquals(Status.SUCCEEDED, task.getStatus());
        Assert.assertEquals(1, control.childSuccessCallCount);
        Assert.assertEquals(task, control.lastSuccessTask);
        Assert.assertEquals(1, task.endCallCount);
    }

    @Test
    public void testFail() {
        task.tree = behaviorTree;
        task.control = control;

        task.fail();

        Assert.assertEquals(Status.FAILED, task.getStatus());
        Assert.assertEquals(1, control.childFailCallCount);
        Assert.assertEquals(task, control.lastFailTask);
        Assert.assertEquals(1, task.endCallCount);
    }

    @Test
    public void testCancel() {
        task.tree = behaviorTree;
        task.control = control;
        task.status = Status.RUNNING;

        task.cancel();

        Assert.assertEquals(Status.CANCELLED, task.getStatus());
        Assert.assertEquals(1, task.endCallCount);
    }

    @Test
    public void testCancelNotRunning() {
        task.tree = behaviorTree;
        task.control = control;
        task.status = Status.FRESH; // Not running

        task.cancel();

        Assert.assertEquals(Status.CANCELLED, task.getStatus());
        Assert.assertEquals(1, task.endCallCount);
    }

    @Test
    public void testCancelRunningChildren() {
        TestBranchTask branchTask = new TestBranchTask();
        TestTask runningChild1 = new TestTask("running1");
        TestTask runningChild2 = new TestTask("running2");
        TestTask nonRunningChild = new TestTask("nonRunning");

        runningChild1.status = Status.RUNNING;
        runningChild2.status = Status.RUNNING;
        nonRunningChild.status = Status.FRESH;

        branchTask.addChild(runningChild1);
        branchTask.addChild(nonRunningChild);
        branchTask.addChild(runningChild2);

        BehaviorTree<String> tree = new BehaviorTree<>();
        tree.setObject("test");
        branchTask.tree = tree;
        runningChild1.tree = tree;
        runningChild2.tree = tree;
        nonRunningChild.tree = tree;

        branchTask.cancelRunningChildren(0);

        Assert.assertEquals(Status.CANCELLED, runningChild1.getStatus());
        Assert.assertEquals(Status.FRESH, nonRunningChild.getStatus());
        Assert.assertEquals(Status.CANCELLED, runningChild2.getStatus());
    }

    @Test
    public void testCancelRunningChildrenFromIndex() {
        TestBranchTask branchTask = new TestBranchTask();
        TestTask child1 = new TestTask("child1");
        TestTask runningChild2 = new TestTask("running2");
        TestTask runningChild3 = new TestTask("running3");

        child1.status = Status.RUNNING;
        runningChild2.status = Status.RUNNING;
        runningChild3.status = Status.RUNNING;

        branchTask.addChild(child1);
        branchTask.addChild(runningChild2);
        branchTask.addChild(runningChild3);

        BehaviorTree<String> tree = new BehaviorTree<>();
        tree.setObject("test");
        branchTask.tree = tree;
        child1.tree = tree;
        runningChild2.tree = tree;
        runningChild3.tree = tree;

        branchTask.cancelRunningChildren(1);

        Assert.assertEquals(Status.RUNNING, child1.getStatus());
        Assert.assertEquals(Status.CANCELLED, runningChild2.getStatus());
        Assert.assertEquals(Status.CANCELLED, runningChild3.getStatus());
    }

    @Test
    public void testResetTask() {
        task.tree = behaviorTree;
        task.control = control;
        task.status = Status.RUNNING;
        task.guard = guardTask;

        TestBranchTask branchTask = new TestBranchTask();
        TestTask child = new TestTask("child");
        child.status = Status.RUNNING;
        child.tree = behaviorTree;
        child.control = branchTask;
        branchTask.addChild(child);

        branchTask.resetTask();

        Assert.assertEquals(Status.FRESH, branchTask.getStatus());
        Assert.assertNull(branchTask.tree);
        Assert.assertNull(branchTask.control);
        Assert.assertEquals(Status.FRESH, child.getStatus());
        Assert.assertNull(child.tree);
        Assert.assertNull(child.control);
    }

    @Test
    public void testResetTaskCancelsRunningChildren() {
        TestBranchTask branchTask = new TestBranchTask();
        TestTask runningChild = new TestTask("running");
        runningChild.status = Status.RUNNING;
        branchTask.addChild(runningChild);

        BehaviorTree<String> tree = new BehaviorTree<>();
        tree.setObject("test");
        branchTask.tree = tree;
        runningChild.tree = tree;

        branchTask.resetTask();
        Assert.assertEquals(Status.FRESH, runningChild.getStatus());
    }

    @Test
    public void testReset() {
        task.tree = behaviorTree;
        task.control = control;
        task.status = Status.RUNNING;
        task.guard = guardTask;

        task.reset();

        Assert.assertEquals(Status.FRESH, task.getStatus());
        Assert.assertNull(task.tree);
        Assert.assertNull(task.control);
        Assert.assertNull(task.guard);
    }

    @Test
    public void testCheckGuardNoGuard() {
        Assert.assertTrue(task.checkGuard(control));
    }

    @Test
    public void testCheckGuardWithGuardSuccess() {
        task.guard = guardTask;
        guardTask.statusToReturn = Status.SUCCEEDED;

        Assert.assertTrue(task.checkGuard(control));
    }

    @Test
    public void testCheckGuardWithGuardFailure() {
        task.guard = guardTask;
        guardTask.statusToReturn = Status.FAILED;

        Assert.assertFalse(task.checkGuard(control));
    }

    @Test
    public void testCheckGuardWithGuardRunning() {
        task.guard = guardTask;
        guardTask.statusToReturn = Status.RUNNING;

        try {
            task.checkGuard(control);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("Illegal guard status 'RUNNING'. Guards must either succeed or fail in one step.", e.getMessage());
        }
    }

    @Test
    public void testCheckGuardWithNestedGuard() {
        TestTask nestedGuard = new TestTask("nestedGuard");
        nestedGuard.statusToReturn = Status.FAILED;
        guardTask.guard = nestedGuard;
        task.guard = guardTask;

        Assert.assertFalse(task.checkGuard(control));
    }

    @Test
    public void testCloneTaskWithDefaultCloner() {

        Task.TASK_CLONER = new TaskCloner() {
            @Override
            public <T> Task<T> cloneTask(Task<T> task1) {
                TestTask original = (TestTask) task1;
                TestTask clone = new TestTask(original.name);
                clone.status = original.getStatus();
                clone.statusToReturn = original.statusToReturn;
                if (original.guard != null) {
                    clone.guard = original.guard.cloneTask();
                }
                return (Task<T>) clone;
            }

            @Override
            public <T> void freeTask(Task<T> task1) {
            }
        };
        task.guard = guardTask;

        TestTask cloned = (TestTask) task.cloneTask();

        Assert.assertNotSame(task, cloned);
        Assert.assertEquals(task.name, cloned.name);
        Assert.assertNotNull(cloned.guard);
        Assert.assertNotSame(guardTask, cloned.guard);
        Assert.assertEquals(guardTask.name, ((TestTask) cloned.guard).name);
    }

    @Test
    public void testCloneTaskWithCustomCloner() {

        Task.TASK_CLONER = new TaskCloner() {
            @Override
            public <T> Task<T> cloneTask(Task<T> task1) {
                TestTask clone = new TestTask("cloned-" + ((TestTask) task1).name);
                clone.status = task1.getStatus();
                return (Task<T>) clone;
            }

            @Override
            public <T> void freeTask(Task<T> task1) {
            }
        };
        task.status = Status.RUNNING;

        TestTask cloned = (TestTask) task.cloneTask();

        Assert.assertEquals("cloned-task", cloned.name);
        Assert.assertEquals(Status.RUNNING, cloned.getStatus());
    }

    @Test
    public void testCloneTaskWithFailingCustomCloner() {

        Task.TASK_CLONER = new TaskCloner() {
            @Override
            public <T> Task<T> cloneTask(Task<T> task1) {
                throw new RuntimeException("Clone failed");
            }

            @Override
            public <T> void freeTask(Task<T> task1) {
            }
        };

        try {
            task.cloneTask();
            Assert.fail("Expected TaskCloneException");
        } catch (TaskCloneException e) {
            Assert.assertEquals("Clone failed", e.getCause().getMessage());
        }
    }

    @Test
    public void testCloneTaskWithReflectionFailure() {
        class NonInstantiableTask extends Task<String> {
            private NonInstantiableTask() {
            }

            @Override
            protected int addChildToTask(Task<String> child) {
                return 0;
            }

            @Override
            public int getChildCount() {
                return 0;
            }

            @Override
            public Task<String> getChild(int i) {
                return null;
            }

            @Override
            public void run() {
            }

            @Override
            public void childSuccess(Task<String> task) {
            }

            @Override
            public void childFail(Task<String> task) {
            }

            @Override
            public void childRunning(Task<String> runningTask, Task<String> reporter) {
            }

            @Override
            protected Task<String> copyTo(Task<String> task) {
                return task;
            }
        }

        NonInstantiableTask failingTask = new NonInstantiableTask();

        try {
            failingTask.cloneTask();
            Assert.fail("Expected TaskCloneException");
        } catch (TaskCloneException e) {
            Assert.assertTrue(e.getCause() instanceof ReflectionException);
        }
    }

    @Test
    public void testStatusEnum() {
        Status[] statuses = Status.values();
        Assert.assertEquals(5, statuses.length);

        boolean hasFresh = false, hasRunning = false, hasFailed = false;
        boolean hasSucceeded = false, hasCancelled = false;

        for (Status status : statuses) {
            switch (status) {
                case FRESH:
                    hasFresh = true;
                    break;
                case RUNNING:
                    hasRunning = true;
                    break;
                case FAILED:
                    hasFailed = true;
                    break;
                case SUCCEEDED:
                    hasSucceeded = true;
                    break;
                case CANCELLED:
                    hasCancelled = true;
                    break;
            }
        }

        Assert.assertTrue(hasFresh);
        Assert.assertTrue(hasRunning);
        Assert.assertTrue(hasFailed);
        Assert.assertTrue(hasSucceeded);
        Assert.assertTrue(hasCancelled);
    }

    @Test
    public void testTaskConstraintAnnotation() {
        TaskConstraint annotation = Task.class.getAnnotation(TaskConstraint.class);
        Assert.assertNotNull(annotation);
    }

    @Test
    public void testAddChild() {
        TestBranchTask branchTask = new TestBranchTask();
        branchTask.tree = behaviorTree;

        int index = branchTask.addChild(childTask);

        Assert.assertEquals(0, index);
        Assert.assertEquals(1, branchTask.getChildCount());
        Assert.assertEquals(childTask, branchTask.getChild(0));
    }

    @Test
    public void testAddChildWithNotification() {
        TestBranchTask branchTask = new TestBranchTask();
        behaviorTree.listeners = new com.badlogic.gdx.utils.Array<>();
        TestListener listener = new TestListener();
        behaviorTree.listeners.add(listener);
        branchTask.tree = behaviorTree;

        branchTask.addChild(childTask);

        Assert.assertEquals(1, listener.childAddedCallCount);
        Assert.assertEquals(branchTask, listener.lastParent);
        Assert.assertEquals(0, listener.lastIndex);
    }

    private static class TestTask extends Task<String> {
        String name;
        Status statusToReturn = Status.SUCCEEDED;
        int endCallCount = 0;

        public TestTask(String name) {
            this.name = name;
        }

        @Override
        protected int addChildToTask(Task<String> child) {
            throw new UnsupportedOperationException("TestTask does not support children");
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public Task<String> getChild(int i) {
            throw new IndexOutOfBoundsException("TestTask has no children");
        }

        @Override
        public void run() {
            switch (statusToReturn) {
                case SUCCEEDED:
                    success();
                    break;
                case FAILED:
                    fail();
                    break;
                case RUNNING:
                    running();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void childSuccess(Task<String> task) {
        }

        @Override
        public void childFail(Task<String> task) {
        }

        @Override
        public void childRunning(Task<String> runningTask, Task<String> reporter) {
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            if (task instanceof TestTask) {
                TestTask testTask = (TestTask) task;
                testTask.name = this.name;
                testTask.statusToReturn = this.statusToReturn;
            }
            return task;
        }

        @Override
        public void end() {
            endCallCount++;
        }
    }

    private static class TestBranchTask extends Task<String> {
        com.badlogic.gdx.utils.Array<Task<String>> children = new com.badlogic.gdx.utils.Array<>();

        @Override
        protected int addChildToTask(Task<String> child) {
            children.add(child);
            return children.size - 1;
        }

        @Override
        public int getChildCount() {
            return children.size;
        }

        @Override
        public Task<String> getChild(int i) {
            return children.get(i);
        }

        @Override
        public void run() {
        }

        @Override
        public void childSuccess(Task<String> task) {
        }

        @Override
        public void childFail(Task<String> task) {
        }

        @Override
        public void childRunning(Task<String> runningTask, Task<String> reporter) {
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            if (task instanceof TestBranchTask) {
                TestBranchTask branchTask = (TestBranchTask) task;
                for (Task<String> child : children) {
                    branchTask.addChild(child.cloneTask());
                }
            }
            return task;
        }
    }

    private static class TestControl extends Task<String> {
        int childRunningCallCount = 0;
        int childSuccessCallCount = 0;
        int childFailCallCount = 0;
        Task<String> lastRunningTask;
        Task<String> lastReporter;
        Task<String> lastSuccessTask;
        Task<String> lastFailTask;

        public TestControl() {
            this.tree = new BehaviorTree<>();
            this.tree.setObject("test");
        }

        @Override
        protected int addChildToTask(Task<String> child) {
            return 0;
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public Task<String> getChild(int i) {
            return null;
        }

        @Override
        public void run() {
        }

        @Override
        public void childSuccess(Task<String> task) {
            childSuccessCallCount++;
            lastSuccessTask = task;
        }

        @Override
        public void childFail(Task<String> task) {
            childFailCallCount++;
            lastFailTask = task;
        }

        @Override
        public void childRunning(Task<String> runningTask, Task<String> reporter) {
            childRunningCallCount++;
            lastRunningTask = runningTask;
            lastReporter = reporter;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return task;
        }
    }

    private static class TestListener implements BehaviorTree.Listener<String> {
        int statusUpdatedCallCount = 0;
        int childAddedCallCount = 0;
        Task<String> lastTask;
        Status lastPreviousStatus;
        Task<String> lastParent;
        int lastIndex;

        @Override
        public void statusUpdated(Task<String> task, Status previousStatus) {
            statusUpdatedCallCount++;
            lastTask = task;
            lastPreviousStatus = previousStatus;
        }

        @Override
        public void childAdded(Task<String> parent, int index) {
            childAddedCallCount++;
            lastParent = parent;
            lastIndex = index;
        }
    }
}
