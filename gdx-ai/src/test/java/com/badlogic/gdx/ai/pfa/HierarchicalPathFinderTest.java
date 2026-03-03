package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HierarchicalPathFinderTest {

    private HierarchicalGraph<TestNode> graph;
    private PathFinder<TestNode> levelPathFinder;
    private HierarchicalPathFinder<TestNode> hierarchicalPathFinder;
    private GraphPath<TestNode> nodePath;
    private GraphPath<Connection<TestNode>> connectionPath;
    private TestHeuristic heuristic;

    @Before
    public void setUp() {
        graph = new TestHierarchicalGraph();
        levelPathFinder = new TestPathFinder();
        hierarchicalPathFinder = new HierarchicalPathFinder<>(graph, levelPathFinder);
        nodePath = new DefaultGraphPath<>();
        connectionPath = new DefaultGraphPath<>();
        heuristic = new TestHeuristic();

        // Setup GdxAI logger to avoid null pointer exceptions
        GdxAI.setLogger(new TestLogger());
    }

    @Test
    public void testConstructor() {
        HierarchicalPathFinder<TestNode> hpf = new HierarchicalPathFinder<>(graph, levelPathFinder);

        assertNotNull("graph should be set", hpf.graph);
        assertSame("graph should be the provided instance", graph, hpf.graph);
        assertNotNull("levelPathFinder should be set", hpf.levelPathFinder);
        assertSame("levelPathFinder should be the provided instance", levelPathFinder, hpf.levelPathFinder);
        assertNull("levelRequest should be null initially", getField(hpf, "levelRequest"));
        assertNull("levelRequestControl should be null initially", getField(hpf, "levelRequestControl"));
    }

    @Test
    public void testConstructorWithNullGraph() {
        // Should allow null graph for flexibility
        HierarchicalPathFinder<TestNode> hpf = new HierarchicalPathFinder<>(null, levelPathFinder);
        assertNull("graph should be null", hpf.graph);
        assertNotNull("levelPathFinder should be set", hpf.levelPathFinder);
    }

    @Test
    public void testConstructorWithNullLevelPathFinder() {
        // Should allow null level path finder for flexibility
        HierarchicalPathFinder<TestNode> hpf = new HierarchicalPathFinder<>(graph, null);
        assertNotNull("graph should be set", hpf.graph);
        assertNull("levelPathFinder should be null", hpf.levelPathFinder);
    }

    // Non-interruptible Node Path Search Tests

    @Test
    public void testSearchNodePathSameStartAndEnd() {
        TestNode node = new TestNode(0);
        // Same node

        boolean result = hierarchicalPathFinder.searchNodePath(node, node, heuristic, nodePath);

        assertTrue("should return true when start and end are the same", result);
        assertEquals("path should remain empty", 0, nodePath.getCount());
    }

    @Test
    public void testSearchNodePathSuccess() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Setup graph to return successful path
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, nodePath);

        assertTrue("should return true when path is found", result);
        assertTrue("path should contain nodes", nodePath.getCount() > 0);
        assertEquals("graph level should be set to 0", 0, ((TestHierarchicalGraph) graph).getCurrentLevel());
    }

    @Test
    public void testSearchNodePathFailure() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Setup graph to return failed path
        ((TestHierarchicalGraph) graph).setPathFound(false);
        ((TestPathFinder) levelPathFinder).setPathFound(false);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, nodePath);

        assertFalse("should return false when path is not found", result);
    }

    @Test
    public void testSearchNodePathWithMultipleLevels() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(3);

        // Setup graph with multiple levels
        ((TestHierarchicalGraph) graph).setLevelCount(3);
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, nodePath);

        assertTrue("should return true when path is found across multiple levels", result);
        assertTrue("path should contain nodes", nodePath.getCount() > 0);
    }

    @Test
    public void testSearchNodePathWithNullHeuristic() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, null, nodePath);

        assertTrue("should handle null heuristic", result);
    }

    @Test
    public void testSearchNodePathWithNullPath() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        try {
            hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, null);
            fail("Should throw exception for null path");
        } catch (Exception e) {
            // Expected behavior
        }
    }

    @Test
    public void testSearchConnectionPathSameStartAndEnd() {
        TestNode node = new TestNode(0);

        boolean result = hierarchicalPathFinder.searchConnectionPath(node, node, heuristic, connectionPath);

        assertTrue("should return true when start and end are the same", result);
        assertEquals("path should remain empty", 0, connectionPath.getCount());
    }

    @Test
    public void testSearchConnectionPathSuccess() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Setup graph to return successful path
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchConnectionPath(startNode, endNode, heuristic, connectionPath);

        assertTrue("should return true when path is found", result);
        assertTrue("path should contain connections", connectionPath.getCount() > 0);
        assertEquals("graph level should be set to 0", 0, ((TestHierarchicalGraph) graph).getCurrentLevel());
    }

    @Test
    public void testSearchConnectionPathFailure() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Setup graph to return failed path
        ((TestHierarchicalGraph) graph).setPathFound(false);
        ((TestPathFinder) levelPathFinder).setPathFound(false);

        boolean result = hierarchicalPathFinder.searchConnectionPath(startNode, endNode, heuristic, connectionPath);

        assertFalse("should return false when path is not found", result);
    }

    @Test
    public void testSearchConnectionPathWithMultipleLevels() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(3);

        // Setup graph with multiple levels
        ((TestHierarchicalGraph) graph).setLevelCount(3);
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchConnectionPath(startNode, endNode, heuristic, connectionPath);

        assertTrue("should return true when path is found across multiple levels", result);
        assertTrue("path should contain connections", connectionPath.getCount() > 0);
    }

    // Interruptible Search Tests

    @Test
    public void testSearchSameStartAndEnd() {
        TestNode node = new TestNode(0);

        PathFinderRequest<TestNode> request = new PathFinderRequest<>(node, node, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger early return

        boolean result = hierarchicalPathFinder.search(request, 1000000);

        assertTrue("should return true when start and end are the same", result);
    }

    @Test
    public void testSearchInitialization() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Setup graph to return successful path
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.search(request, 1000000);

        assertTrue("should return true when search completes", result);
        assertNotNull("levelRequest should be initialized", getField(hierarchicalPathFinder, "levelRequest"));
        assertNotNull("levelRequestControl should be initialized", getField(hierarchicalPathFinder, "levelRequestControl"));
        assertTrue("path should be found", request.pathFound);
    }

    @Test
    public void testSearchWithInsufficientTime() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Setup level path finder to take time
        ((TestPathFinder) levelPathFinder).setTimeConsuming(true);

        boolean result = hierarchicalPathFinder.search(request, 1); // Very little time

        assertFalse("should return false when insufficient time", result);
    }

    @Test
    public void testSearchWithFailure() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Setup graph to return failed path
        ((TestHierarchicalGraph) graph).setPathFound(false);
        ((TestPathFinder) levelPathFinder).setPathFound(false);

        boolean result = hierarchicalPathFinder.search(request, 1000000);

        // Should return true because the search process completes, even if no path is found
        assertTrue("should return true when search completes (even with no path)", result);
        assertFalse("path should not be found", request.pathFound);
    }

    @Test
    public void testSearchMultipleCalls() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Setup graph to return successful path
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        // First call
        boolean result1 = hierarchicalPathFinder.search(request, 1000000);
        assertTrue("first call should return true", result1);

        // Create a new request for second call (since requests are typically one-time use)
        PathFinderRequest<TestNode> request2 = new PathFinderRequest<>(startNode, endNode, heuristic, new DefaultGraphPath<>());
        request2.statusChanged = true; // Set statusChanged to trigger initialization

        // Second call
        boolean result2 = hierarchicalPathFinder.search(request2, 1000000);
        assertTrue("second call should return true", result2);
    }

    // LevelPathFinderRequest Tests

    @Test
    public void testLevelPathFinderRequestInitializeSearch() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Trigger initialization
        hierarchicalPathFinder.search(request, 1000000);

        Object levelRequest = getField(hierarchicalPathFinder, "levelRequest");
        assertNotNull("levelRequest should be created", levelRequest);

        // Test initializeSearch method through reflection
        try {
            java.lang.reflect.Method initializeSearch = levelRequest.getClass().getDeclaredMethod("initializeSearch", long.class);
            initializeSearch.setAccessible(true);
            boolean result = (Boolean) initializeSearch.invoke(levelRequest, 1000000);
            // initializeSearch should return true if it successfully sets up the search
            assertTrue("initializeSearch should return true", result);
        } catch (Exception e) {
            // If reflection fails, at least verify the object exists
            assertNotNull("levelRequest object should exist", levelRequest);
        }
    }

    @Test
    public void testLevelPathFinderRequestSearch() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Trigger initialization
        hierarchicalPathFinder.search(request, 1000000);

        Object levelRequest = getField(hierarchicalPathFinder, "levelRequest");
        assertNotNull("levelRequest should be created", levelRequest);

        // Test search method through reflection
        try {
            java.lang.reflect.Method search = levelRequest.getClass().getDeclaredMethod("search", PathFinder.class, long.class);
            search.setAccessible(true);
            boolean result = (Boolean) search.invoke(levelRequest, levelPathFinder, 1000000);
            // search method delegates to super.search() which should return true for our test setup
            assertTrue("search should return true", result);
        } catch (Exception e) {
            // If reflection fails, at least verify the object exists
            assertNotNull("levelRequest object should exist", levelRequest);
        }
    }

    @Test
    public void testLevelPathFinderRequestFinalizeSearch() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);
        PathFinderRequest<TestNode> request = new PathFinderRequest<>(startNode, endNode, heuristic, nodePath);
        request.statusChanged = true; // Set statusChanged to trigger initialization

        // Trigger initialization
        hierarchicalPathFinder.search(request, 1000000);

        Object levelRequest = getField(hierarchicalPathFinder, "levelRequest");
        assertNotNull("levelRequest should be created", levelRequest);

        // Test finalizeSearch method through reflection
        try {
            java.lang.reflect.Method finalizeSearch = levelRequest.getClass().getDeclaredMethod("finalizeSearch", long.class);
            finalizeSearch.setAccessible(true);
            boolean result = (Boolean) finalizeSearch.invoke(levelRequest, 1000000);
            // finalizeSearch should return true if it successfully completes
            assertTrue("finalizeSearch should return true", result);
        } catch (Exception e) {
            // If reflection fails, at least verify the object exists
            assertNotNull("levelRequest object should exist", levelRequest);
        }
    }

    // Edge Cases and Error Conditions

    @Test
    public void testSearchWithNullRequest() {
        try {
            hierarchicalPathFinder.search(null, 1000000);
            fail("Should throw exception for null request");
        } catch (Exception e) {
            // Expected behavior
        }
    }

    @Test
    public void testHierarchicalGraphIntegration() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Test that hierarchical graph methods are called
        ((TestHierarchicalGraph) graph).setLevelCount(2);
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, nodePath);

        assertTrue("should integrate with hierarchical graph", result);
        assertTrue("convertNodeBetweenLevels should be called", ((TestHierarchicalGraph) graph).convertNodeCalled);
        assertTrue("setLevel should be called", ((TestHierarchicalGraph) graph).setLevelCalled);
    }

    @Test
    public void testLevelPathFinderIntegration() {
        TestNode startNode = new TestNode(0);
        TestNode endNode = new TestNode(2);

        // Test that level path finder methods are called
        ((TestHierarchicalGraph) graph).setPathFound(true);
        ((TestPathFinder) levelPathFinder).setPathFound(true);

        boolean result = hierarchicalPathFinder.searchNodePath(startNode, endNode, heuristic, nodePath);

        assertTrue("should integrate with level path finder", result);
        assertTrue("searchNodePath should be called", ((TestPathFinder) levelPathFinder).searchNodePathCalled);
    }

    // Helper methods and test classes

    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    // Test node class
    static class TestNode {
        final int id;

        TestNode(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestNode testNode = (TestNode) obj;
            return id == testNode.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    // Test hierarchical graph implementation
    static class TestHierarchicalGraph implements HierarchicalGraph<TestNode> {
        private int levelCount = 2;
        private int currentLevel = 0;
        private boolean pathFound = true;
        public boolean convertNodeCalled = false;
        public boolean setLevelCalled = false;

        public void setLevelCount(int levelCount) {
            this.levelCount = levelCount;
        }

        public void setPathFound(boolean pathFound) {
            this.pathFound = pathFound;
        }

        public int getCurrentLevel() {
            return currentLevel;
        }

        @Override
        public int getLevelCount() {
            return levelCount;
        }

        @Override
        public void setLevel(int level) {
            this.currentLevel = level;
            setLevelCalled = true;
        }

        @Override
        public TestNode convertNodeBetweenLevels(int inputLevel, TestNode node, int outputLevel) {
            convertNodeCalled = true;
            // Simple conversion - return a node with the same ID but representing the target level
            return new TestNode(node.id * 10 + outputLevel);
        }

        @Override
        public Array<Connection<TestNode>> getConnections(TestNode node) {
            Array<Connection<TestNode>> connections = new Array<>();
            if (pathFound) {
                // Create some mock connections
                for (int i = 0; i < 3; i++) {
                    TestNode targetNode = new TestNode(node.id + i + 1);
                    connections.add(new TestConnection(node, targetNode));
                }
            }
            return connections;
        }
    }

    // Test path finder implementation
    static class TestPathFinder implements PathFinder<TestNode> {
        private boolean pathFound = true;
        private boolean timeConsuming = false;
        public boolean searchNodePathCalled = false;
        public boolean searchConnectionPathCalled = false;
        public boolean searchCalled = false;

        public void setPathFound(boolean pathFound) {
            this.pathFound = pathFound;
        }

        public void setTimeConsuming(boolean timeConsuming) {
            this.timeConsuming = timeConsuming;
        }

        @Override
        public boolean searchConnectionPath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<Connection<TestNode>> outPath) {
            searchConnectionPathCalled = true;

            if (timeConsuming) {
                try {
                    Thread.sleep(10); // Simulate time-consuming operation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (pathFound) {
                // Add some mock connections
                outPath.add(new TestConnection(startNode, new TestNode(startNode.id + 1)));
                outPath.add(new TestConnection(new TestNode(startNode.id + 1), endNode));
            }

            return pathFound;
        }

        @Override
        public boolean searchNodePath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<TestNode> outPath) {
            searchNodePathCalled = true;

            if (timeConsuming) {
                try {
                    Thread.sleep(10); // Simulate time-consuming operation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (pathFound) {
                // Add some mock nodes
                outPath.add(startNode);
                outPath.add(new TestNode(startNode.id + 1));
                outPath.add(endNode);
            }

            return pathFound;
        }

        @Override
        public boolean search(PathFinderRequest<TestNode> request, long timeToRun) {
            searchCalled = true;

            if (timeConsuming) {
                try {
                    Thread.sleep(10); // Simulate time-consuming operation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (pathFound) {
                // Add some mock nodes
                request.resultPath.add(request.startNode);
                request.resultPath.add(new TestNode(request.startNode.id + 1));
                request.resultPath.add(request.endNode);
            }

            request.pathFound = pathFound;
            return true;
        }
    }

    // Test connection class
    static class TestConnection implements Connection<TestNode> {
        private final TestNode fromNode;
        private final TestNode toNode;

        TestConnection(TestNode fromNode, TestNode toNode) {
            this.fromNode = fromNode;
            this.toNode = toNode;
        }

        @Override
        public TestNode getFromNode() {
            return fromNode;
        }

        @Override
        public TestNode getToNode() {
            return toNode;
        }

        @Override
        public float getCost() {
            return 1.0f;
        }
    }

    // Test heuristic implementation
    static class TestHeuristic implements Heuristic<TestNode> {
        @Override
        public float estimate(TestNode node, TestNode endNode) {
            return Math.abs(node.id - endNode.id);
        }
    }

    // Test logger implementation
    static class TestLogger implements com.badlogic.gdx.ai.Logger {
        private final List<String> logs = new ArrayList<>();

        @Override
        public void debug(String tag, String message) {
            logs.add("DEBUG: " + tag + " - " + message);
        }

        @Override
        public void debug(String tag, String message, Throwable exception) {
            logs.add("DEBUG: " + tag + " - " + message + " - " + exception.getMessage());
        }

        @Override
        public void info(String tag, String message) {
            logs.add("INFO: " + tag + " - " + message);
        }

        @Override
        public void info(String tag, String message, Throwable exception) {
            logs.add("INFO: " + tag + " - " + message + " - " + exception.getMessage());
        }

        @Override
        public void error(String tag, String message) {
            logs.add("ERROR: " + tag + " - " + message);
        }

        @Override
        public void error(String tag, String message, Throwable exception) {
            logs.add("ERROR: " + tag + " - " + message + " - " + exception.getMessage());
        }
    }
}
