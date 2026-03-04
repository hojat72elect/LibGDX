package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class PathFinderRequestTest {

    private TestNode startNode;
    private TestNode endNode;
    private TestHeuristic heuristic;
    private TestGraphPath resultPath;
    private MessageDispatcher dispatcher;
    private TestTelegraph client;
    private TestPathFinder pathFinder;

    @Before
    public void setUp() {
        startNode = new TestNode("start");
        endNode = new TestNode("end");
        heuristic = new TestHeuristic();
        resultPath = new TestGraphPath();
        dispatcher = new MessageDispatcher();
        client = new TestTelegraph();
        pathFinder = new TestPathFinder();
    }

    @Test
    public void testConstructorNoArgs() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        assertNull("startNode should be null", request.startNode);
        assertNull("endNode should be null", request.endNode);
        assertNull("heuristic should be null", request.heuristic);
        assertNull("resultPath should be null", request.resultPath);
        assertEquals("executionFrames should be 0", 0, request.executionFrames);
        assertFalse("pathFound should be false", request.pathFound);
        assertEquals("status should be 0 (default)", 0, request.status);
        assertFalse("statusChanged should be false", request.statusChanged);
        assertNull("client should be null", request.client);
        assertEquals("responseMessageCode should be 0 (default)", 0, request.responseMessageCode);
        assertNull("dispatcher should be null", request.dispatcher);
    }

    @Test
    public void testConstructorWithParameters() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath);

        assertSame("startNode should be set", startNode, request.startNode);
        assertSame("endNode should be set", endNode, request.endNode);
        assertSame("heuristic should be set", heuristic, request.heuristic);
        assertSame("resultPath should be set", resultPath, request.resultPath);
        assertEquals("executionFrames should be 0", 0, request.executionFrames);
        assertFalse("pathFound should be false", request.pathFound);
        assertEquals("status should be SEARCH_NEW", PathFinderRequest.SEARCH_NEW, request.status);
        assertFalse("statusChanged should be false", request.statusChanged);
        assertNull("client should be null", request.client);
        assertEquals("responseMessageCode should be 0", 0, request.responseMessageCode);
        assertNotNull("dispatcher should be MessageManager instance", request.dispatcher);
        assertSame("dispatcher should be MessageManager.getInstance()", MessageManager.getInstance(), request.dispatcher);
    }

    @Test
    public void testConstructorWithParametersAndDispatcher() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);

        assertSame("startNode should be set", startNode, request.startNode);
        assertSame("endNode should be set", endNode, request.endNode);
        assertSame("heuristic should be set", heuristic, request.heuristic);
        assertSame("resultPath should be set", resultPath, request.resultPath);
        assertSame("dispatcher should be set", dispatcher, request.dispatcher);
        assertEquals("executionFrames should be 0", 0, request.executionFrames);
        assertFalse("pathFound should be false", request.pathFound);
        assertEquals("status should be SEARCH_NEW", PathFinderRequest.SEARCH_NEW, request.status);
        assertFalse("statusChanged should be false", request.statusChanged);
        assertNull("client should be null", request.client);
        assertEquals("responseMessageCode should be 0", 0, request.responseMessageCode);
    }

    @Test
    public void testConstructorWithNullParameters() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(null, null, null, null, null);

        assertNull("startNode should be null", request.startNode);
        assertNull("endNode should be null", request.endNode);
        assertNull("heuristic should be null", request.heuristic);
        assertNull("resultPath should be null", request.resultPath);
        assertNull("dispatcher should be null", request.dispatcher);
        assertEquals("executionFrames should be 0", 0, request.executionFrames);
        assertFalse("pathFound should be false", request.pathFound);
        assertEquals("status should be SEARCH_NEW", PathFinderRequest.SEARCH_NEW, request.status);
        assertFalse("statusChanged should be false", request.statusChanged);
        assertNull("client should be null", request.client);
        assertEquals("responseMessageCode should be 0", 0, request.responseMessageCode);
    }

    @Test
    public void testConstants() {
        assertEquals("SEARCH_NEW should be 0", 0, PathFinderRequest.SEARCH_NEW);
        assertEquals("SEARCH_INITIALIZED should be 1", 1, PathFinderRequest.SEARCH_INITIALIZED);
        assertEquals("SEARCH_DONE should be 2", 2, PathFinderRequest.SEARCH_DONE);
        assertEquals("SEARCH_FINALIZED should be 3", 3, PathFinderRequest.SEARCH_FINALIZED);
    }

    @Test
    public void testChangeStatus() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        assertEquals("initial status should be 0", 0, request.status);
        assertFalse("initial statusChanged should be false", request.statusChanged);

        request.changeStatus(PathFinderRequest.SEARCH_INITIALIZED);
        assertEquals("status should be SEARCH_INITIALIZED", PathFinderRequest.SEARCH_INITIALIZED, request.status);
        assertTrue("statusChanged should be true", request.statusChanged);

        request.changeStatus(PathFinderRequest.SEARCH_DONE);
        assertEquals("status should be SEARCH_DONE", PathFinderRequest.SEARCH_DONE, request.status);
        assertTrue("statusChanged should still be true", request.statusChanged);

        request.changeStatus(PathFinderRequest.SEARCH_FINALIZED);
        assertEquals("status should be SEARCH_FINALIZED", PathFinderRequest.SEARCH_FINALIZED, request.status);
        assertTrue("statusChanged should still be true", request.statusChanged);
    }

    @Test
    public void testChangeStatusWithCustomValue() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        request.changeStatus(999);
        assertEquals("status should be 999", 999, request.status);
        assertTrue("statusChanged should be true", request.statusChanged);
    }

    @Test
    public void testChangeStatusMultipleTimes() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        for (int i = 0; i < 10; i++) {
            request.changeStatus(i);
            assertEquals("status should be " + i, i, request.status);
            assertTrue("statusChanged should be true", request.statusChanged);
        }
    }

    @Test
    public void testInitializeSearch() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        assertTrue("initializeSearch should return true", request.initializeSearch(1000));
        assertTrue("initializeSearch should return true with zero time", request.initializeSearch(0));
        assertTrue("initializeSearch should return true with negative time", request.initializeSearch(-1));
        assertTrue("initializeSearch should return true with large time", request.initializeSearch(Long.MAX_VALUE));
    }

    @Test
    public void testInitializeSearchMultipleCalls() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        for (int i = 0; i < 10; i++) {
            assertTrue("initializeSearch should always return true", request.initializeSearch(i * 1000));
        }
    }

    @Test
    public void testSearch() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath);

        assertTrue("search should delegate to pathFinder", request.search(pathFinder, 1000));
        assertTrue("pathFinder.search should have been called", pathFinder.searchCalled);
        assertSame("request should be passed to pathFinder", request, pathFinder.lastRequest);
        assertEquals("timeToRun should be passed to pathFinder", 1000, pathFinder.lastTimeToRun);
    }

    @Test
    public void testSearchWithDifferentTimes() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath);

        long[] times = {0, 1, 1000, Long.MAX_VALUE};
        for (long time : times) {
            pathFinder.reset();
            assertTrue("search should delegate to pathFinder", request.search(pathFinder, time));
            assertTrue("pathFinder.search should have been called", pathFinder.searchCalled);
            assertEquals("timeToRun should be passed to pathFinder", time, pathFinder.lastTimeToRun);
        }
    }

    @Test
    public void testSearchWithNullPathFinder() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        try {
            request.search(null, 1000);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Expected NullPointerException", true);
        }
    }

    @Test
    public void testFinalizeSearch() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        assertTrue("finalizeSearch should return true", request.finalizeSearch(1000));
        assertTrue("finalizeSearch should return true with zero time", request.finalizeSearch(0));
        assertTrue("finalizeSearch should return true with negative time", request.finalizeSearch(-1));
        assertTrue("finalizeSearch should return true with large time", request.finalizeSearch(Long.MAX_VALUE));
    }

    @Test
    public void testFinalizeSearchMultipleCalls() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        for (int i = 0; i < 10; i++) {
            assertTrue("finalizeSearch should always return true", request.finalizeSearch(i * 1000));
        }
    }

    @Test
    public void testFieldAccess() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        // Test setting and getting all public fields
        request.startNode = startNode;
        request.endNode = endNode;
        request.heuristic = heuristic;
        request.resultPath = resultPath;
        request.executionFrames = 42;
        request.pathFound = true;
        request.status = PathFinderRequest.SEARCH_DONE;
        request.statusChanged = true;
        request.client = client;
        request.responseMessageCode = 123;
        request.dispatcher = dispatcher;

        assertSame("startNode should be accessible", startNode, request.startNode);
        assertSame("endNode should be accessible", endNode, request.endNode);
        assertSame("heuristic should be accessible", heuristic, request.heuristic);
        assertSame("resultPath should be accessible", resultPath, request.resultPath);
        assertEquals("executionFrames should be accessible", 42, request.executionFrames);
        assertTrue("pathFound should be accessible", request.pathFound);
        assertEquals("status should be accessible", PathFinderRequest.SEARCH_DONE, request.status);
        assertTrue("statusChanged should be accessible", request.statusChanged);
        assertSame("client should be accessible", client, request.client);
        assertEquals("responseMessageCode should be accessible", 123, request.responseMessageCode);
        assertSame("dispatcher should be accessible", dispatcher, request.dispatcher);
    }

    // Integration Tests

    @Test
    public void testCompleteWorkflow() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);

        // Initial state
        assertEquals("status should be SEARCH_NEW", PathFinderRequest.SEARCH_NEW, request.status);
        assertFalse("statusChanged should be false", request.statusChanged);
        assertFalse("pathFound should be false", request.pathFound);
        assertEquals("executionFrames should be 0", 0, request.executionFrames);

        // Initialize search
        assertTrue("initializeSearch should return true", request.initializeSearch(1000));

        // Change status to initialized
        request.changeStatus(PathFinderRequest.SEARCH_INITIALIZED);
        assertEquals("status should be SEARCH_INITIALIZED", PathFinderRequest.SEARCH_INITIALIZED, request.status);
        assertTrue("statusChanged should be true", request.statusChanged);

        // Perform search
        assertTrue("search should return true", request.search(pathFinder, 1000));
        assertTrue("pathFinder should have been called", pathFinder.searchCalled);

        // Change status to done
        request.changeStatus(PathFinderRequest.SEARCH_DONE);
        assertEquals("status should be SEARCH_DONE", PathFinderRequest.SEARCH_DONE, request.status);

        // Finalize search
        assertTrue("finalizeSearch should return true", request.finalizeSearch(1000));

        // Change status to finalized
        request.changeStatus(PathFinderRequest.SEARCH_FINALIZED);
        assertEquals("status should be SEARCH_FINALIZED", PathFinderRequest.SEARCH_FINALIZED, request.status);
    }

    @Test
    public void testWorkflowWithNullFields() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>();

        // Should work even with null fields
        assertTrue("initializeSearch should work with null fields", request.initializeSearch(1000));
        request.changeStatus(PathFinderRequest.SEARCH_INITIALIZED);

        try {
            request.search(null, 1000);
            fail("Should throw NullPointerException for null pathFinder");
        } catch (NullPointerException e) {
            assertTrue("Expected NullPointerException", true);
        }

        assertTrue("finalizeSearch should work with null fields", request.finalizeSearch(1000));
    }

    private static class TestNode {
        final String name;

        TestNode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestHeuristic implements Heuristic<TestNode> {
        @Override
        public float estimate(TestNode node, TestNode endNode) {
            return node.name.equals(endNode.name) ? 0.0f : 1.0f;
        }
    }

    private static class TestGraphPath implements GraphPath<TestNode> {
        private final Array<TestNode> nodes = new Array<>();

        @Override
        public void clear() {
            nodes.clear();
        }

        @Override
        public int getCount() {
            return nodes.size;
        }

        @Override
        public void add(TestNode node) {
            nodes.add(node);
        }

        @Override
        public TestNode get(int index) {
            return nodes.get(index);
        }

        @Override
        public void reverse() {
            nodes.reverse();
        }

        @NotNull
        @Override
        public Iterator<TestNode> iterator() {
            return nodes.iterator();
        }
    }

    private static class TestTelegraph implements Telegraph {
        @Override
        public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {
            return true;
        }
    }

    private static class TestPathFinder implements PathFinder<TestNode> {
        boolean searchCalled = false;
        boolean searchConnectionPathCalled = false;
        boolean searchNodePathCalled = false;
        PathFinderRequest<TestNode> lastRequest;
        long lastTimeToRun;
        TestNode lastStartNode;
        TestNode lastEndNode;
        Heuristic<TestNode> lastHeuristic;
        GraphPath<TestNode> lastOutPath;
        GraphPath<Connection<TestNode>> lastConnectionPath;

        void reset() {
            searchCalled = false;
            searchConnectionPathCalled = false;
            searchNodePathCalled = false;
            lastRequest = null;
            lastTimeToRun = 0;
            lastStartNode = null;
            lastEndNode = null;
            lastHeuristic = null;
            lastOutPath = null;
            lastConnectionPath = null;
        }

        @Override
        public boolean searchConnectionPath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<Connection<TestNode>> outPath) {
            searchConnectionPathCalled = true;
            lastStartNode = startNode;
            lastEndNode = endNode;
            lastHeuristic = heuristic;
            lastConnectionPath = outPath;
            return true;
        }

        @Override
        public boolean searchNodePath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<TestNode> outPath) {
            searchNodePathCalled = true;
            lastStartNode = startNode;
            lastEndNode = endNode;
            lastHeuristic = heuristic;
            lastOutPath = outPath;
            return true;
        }

        @Override
        public boolean search(PathFinderRequest<TestNode> request, long timeToRun) {
            searchCalled = true;
            lastRequest = request;
            lastTimeToRun = timeToRun;
            return true;
        }
    }
}
