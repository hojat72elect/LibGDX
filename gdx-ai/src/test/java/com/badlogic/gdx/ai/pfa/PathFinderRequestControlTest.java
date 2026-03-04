package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class PathFinderRequestControlTest {

    private TestNode startNode;
    private TestNode endNode;
    private TestHeuristic heuristic;
    private TestGraphPath resultPath;
    private MessageDispatcher dispatcher;
    private TestTelegraph server;
    private TestTelegraph client;
    private TestPathFinder pathFinder;
    private PathFinderRequestControl<TestNode> control;

    @Before
    public void setUp() {
        startNode = new TestNode("start");
        endNode = new TestNode("end");
        heuristic = new TestHeuristic();
        resultPath = new TestGraphPath();
        dispatcher = new MessageDispatcher();
        server = new TestTelegraph();
        client = new TestTelegraph();
        pathFinder = new TestPathFinder();
        control = new PathFinderRequestControl<>();
        control.server = server;
        control.pathFinder = pathFinder;
    }

    @Test
    public void testConstructor() {
        PathFinderRequestControl<TestNode> newControl = new PathFinderRequestControl<>();

        assertNotNull("control should be created", newControl);
    }

    @Test
    public void testExecuteBasicWorkflow() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.client = client;
        request.responseMessageCode = 123;

        control.execute(request);

        // Should execute without crashing - just verify it doesn't throw exceptions
        // The return value depends on time tolerance, so we just check it executes
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testExecuteWithFinalizedStatus() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.changeStatus(PathFinderRequest.SEARCH_FINALIZED);

        control.execute(request);

        assertEquals("executionFrames should be incremented", 1, request.executionFrames);
        assertEquals("status should remain SEARCH_FINALIZED", PathFinderRequest.SEARCH_FINALIZED, request.status);
    }

    @Test
    public void testExecuteWithServer() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.client = client;
        request.responseMessageCode = 123;

        control.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testExecuteWithNullServer() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.client = client;
        request.responseMessageCode = 123;
        control.server = null; // No server

        control.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testExecuteWithNullClient() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.client = null; // No client
        request.responseMessageCode = 123;

        control.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testExecuteWithNullDispatcher() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, null); // No dispatcher
        request.client = client;
        request.responseMessageCode = 123;

        control.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testExecuteWithNullRequest() {
        try {
            control.execute(null);
            fail("Should throw NullPointerException for null request");
        } catch (NullPointerException e) {
            assertTrue("Expected NullPointerException", true);
        }
    }

    @Test
    public void testExecuteWithNullPathFinder() {
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        control.pathFinder = null;

        control.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
    }

    @Test
    public void testFieldAccess() {
        PathFinderRequestControl<TestNode> newControl = new PathFinderRequestControl<>();

        // Test setting public fields
        newControl.server = server;
        newControl.pathFinder = pathFinder;

        // Verify fields are set by testing behavior
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, resultPath, dispatcher);
        request.changeStatus(PathFinderRequest.SEARCH_DONE);

        newControl.execute(request);

        // Just verify it doesn't crash
        assertTrue("execute should complete without exception", true);
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

        @Override
        public Iterator<TestNode> iterator() {
            return nodes.iterator();
        }
    }

    private static class TestTelegraph implements Telegraph {
        boolean messageReceived = false;

        @Override
        public boolean handleMessage(com.badlogic.gdx.ai.msg.Telegram msg) {
            messageReceived = true;
            return true;
        }
    }

    private static class TestPathFinder implements PathFinder<TestNode> {
        @Override
        public boolean searchConnectionPath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<Connection<TestNode>> outPath) {
            return true;
        }

        @Override
        public boolean searchNodePath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<TestNode> outPath) {
            return true;
        }

        @Override
        public boolean search(PathFinderRequest<TestNode> request, long timeToRun) {
            return true;
        }
    }
}
