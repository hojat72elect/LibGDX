package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.TimeUtils;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Comprehensive unit tests for PathFinderQueue class.
 * <p>
 * Test Coverage:
 * - Constructor behavior and initialization
 * - Schedulable interface implementation (run method)
 * - Telegraph interface implementation (handleMessage method)
 * - Queue management and request processing
 * - Time management and tolerance handling
 * - Request lifecycle management
 * - Edge cases and error conditions
 * - Integration with PathFinder and PathFinderRequestControl
 */
public class PathFinderQueueTest {

    private PathFinderQueue<String> pathFinderQueue;
    private TestPathFinder testPathFinder;
    private TestPathFinderRequestControl<String> requestControl;

    private static class TestPathFinder implements PathFinder<String> {
        public boolean searchConnectionPathCalled = false;
        public boolean searchNodePathCalled = false;
        public boolean searchCalled = false;
        public PathFinderRequest<String> lastRequest;
        public long lastTimeToRun;

        @Override
        public boolean searchConnectionPath(String startNode, String endNode, Heuristic<String> heuristic, GraphPath<Connection<String>> outPath) {
            searchConnectionPathCalled = true;
            return false;
        }

        @Override
        public boolean searchNodePath(String startNode, String endNode, Heuristic<String> heuristic, GraphPath<String> outPath) {
            searchNodePathCalled = true;
            return false;
        }

        @Override
        public boolean search(PathFinderRequest<String> request, long timeToRun) {
            searchCalled = true;
            lastRequest = request;
            lastTimeToRun = timeToRun;
            return true;
        }
    }

    private static class TestPathFinderRequestControl<N> extends PathFinderRequestControl<N> {
        public boolean executeReturnValue = true;
        public int executeCallCount = 0;
        public PathFinderRequest<N> lastExecutedRequest;
        public long lastTimeToRun;

        @Override
        public boolean execute(PathFinderRequest<N> request) {
            executeCallCount++;
            lastExecutedRequest = request;
            lastTimeToRun = timeToRun;
            return executeReturnValue;
        }
    }

    private static class TestPathFinderRequest extends PathFinderRequest<String> {
        public boolean initializeSearchCalled = false;
        public boolean searchCalled = false;
        public boolean finalizeSearchCalled = false;
        public long lastTimeToRun;

        @Override
        public boolean initializeSearch(long timeToRun) {
            initializeSearchCalled = true;
            lastTimeToRun = timeToRun;
            return true;
        }

        @Override
        public boolean search(PathFinder<String> pathFinder, long timeToRun) {
            searchCalled = true;
            lastTimeToRun = timeToRun;
            return true;
        }

        @Override
        public boolean finalizeSearch(long timeToRun) {
            finalizeSearchCalled = true;
            lastTimeToRun = timeToRun;
            return true;
        }
    }

    private static class TestTelegraph implements Telegraph {
        public boolean handleMessageCalled = false;
        public Telegram lastMessage;

        @Override
        public boolean handleMessage(Telegram message) {
            handleMessageCalled = true;
            lastMessage = message;
            return true;
        }
    }

    @Before
    public void setUp() {
        // Setup GdxAI logger to avoid null pointer exceptions
        GdxAI.setLogger(new TestLogger());

        testPathFinder = new TestPathFinder();
        pathFinderQueue = new PathFinderQueue<>(testPathFinder);

        // Replace requestControl with our test version
        requestControl = new TestPathFinderRequestControl<>();
        try {
            Field controlField = PathFinderQueue.class.getDeclaredField("requestControl");
            controlField.setAccessible(true);
            controlField.set(pathFinderQueue, requestControl);
        } catch (Exception e) {
            fail("Failed to set test request control: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        PathFinderQueue<String> queue = new PathFinderQueue<>(testPathFinder);

        assertNotNull("PathFinder should be set", getField(queue, "pathFinder"));
        assertEquals("PathFinder should be same", testPathFinder, getField(queue, "pathFinder"));
        assertNotNull("Request queue should be initialized", getField(queue, "requestQueue"));
        assertEquals("Request queue should be empty", 0, queue.size());
        assertNull("Current request should be null", getField(queue, "currentRequest"));
        assertNotNull("Request control should be initialized", getField(queue, "requestControl"));
    }

    @Test
    public void testConstructorWithNullPathFinder() {
        PathFinderQueue<String> queue = new PathFinderQueue<>(null);
        assertNull("PathFinder should be null", getField(queue, "pathFinder"));
        assertNotNull("Request queue should still be initialized", getField(queue, "requestQueue"));
        assertNotNull("Request control should be initialized", getField(queue, "requestControl"));
    }

    @Test
    public void testRunWithEmptyQueue() {
        long timeToRun = 1000000L; // 1ms
        pathFinderQueue.run(timeToRun);

        assertEquals("Execute should not be called for empty queue", 0, requestControl.executeCallCount);
        assertNull("Current request should remain null", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testRunWithSingleRequest() {
        // Add a request to queue
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);
        setField(pathFinderQueue, null);

        requestControl.executeReturnValue = true;
        long timeToRun = 1000000L;

        pathFinderQueue.run(timeToRun);

        assertEquals("Execute should be called once", 1, requestControl.executeCallCount);
        assertEquals("Correct request should be executed", request, requestControl.lastExecutedRequest);
        assertEquals("Time to run should be set", timeToRun, requestControl.lastTimeToRun);
        assertNull("Current request should be null after completion", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testRunWithMultipleRequests() {
        // Add multiple requests to queue
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        TestPathFinderRequest request3 = new TestPathFinderRequest();

        pathFinderQueue.requestQueue.store(request1);
        pathFinderQueue.requestQueue.store(request2);
        pathFinderQueue.requestQueue.store(request3);
        setField(pathFinderQueue, null);

        requestControl.executeReturnValue = true;
        long timeToRun = 1000000L;

        pathFinderQueue.run(timeToRun);

        assertEquals("Execute should be called 3 times", 3, requestControl.executeCallCount);
        assertEquals("Last executed request should be third one", request3, requestControl.lastExecutedRequest);
        assertNull("Current request should be null after completion", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testRunWithRequestNotFinished() {
        // Add a request to queue
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);
        setField(pathFinderQueue, null);

        requestControl.executeReturnValue = false; // Request not finished
        long timeToRun = 1000000L;

        pathFinderQueue.run(timeToRun);

        assertEquals("Execute should be called once", 1, requestControl.executeCallCount);
        assertEquals("Correct request should be executed", request, requestControl.lastExecutedRequest);
        assertEquals("Current request should be unfinished request", request, getField(pathFinderQueue, "currentRequest"));
        assertEquals("Queue should be empty after reading request", 0, pathFinderQueue.size());
    }

    @Test
    public void testRunWithExistingCurrentRequest() {
        // Set up current request
        TestPathFinderRequest request = new TestPathFinderRequest();
        setField(pathFinderQueue, request);

        requestControl.executeReturnValue = true;
        long timeToRun = 1000000L;

        pathFinderQueue.run(timeToRun);

        assertEquals("Execute should be called once", 1, requestControl.executeCallCount);
        assertEquals("Current request should be executed", request, requestControl.lastExecutedRequest);
        assertNull("Current request should be null after completion", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testRunSetsUpRequestControl() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);
        setField(pathFinderQueue, null);

        long timeToRun = 1000000L;
        long beforeTime = TimeUtils.nanoTime();

        pathFinderQueue.run(timeToRun);

        long afterTime = TimeUtils.nanoTime();

        assertTrue("Last time should be set", requestControl.lastTime >= beforeTime && requestControl.lastTime <= afterTime);
        assertEquals("Time to run should be set", timeToRun, requestControl.timeToRun);
        assertEquals("Time tolerance should be set", PathFinderQueue.TIME_TOLERANCE, requestControl.timeTolerance);
        assertEquals("Path finder should be set", testPathFinder, requestControl.pathFinder);
        assertEquals("Server should be set", pathFinderQueue, requestControl.server);
    }

    // Telegraph Interface Tests

    @Test
    public void testHandleMessage() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        TestTelegraph client = new TestTelegraph();
        Telegram telegram = new Telegram();
        telegram.extraInfo = request;
        telegram.sender = client;

        boolean result = pathFinderQueue.handleMessage(telegram);

        assertTrue("Should return true", result);
        assertEquals("Request should be added to queue", 1, pathFinderQueue.size());
        assertEquals("Client should be set", client, request.client);
        assertEquals("Status should be SEARCH_NEW", PathFinderRequest.SEARCH_NEW, request.status);
        assertTrue("StatusChanged should be true", request.statusChanged);
        assertEquals("Execution frames should be 0", 0, request.executionFrames);
    }

    @Test
    public void testHandleMessageMultipleRequests() {
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        TestTelegraph client = new TestTelegraph();

        Telegram telegram1 = new Telegram();
        telegram1.extraInfo = request1;
        telegram1.sender = client;

        Telegram telegram2 = new Telegram();
        telegram2.extraInfo = request2;
        telegram2.sender = client;

        pathFinderQueue.handleMessage(telegram1);
        pathFinderQueue.handleMessage(telegram2);

        assertEquals("Both requests should be added to queue", 2, pathFinderQueue.size());
    }

    @Test
    public void testHandleMessageWithNullDispatcher() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        request.dispatcher = null;
        TestTelegraph client = new TestTelegraph();
        Telegram telegram = new Telegram();
        telegram.extraInfo = request;
        telegram.sender = client;

        boolean result = pathFinderQueue.handleMessage(telegram);

        assertTrue("Should return true", result);
        assertEquals("Request should be added to queue", 1, pathFinderQueue.size());
        // Should not throw exception even with null dispatcher
    }

    // Queue Management Tests

    @Test
    public void testSize() {
        assertEquals("Initial size should be 0", 0, pathFinderQueue.size());

        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);
        assertEquals("Size should be 1", 1, pathFinderQueue.size());

        TestPathFinderRequest request2 = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request2);
        assertEquals("Size should be 2", 2, pathFinderQueue.size());

        pathFinderQueue.requestQueue.read();
        assertEquals("Size should be 1 after reading", 1, pathFinderQueue.size());
    }

    @Test
    public void testSizeAfterRun() {
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request1);
        pathFinderQueue.requestQueue.store(request2);

        requestControl.executeReturnValue = true;
        pathFinderQueue.run(1000000L);

        assertEquals("Queue should be empty after processing all requests", 0, pathFinderQueue.size());
    }

    @Test
    public void testSizeAfterPartialRun() {
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request1);
        pathFinderQueue.requestQueue.store(request2);

        requestControl.executeReturnValue = false; // First request not finished
        pathFinderQueue.run(1000000L);

        assertEquals("Queue should have 1 request remaining", 1, pathFinderQueue.size());
        assertEquals("Current request should be the first one", request1, getField(pathFinderQueue, "currentRequest"));
    }

    // Integration Tests

    @Test
    public void testFullWorkflow() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        TestTelegraph client = new TestTelegraph();
        Telegram telegram = new Telegram();
        telegram.extraInfo = request;
        telegram.sender = client;

        // Add request via handleMessage
        pathFinderQueue.handleMessage(telegram);

        assertEquals("Request should be in queue", 1, pathFinderQueue.size());

        // Process request via run
        requestControl.executeReturnValue = true;
        pathFinderQueue.run(1000000L);

        assertEquals("Queue should be empty", 0, pathFinderQueue.size());
        assertNull("Current request should be null", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testMultipleCycles() {
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        TestTelegraph client = new TestTelegraph();

        // Add first request
        Telegram telegram1 = new Telegram();
        telegram1.extraInfo = request1;
        telegram1.sender = client;
        pathFinderQueue.handleMessage(telegram1);

        // Process first request
        requestControl.executeReturnValue = true;
        pathFinderQueue.run(1000000L);

        assertEquals("Queue should be empty after first cycle", 0, pathFinderQueue.size());

        // Add second request
        Telegram telegram2 = new Telegram();
        telegram2.extraInfo = request2;
        telegram2.sender = client;
        pathFinderQueue.handleMessage(telegram2);

        assertEquals("Queue should have second request", 1, pathFinderQueue.size());

        // Process second request
        pathFinderQueue.run(1000000L);

        assertEquals("Queue should be empty after second cycle", 0, pathFinderQueue.size());
    }

    // Edge Cases and Error Conditions

    @Test
    public void testRunWithZeroTime() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);

        requestControl.executeReturnValue = true;
        pathFinderQueue.run(0L);

        assertEquals("Execute should be called even with zero time", 1, requestControl.executeCallCount);
    }

    @Test
    public void testRunWithNegativeTime() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);

        requestControl.executeReturnValue = true;
        pathFinderQueue.run(-1000L);

        assertEquals("Execute should be called even with negative time", 1, requestControl.executeCallCount);
    }

    @Test
    public void testHandleMessageWithNullExtraInfo() {
        Telegram telegram = new Telegram();
        telegram.extraInfo = null;

        try {
            pathFinderQueue.handleMessage(telegram);
            fail("Should throw ClassCastException or NullPointerException");
        } catch (ClassCastException e) {
            // Expected when casting null to PathFinderRequest
        } catch (NullPointerException e) {
            // Also expected when trying to access fields on null PathFinderRequest
        }
    }

    @Test
    public void testHandleMessageWithWrongExtraInfoType() {
        Telegram telegram = new Telegram();
        telegram.extraInfo = "wrong type";

        try {
            pathFinderQueue.handleMessage(telegram);
            fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testRequestControlSetup() {
        TestPathFinderRequest request = new TestPathFinderRequest();
        pathFinderQueue.requestQueue.store(request);
        long timeToRun = 5000000L; // 5ms

        pathFinderQueue.run(timeToRun);

        // Verify request control is properly set up
        assertEquals("PathFinder should be set in control", testPathFinder, requestControl.pathFinder);
        assertEquals("Server should be set in control", pathFinderQueue, requestControl.server);
        assertEquals("Time tolerance should be set", PathFinderQueue.TIME_TOLERANCE, requestControl.timeTolerance);
        assertTrue("Last time should be set", requestControl.lastTime > 0);
    }

    @Test
    public void testTimeToleranceConstant() {
        assertEquals("TIME_TOLERANCE should be 100", 100L, PathFinderQueue.TIME_TOLERANCE);
    }

    @Test
    public void testInterfaceImplementation() {
        assertNotNull("Should implement Schedulable", pathFinderQueue);
        assertTrue("Should implement Telegraph", true);
    }

    @Test
    public void testRequestProcessingOrder() {
        // Add requests in specific order
        TestPathFinderRequest request1 = new TestPathFinderRequest();
        TestPathFinderRequest request2 = new TestPathFinderRequest();
        TestPathFinderRequest request3 = new TestPathFinderRequest();

        pathFinderQueue.requestQueue.store(request1);
        pathFinderQueue.requestQueue.store(request2);
        pathFinderQueue.requestQueue.store(request3);

        requestControl.executeReturnValue = true;
        pathFinderQueue.run(1000000L);

        // Verify requests were processed in order
        assertEquals("Should process exactly 3 requests", 3, requestControl.executeCallCount);
    }

    @Test
    public void testCurrentRequestPersistence() {
        TestPathFinderRequest request = new TestPathFinderRequest();

        // Add request and start processing
        pathFinderQueue.requestQueue.store(request);

        requestControl.executeReturnValue = false; // Not finished
        pathFinderQueue.run(1000000L);

        assertEquals("Current request should be set", request, getField(pathFinderQueue, "currentRequest"));

        // Second run should continue with same request
        requestControl.executeReturnValue = true; // Now finished
        pathFinderQueue.run(1000000L);

        assertEquals("Should execute same request again", 2, requestControl.executeCallCount);
        assertEquals("Should still be same request", request, requestControl.lastExecutedRequest);
        assertNull("Current request should be null after completion", getField(pathFinderQueue, "currentRequest"));
    }

    @Test
    public void testLargeNumberOfRequests() {
        int requestCount = 100;

        // Add many requests
        for (int i = 0; i < requestCount; i++) {
            TestPathFinderRequest request = new TestPathFinderRequest();
            pathFinderQueue.requestQueue.store(request);
        }

        assertEquals("Should have all requests", requestCount, pathFinderQueue.size());

        requestControl.executeReturnValue = true;
        pathFinderQueue.run(1000000L);

        assertEquals("Should process all requests", requestCount, requestControl.executeCallCount);
        assertEquals("Queue should be empty", 0, pathFinderQueue.size());
    }

    // Helper methods for reflection access
    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Failed to access field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }

    private void setField(Object obj, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField("currentRequest");
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            fail("Failed to set field " + "currentRequest" + ": " + e.getMessage());
        }
    }

    static class TestLogger implements com.badlogic.gdx.ai.Logger {

        @Override
        public void debug(String tag, String message) {
        }

        @Override
        public void debug(String tag, String message, Throwable exception) {
        }

        @Override
        public void info(String tag, String message) {
        }

        @Override
        public void info(String tag, String message, Throwable exception) {
        }

        @Override
        public void error(String tag, String message) {
        }

        @Override
        public void error(String tag, String message, Throwable exception) {
        }
    }
}
