package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class LeafTaskTest {
    private final BehaviorTree<String> behaviorTree = new BehaviorTree<>();
    private TestLeafTask leafTask;

    @Before
    public void setUp() {
        behaviorTree.setObject("test");
        leafTask = new TestLeafTask();
        behaviorTree.addChild(leafTask);
    }

    @Test
    public void testConstructor() {
        TestLeafTask task = new TestLeafTask();
        Assert.assertEquals(Status.FRESH, task.getStatus());
    }

    @Test
    public void testExecuteReturnsSuccess() {
        leafTask.setStatusToReturn(Status.SUCCEEDED);
        behaviorTree.step();
        Assert.assertEquals(Status.SUCCEEDED, leafTask.getStatus());
        Assert.assertEquals(1, leafTask.getExecutionCount());
    }

    @Test
    public void testExecuteReturnsFailure() {
        leafTask.setStatusToReturn(Status.FAILED);
        behaviorTree.step();
        Assert.assertEquals(Status.FAILED, leafTask.getStatus());
        Assert.assertEquals(1, leafTask.getExecutionCount());
    }

    @Test
    public void testExecuteReturnsRunning() {
        leafTask.setStatusToReturn(Status.RUNNING);
        behaviorTree.step();
        Assert.assertEquals(Status.RUNNING, leafTask.getStatus());
        Assert.assertEquals(1, leafTask.getExecutionCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testExecuteReturnsNull() {
        leafTask.setStatusToReturn(null);
        behaviorTree.step();
    }

    @Test(expected = IllegalStateException.class)
    public void testExecuteReturnsInvalidStatus() {
        TestLeafTaskInvalidStatus invalidTask = new TestLeafTaskInvalidStatus();
        behaviorTree.addChild(invalidTask);
        behaviorTree.step();
    }

    @Test
    public void testMultipleRuns() {
        leafTask.setStatusToReturn(Status.RUNNING);
        behaviorTree.step();
        Assert.assertEquals(Status.RUNNING, leafTask.getStatus());
        Assert.assertEquals(1, leafTask.getExecutionCount());

        leafTask.setStatusToReturn(Status.SUCCEEDED);
        behaviorTree.step();
        Assert.assertEquals(Status.SUCCEEDED, leafTask.getStatus());
        Assert.assertEquals(2, leafTask.getExecutionCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddChildToTask() {
        TestLeafTask anotherTask = new TestLeafTask();
        leafTask.addChildToTask(anotherTask);
    }

    @Test
    public void testGetChildCount() {
        Assert.assertEquals(0, leafTask.getChildCount());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChild() {
        leafTask.getChild(0);
    }

    @Test
    public void testChildRunning() {
        TestLeafTask childTask = new TestLeafTask();
        leafTask.childRunning(childTask, leafTask);
    }

    @Test
    public void testChildFail() {
        TestLeafTask childTask = new TestLeafTask();
        leafTask.childFail(childTask);
    }

    @Test
    public void testChildSuccess() {
        TestLeafTask childTask = new TestLeafTask();
        leafTask.childSuccess(childTask);
    }

    @Test
    public void testTaskConstraintAnnotation() {
        TaskConstraint annotation = TestLeafTask.class.getAnnotation(TaskConstraint.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals(0, annotation.maxChildren());
    }

    @Test
    public void testInheritanceFromTask() {

        Assert.assertNotNull(leafTask);
        Assert.assertEquals(Status.FRESH, leafTask.getStatus());

        TestLeafTask guard = new TestLeafTask();
        leafTask.setGuard(guard);
        Assert.assertEquals(guard, leafTask.getGuard());
    }

    @Test
    public void testResetTask() {
        leafTask.setStatusToReturn(Status.RUNNING);
        behaviorTree.step();
        Assert.assertEquals(Status.RUNNING, leafTask.getStatus());

        leafTask.resetTask();
        Assert.assertEquals(Status.FRESH, leafTask.getStatus());
        Assert.assertNull(getPrivateField(leafTask, "tree"));
        Assert.assertNull(getPrivateField(leafTask, "control"));
    }

    @Test
    public void testReset() {
        leafTask.setStatusToReturn(Status.RUNNING);
        behaviorTree.step();
        Assert.assertEquals(Status.RUNNING, leafTask.getStatus());

        leafTask.reset();
        Assert.assertEquals(Status.FRESH, leafTask.getStatus());
        Assert.assertNull(getPrivateField(leafTask, "tree"));
        Assert.assertNull(getPrivateField(leafTask, "control"));
        Assert.assertNull(getPrivateField(leafTask, "guard"));
    }

    @Test
    public void testCloneTask() {
        leafTask.setStatusToReturn(Status.SUCCEEDED);
        TestLeafTask clonedTask = (TestLeafTask) leafTask.cloneTask();

        Assert.assertNotSame(leafTask, clonedTask);
        Assert.assertEquals(Status.FRESH, clonedTask.getStatus()); // Cloned task should be fresh
        Assert.assertEquals(0, clonedTask.getExecutionCount());
    }

    @Test
    public void testCopyTo() {
        leafTask.setStatusToReturn(Status.SUCCEEDED);
        TestLeafTask targetTask = new TestLeafTask();

        TestLeafTask copiedTask = (TestLeafTask) leafTask.copyTo(targetTask);

        Assert.assertSame(targetTask, copiedTask);
        Assert.assertEquals(Status.SUCCEEDED, copiedTask.getStatusToReturn());
    }

    @Test
    public void testStartAndEndCallbacks() {
        leafTask.start();
        leafTask.end();
    }

    @Test
    public void testCancel() {
        leafTask.setStatusToReturn(Status.RUNNING);
        behaviorTree.step();
        Assert.assertEquals(Status.RUNNING, leafTask.getStatus());

        leafTask.cancel();
        Assert.assertEquals(Status.CANCELLED, leafTask.getStatus());
    }

    @Test
    public void testGetObject() {
        leafTask.setStatusToReturn(Status.SUCCEEDED);
        behaviorTree.step();
        Assert.assertEquals("test", leafTask.getObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetObjectWithoutTree() {
        TestLeafTask taskWithoutTree = new TestLeafTask();
        taskWithoutTree.getObject();
    }

    private Object getPrivateField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static class TestLeafTask extends LeafTask<String> {
        private Status statusToReturn = Status.SUCCEEDED;
        private int executionCount = 0;

        public TestLeafTask() {
            super();
        }

        public void setStatusToReturn(Status status) {
            this.statusToReturn = status;
        }

        public Status getStatusToReturn() {
            return statusToReturn;
        }

        public int getExecutionCount() {
            return executionCount;
        }

        @Override
        public Status execute() {
            executionCount++;
            return statusToReturn;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestLeafTask leafTask = (TestLeafTask) task;
            leafTask.statusToReturn = this.statusToReturn;
            leafTask.executionCount = 0;
            return task;
        }
    }

    public static class TestLeafTaskInvalidStatus extends LeafTask<String> {
        @Override
        public Status execute() {
            return null;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return task;
        }
    }
}
