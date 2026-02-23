package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class DecoratorTest {
    private final BehaviorTree<String> behaviorTree = new BehaviorTree<>();
    private final TestTask childTask = new TestTask("child");

    @Before
    public void setUp() {
        behaviorTree.setObject("test");
        childTask.setTestStatus(Status.RUNNING);
    }

    @Test
    public void testConstructorNoChild() {
        TestDecorator decorator = new TestDecorator();
        Assert.assertEquals(0, decorator.getChildCount());
        Assert.assertNull(decorator.child);
    }

    @Test
    public void testConstructorWithChild() {
        TestDecorator decorator = new TestDecorator(childTask);
        Assert.assertEquals(1, decorator.getChildCount());
        Assert.assertEquals(childTask, decorator.child);
        Assert.assertEquals(childTask, decorator.getChild(0));
    }

    @Test
    public void testAddChildToTask() {
        TestDecorator decorator = new TestDecorator();
        int index = decorator.addChildToTask(childTask);
        Assert.assertEquals(0, index);
        Assert.assertEquals(1, decorator.getChildCount());
        Assert.assertEquals(childTask, decorator.child);
    }

    @Test
    public void testAddChildToTaskWhenAlreadyHasChild() {
        TestDecorator decorator = new TestDecorator(childTask);
        TestTask secondChild = new TestTask("secondChild");

        try {
            decorator.addChildToTask(secondChild);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("A decorator task cannot have more than one child", e.getMessage());
        }
    }

    @Test
    public void testGetChildCount() {
        TestDecorator decorator = new TestDecorator();
        Assert.assertEquals(0, decorator.getChildCount());

        decorator.addChildToTask(childTask);
        Assert.assertEquals(1, decorator.getChildCount());
    }

    @Test
    public void testGetChildValidIndex() {
        TestDecorator decorator = new TestDecorator(childTask);
        Task<String> child = decorator.getChild(0);
        Assert.assertEquals(childTask, child);
    }

    @Test
    public void testGetChildInvalidIndex() {
        TestDecorator decorator = new TestDecorator(childTask);

        try {
            decorator.getChild(1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertTrue(e.getMessage().contains("index can't be >= size: 1 >= 1"));
        }
    }

    @Test
    public void testGetChildNoChildren() {
        TestDecorator decorator = new TestDecorator();

        try {
            decorator.getChild(0);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertTrue(e.getMessage().contains("index can't be >= size: 0 >= 0"));
        }
    }

    @Test
    public void testRunWithFreshChild() {
        TestTask freshChild = new TestTask("freshChild");
        TestDecorator decorator = new TestDecorator(freshChild);

        Assert.assertEquals(1, decorator.getChildCount());
        Assert.assertEquals(freshChild, decorator.getChild(0));
        Assert.assertEquals(Status.FRESH, freshChild.getStatus());
    }

    @Test
    public void testRunWithRunningChild() {
        TestDecorator decorator = new TestDecorator(childTask);
        childTask.setTestStatus(Status.RUNNING);

        Assert.assertEquals(1, decorator.getChildCount());
        Assert.assertEquals(childTask, decorator.getChild(0));
        Assert.assertEquals(Status.RUNNING, childTask.getStatus());
    }

    @Test
    public void testRunWithChildGuardFailure() {
        TestGuard failingGuard = new TestGuard(Status.FAILED);
        TestDecorator decorator = new TestDecorator(childTask);
        childTask.setGuard(failingGuard);
        childTask.setTestStatus(Status.RUNNING);

        Assert.assertEquals(1, decorator.getChildCount());
        Assert.assertEquals(childTask, decorator.getChild(0));
        Assert.assertEquals(failingGuard, childTask.getGuard());
        Assert.assertEquals(Status.RUNNING, childTask.getStatus());
    }

    @Test
    public void testChildRunning() {
        TestDecorator decorator = new TestDecorator(childTask);
        decorator.setControl(behaviorTree);

        decorator.childRunning(childTask, decorator);

        Assert.assertEquals(Status.RUNNING, decorator.getStatus());
    }

    @Test
    public void testChildFail() {
        TestDecorator decorator = new TestDecorator(childTask);
        decorator.setControl(behaviorTree);

        decorator.childFail(childTask);

        Assert.assertEquals(Status.FAILED, decorator.getStatus());
    }

    @Test
    public void testChildSuccess() {
        TestDecorator decorator = new TestDecorator(childTask);
        decorator.setControl(behaviorTree);

        decorator.childSuccess(childTask);

        Assert.assertEquals(Status.SUCCEEDED, decorator.getStatus());
    }

    @Test
    public void testCopyToWithChild() {
        TestDecorator decorator = new TestDecorator(childTask);
        TestDecorator copy = new TestDecorator();

        decorator.copyTo(copy);

        Assert.assertNotSame(decorator.child, copy.child);
        Assert.assertEquals(childTask.getName(), ((TestTask) copy.child).getName());
        Assert.assertEquals(childTask.getTestStatus(), ((TestTask) copy.child).getTestStatus());
    }

    @Test
    public void testCopyToWithoutChild() {
        TestDecorator decorator = new TestDecorator();
        TestDecorator copy = new TestDecorator();

        decorator.copyTo(copy);

        Assert.assertNull(copy.child);
    }

    @Test
    public void testCloneTask() {
        TestDecorator decorator = new TestDecorator(childTask);
        TestDecorator cloned = (TestDecorator) decorator.cloneTask();

        Assert.assertNotSame(decorator, cloned);
        Assert.assertNotSame(decorator.child, cloned.child);
        Assert.assertEquals(childTask.getName(), ((TestTask) cloned.child).getName());
    }

    @Test
    public void testCloneTaskWithoutChild() {
        TestDecorator decorator = new TestDecorator();
        TestDecorator cloned = (TestDecorator) decorator.cloneTask();

        Assert.assertNotSame(decorator, cloned);
        Assert.assertNull(cloned.child);
    }

    @Test
    public void testReset() {
        TestDecorator decorator = new TestDecorator(childTask);
        decorator.setTestStatus(Status.RUNNING);

        decorator.reset();

        Assert.assertNull(decorator.child);
        Assert.assertEquals(Status.FRESH, decorator.getStatus());
    }

    @Test
    public void testResetTask() {
        TestDecorator decorator = new TestDecorator(childTask);
        decorator.setTestStatus(Status.RUNNING);
        decorator.setControl(behaviorTree);
        childTask.setControl(decorator);

        decorator.resetTask();

        Assert.assertEquals(childTask, decorator.child);
        Assert.assertEquals(Status.FRESH, decorator.getStatus());
    }

    @Test
    public void testAnnotationConstraints() {
        TaskConstraint constraint = TestDecorator.class.getAnnotation(TaskConstraint.class);
        Assert.assertNotNull(constraint);
        Assert.assertEquals(1, constraint.minChildren());
        Assert.assertEquals(1, constraint.maxChildren());
    }

    @Test
    public void testInheritanceFromTask() {
        TestDecorator decorator = new TestDecorator();

        Assert.assertEquals(Status.FRESH, decorator.getStatus());
        Assert.assertNull(decorator.getGuard());

        decorator.setGuard(childTask);
        Assert.assertEquals(childTask, decorator.getGuard());
    }

    @Test
    public void testMultipleOperations() {
        TestDecorator decorator = new TestDecorator();

        decorator.addChildToTask(childTask);
        Assert.assertEquals(1, decorator.getChildCount());

        TestDecorator copy = new TestDecorator();
        decorator.copyTo(copy);
        Assert.assertEquals(1, copy.getChildCount());

        decorator.reset();
        Assert.assertEquals(0, decorator.getChildCount());
        Assert.assertNull(decorator.child);
    }

    public static class TestDecorator extends Decorator<String> {
        public BehaviorTree<String> tree;
        public Task<String> guard;

        public TestDecorator() {
            super();
        }

        public TestDecorator(Task<String> child) {
            super(child);
        }

        public Status execute() {
            return Status.SUCCEEDED;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestDecorator decorator = (TestDecorator) super.copyTo(task);
            decorator.tree = this.tree;
            decorator.guard = this.guard;
            return decorator;
        }

        @Override
        public void reset() {
            super.reset();
            this.tree = null;
            this.guard = null;
        }

        public void setTestStatus(Status status) {
            try {
                Field statusField = Task.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(this, status);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Task<String> getGuard() {
            return guard;
        }

        public void setGuard(Task<String> guard) {
            this.guard = guard;
        }
    }

    public static class TestTask extends LeafTask<String> {
        String name;
        int executions = 0;

        public TestTask() {
            this.name = "default";
        }

        public TestTask(String name) {
            this.name = name;
        }

        public void setTestStatus(Status status) {
            try {
                Field statusField = Task.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(this, status);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Status getTestStatus() {
            try {
                Field statusField = Task.class.getDeclaredField("status");
                statusField.setAccessible(true);
                return (Status) statusField.get(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getName() {
            return name;
        }

        public int getExecutions() {
            return executions;
        }

        @Override
        public Status execute() {
            executions++;
            return getTestStatus();
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestTask testTask = (TestTask) task;
            testTask.name = this.name;
            testTask.setTestStatus(getTestStatus());
            return task;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class TestGuard extends LeafTask<String> {
        private final Status resultStatus;

        public TestGuard(Status resultStatus) {
            this.resultStatus = resultStatus;
        }

        @Override
        public Status execute() {
            return resultStatus;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return new TestGuard(resultStatus);
        }
    }
}
