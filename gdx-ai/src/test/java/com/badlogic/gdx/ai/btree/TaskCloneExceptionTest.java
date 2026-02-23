package com.badlogic.gdx.ai.btree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link TaskCloneException}.
 */
public class TaskCloneExceptionTest {

    @Test
    public void testConstructorNoArgs() {
        TaskCloneException exception = new TaskCloneException();

        assertNull("Message should be null", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Test message";
        TaskCloneException exception = new TaskCloneException(message);

        assertEquals("Message should match", message, exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessage() {
        TaskCloneException exception = new TaskCloneException((String) null);

        assertNull("Message should be null", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Test cause");
        TaskCloneException exception = new TaskCloneException(cause);

        assertEquals("Cause should match", cause, exception.getCause());
        assertEquals("Message should be cause.toString()", cause.toString(), exception.getMessage());
    }

    @Test
    public void testConstructorWithNullCause() {
        TaskCloneException exception = new TaskCloneException((Throwable) null);

        assertNull("Cause should be null", exception.getCause());
        assertNull("Message should be null", exception.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        TaskCloneException exception = new TaskCloneException(message, cause);

        assertEquals("Message should match", message, exception.getMessage());
        assertEquals("Cause should match", cause, exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessageAndCause() {
        Throwable cause = new RuntimeException("Test cause");
        TaskCloneException exception = new TaskCloneException(null, cause);

        assertNull("Message should be null", exception.getMessage());
        assertEquals("Cause should match", cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndNullCause() {
        String message = "Test message";
        TaskCloneException exception = new TaskCloneException(message, null);

        assertEquals("Message should match", message, exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessageAndNullCause() {
        TaskCloneException exception = new TaskCloneException(null, null);

        assertNull("Message should be null", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testMessagePreservation() {
        String message = "Cloning failed due to reflection error";
        TaskCloneException exception = new TaskCloneException(message);

        assertEquals("Message should be preserved exactly", message, exception.getMessage());
    }

    @Test
    public void testCausePreservation() {
        Throwable cause = new IllegalStateException("Cannot access private field");
        TaskCloneException exception = new TaskCloneException(cause);

        assertSame("Cause should be preserved exactly", cause, exception.getCause());
    }

    @Test
    public void testCauseMessageGeneration() {
        String causeMessage = "Reflection error occurred";
        Throwable cause = new RuntimeException(causeMessage);
        TaskCloneException exception = new TaskCloneException(cause);

        assertEquals("Message should be generated from cause", cause.toString(), exception.getMessage());
        assertTrue("Generated message should contain cause message", exception.getMessage().contains(causeMessage));
    }

    @Test
    public void testStackTrace() {
        TaskCloneException exception = new TaskCloneException("Test message");

        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull("Stack trace should not be null", stackTrace);
        assertTrue("Stack trace should not be empty", stackTrace.length > 0);

        // Verify that the stack trace contains this test method
        boolean foundTestMethod = false;
        for (StackTraceElement element : stackTrace) {
            if (element.getMethodName().equals("testStackTrace")) {
                foundTestMethod = true;
                break;
            }
        }
        assertTrue("Stack trace should contain the test method", foundTestMethod);
    }

    @Test
    public void testExceptionChaining() {
        Throwable originalCause = new NullPointerException("Null field access");
        TaskCloneException exception = new TaskCloneException("Cloning failed", originalCause);

        // Test that we can chain exceptions properly
        Exception chainedException = new Exception("Higher level error", exception);

        assertSame("Chained exception should have TaskCloneException as cause", exception, chainedException.getCause());
        assertSame("TaskCloneException should have original cause", originalCause, exception.getCause());
    }

    @Test
    public void testToString() {
        String message = "Test message";
        TaskCloneException exception = new TaskCloneException(message);

        String toString = exception.toString();
        assertNotNull("toString() should not return null", toString);
        assertTrue("toString() should contain class name", toString.contains(TaskCloneException.class.getSimpleName()));
        assertTrue("toString() should contain message", toString.contains(message));
    }

    @Test
    public void testToStringWithCause() {
        String message = "Test message";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        TaskCloneException exception = new TaskCloneException(message, cause);

        String toString = exception.toString();
        assertNotNull("toString() should not return null", toString);
        assertTrue("toString() should contain class name", toString.contains(TaskCloneException.class.getSimpleName()));
        assertTrue("toString() should contain message", toString.contains(message));
    }

    @Test
    public void testMultipleConstructorsWithSameParameters() {
        String message = "Test";
        Throwable cause = new RuntimeException("Cause");

        // Test that different constructors with same parameters produce equivalent results
        TaskCloneException exception1 = new TaskCloneException(message);
        TaskCloneException exception2 = new TaskCloneException(message);

        assertEquals("Messages should be equal", exception1.getMessage(), exception2.getMessage());
        assertEquals("Causes should be equal", exception1.getCause(), exception2.getCause());

        TaskCloneException exception3 = new TaskCloneException(cause);
        TaskCloneException exception4 = new TaskCloneException(cause);

        assertEquals("Messages should be equal", exception3.getMessage(), exception4.getMessage());
        assertEquals("Causes should be equal", exception3.getCause(), exception4.getCause());
    }

    @Test
    public void testEmptyMessage() {
        TaskCloneException exception = new TaskCloneException("");

        assertEquals("Empty message should be preserved", "", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testMessageWithSpecialCharacters() {
        String message = "Cloning failed: \n\tSpecial chars: !@#$%^&*()";
        TaskCloneException exception = new TaskCloneException(message);

        assertEquals("Special characters should be preserved", message, exception.getMessage());
    }
}
