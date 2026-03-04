package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

public class PathSmootherTest {

    private TestRaycastCollisionDetector<Vector2> collisionDetector2D;
    private TestRaycastCollisionDetector<Vector3> collisionDetector3D;
    private PathSmoother<String, Vector2> pathSmoother2D;
    private PathSmoother<String, Vector3> pathSmoother3D;
    private TestSmoothableGraphPath<String, Vector2> testPath2D;
    private TestSmoothableGraphPath<String, Vector3> testPath3D;

    @Before
    public void setUp() {
        collisionDetector2D = new TestRaycastCollisionDetector<>();
        collisionDetector3D = new TestRaycastCollisionDetector<>();
        pathSmoother2D = new PathSmoother<>(collisionDetector2D);
        pathSmoother3D = new PathSmoother<>(collisionDetector3D);
        testPath2D = new TestSmoothableGraphPath<>();
        testPath3D = new TestSmoothableGraphPath<>();
    }

    @Test
    public void testConstructor() {
        PathSmoother<String, Vector2> smoother = new PathSmoother<>(collisionDetector2D);
        assertNotNull("PathSmoother should be created", smoother);
        assertNotNull("RaycastCollisionDetector should be set", collisionDetector2D);
    }

    @Test
    public void testConstructorWithNullCollisionDetector() {
        PathSmoother<String, Vector2> smoother = new PathSmoother<>(null);
        assertNotNull("PathSmoother should be created even with null collision detector", smoother);
    }

    @Test
    public void testSmoothPathEmptyPath() {
        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Empty path should remove 0 nodes", 0, removedNodes);
        assertEquals("Path should remain empty", 0, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathSingleNode() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Single node path should remove 0 nodes", 0, removedNodes);
        assertEquals("Path should remain with 1 node", 1, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathTwoNodes() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(10, 0));

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Two node path should remove 0 nodes", 0, removedNodes);
        assertEquals("Path should remain with 2 nodes", 2, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathThreeNodesNoCollisions() {
        // Create path: A -> B -> C where A->C has direct line of sight
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(5, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(10, 0));

        // No collisions for any ray cast
        collisionDetector2D.setCollides(false);

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Should remove 1 node (B)", 1, removedNodes);
        assertEquals("Path should have 2 nodes after smoothing", 2, testPath2D.getCount());
        assertEquals("First node should be A", "A", testPath2D.get(0));
        assertEquals("Second node should be C", "C", testPath2D.get(1));
    }

    @Test
    public void testSmoothPathThreeNodesWithCollision() {
        // Create path: A -> B -> C where A->C is blocked
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(5, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(10, 0));

        // A->C ray collides, so we need to keep B
        collisionDetector2D.setCollides(true);

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Should remove 0 nodes when collision detected", 0, removedNodes);
        assertEquals("Path should remain with 3 nodes", 3, testPath2D.getCount());
        assertEquals("Nodes should remain in order", "A", testPath2D.get(0));
        assertEquals("Nodes should remain in order", "B", testPath2D.get(1));
        assertEquals("Nodes should remain in order", "C", testPath2D.get(2));
    }

    @Test
    public void testSmoothPathMultipleNodesMixedCollisions() {
        // Create path: A -> B -> C -> D -> E
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(2, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(4, 0));
        testPath2D.addNode("D");
        testPath2D.addPosition(new Vector2(6, 0));
        testPath2D.addNode("E");
        testPath2D.addPosition(new Vector2(8, 0));

        // Set up collision pattern: A->C collides, A->D no collision, A->E no collision
        collisionDetector2D.setCollisionPattern(true, false, false);

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Should remove 2 nodes (C and D)", 2, removedNodes);
        assertEquals("Path should have 3 nodes after smoothing", 3, testPath2D.getCount());
        assertEquals("First node should be A", "A", testPath2D.get(0));
        assertEquals("Second node should be B", "B", testPath2D.get(1));
        assertEquals("Third node should be E", "E", testPath2D.get(2));
    }

    @Test
    public void testRayInitialization() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(1, 2));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(3, 4));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(5, 6));

        collisionDetector2D.setCollides(false);
        pathSmoother2D.smoothPath(testPath2D);

        Ray<Vector2> usedRay = collisionDetector2D.getLastRay();
        assertNotNull("Ray should be initialized", usedRay);
        assertNotNull("Ray start should be set", usedRay.start);
        assertNotNull("Ray end should be set", usedRay.end);
    }

    @Test
    public void testRayReuseAcrossMultipleSmooths() {
        // First smooth
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(1, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(2, 0));
        collisionDetector2D.setCollides(false);
        pathSmoother2D.smoothPath(testPath2D);

        Ray<Vector2> firstRay = collisionDetector2D.getLastRay();

        // Second smooth with different path
        testPath2D.clear();
        testPath2D.addNode("X");
        testPath2D.addPosition(new Vector2(10, 10));
        testPath2D.addNode("Y");
        testPath2D.addPosition(new Vector2(11, 10));
        testPath2D.addNode("Z");
        testPath2D.addPosition(new Vector2(12, 10));
        pathSmoother2D.smoothPath(testPath2D);

        Ray<Vector2> secondRay = collisionDetector2D.getLastRay();
        assertNotNull("Ray should be reused", secondRay);
        // The ray object should be the same instance (reused)
        assertSame("Ray should be reused across multiple smooths", firstRay, secondRay);
    }

    @Test
    public void testSmoothPath3D() {
        testPath3D.addNode("A");
        testPath3D.addPosition(new Vector3(0, 0, 0));
        testPath3D.addNode("B");
        testPath3D.addPosition(new Vector3(5, 0, 0));
        testPath3D.addNode("C");
        testPath3D.addPosition(new Vector3(10, 0, 0));

        collisionDetector3D.setCollides(false);

        int removedNodes = pathSmoother3D.smoothPath(testPath3D);
        assertEquals("Should remove 1 node in 3D path", 1, removedNodes);
        assertEquals("Path should have 2 nodes after smoothing", 2, testPath3D.getCount());
        assertEquals("First node should be A", "A", testPath3D.get(0));
        assertEquals("Second node should be C", "C", testPath3D.get(1));
    }

    // Interruptible Smoothing Tests

    @Test
    public void testSmoothPathWithRequestEmptyPath() {
        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        boolean completed = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Empty path should complete immediately", completed);
        assertEquals("Path should remain empty", 0, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathWithRequestSingleNode() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        boolean completed = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Single node path should complete immediately", completed);
        assertEquals("Path should remain with 1 node", 1, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathWithRequestTwoNodes() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(10, 0));

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        boolean completed = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Two node path should complete immediately", completed);
        assertEquals("Path should remain with 2 nodes", 2, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathWithRequestThreeNodesNoCollisions() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(5, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(10, 0));

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        collisionDetector2D.setCollides(false);

        boolean completed = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Should complete with sufficient time", completed);
        assertEquals("Should remove 1 node", 1, 3 - testPath2D.getCount()); // Original 3 - final count = removed
        assertEquals("Path should have 2 nodes", 2, testPath2D.getCount());
    }

    @Test
    public void testSmoothPathWithRequestInsufficientTime() {
        // Create a longer path to ensure we run out of time
        for (int i = 0; i < 10; i++) {
            testPath2D.addNode("N" + i);
            testPath2D.addPosition(new Vector2(i * 2, 0));
        }

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        collisionDetector2D.setCollides(false);

        // Give very little time (less than TIME_TOLERANCE)
        boolean completed = pathSmoother2D.smoothPath(request, 50);
        assertFalse("Should not complete with insufficient time", completed);
        assertFalse("Request should still be marked as not new", request.isNew);
    }

    @Test
    public void testSmoothPathWithRequestMultipleCalls() {
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(5, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(10, 0));

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        collisionDetector2D.setCollides(false);

        // First call with limited time
        boolean completed1 = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Should complete on first call", completed1);
        assertEquals("Path should be smoothed", 2, testPath2D.getCount());

        // Second call should complete immediately
        boolean completed2 = pathSmoother2D.smoothPath(request, 1000000);
        assertTrue("Should complete on second call", completed2);
    }

    @Test
    public void testSmoothPathWithRequestStatePersistence() {
        // Create path: A -> B -> C -> D -> E
        for (int i = 0; i < 5; i++) {
            testPath2D.addNode("N" + i);
            testPath2D.addPosition(new Vector2(i * 2, 0));
        }

        PathSmootherRequest<String, Vector2> request = new PathSmootherRequest<>();
        request.refresh(testPath2D);

        collisionDetector2D.setCollides(false);

        // First call - initialize state
        pathSmoother2D.smoothPath(request, 1000000);

        // Verify state after completion
        assertEquals("outputIndex should be final value", 1, request.outputIndex); // Should be 1 after smoothing 5 nodes to 2
        assertEquals("inputIndex should be final value", 5, request.inputIndex); // Should reach inputPathLength (5)
        assertFalse("isNew should be false after initialization", request.isNew);
    }

    // Edge Cases and Error Handling Tests

    @Test
    public void testSmoothPathWithNullCollisionDetector() {
        PathSmoother<String, Vector2> smoother = new PathSmoother<>(null);

        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(5, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(10, 0));

        // Should throw NullPointerException when trying to call collides on null detector
        try {
            smoother.smoothPath(testPath2D);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertTrue("Expected NullPointerException", true);
        }
    }

    @Test
    public void testSmoothPathLargeNumberOfNodes() {
        // Create a path with 100 nodes
        for (int i = 0; i < 100; i++) {
            testPath2D.addNode("N" + i);
            testPath2D.addPosition(new Vector2(i, 0));
        }

        collisionDetector2D.setCollides(false);

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Should remove 98 nodes (keep first and last)", 98, removedNodes);
        assertEquals("Path should have 2 nodes", 2, testPath2D.getCount());
        assertEquals("First node should be N0", "N0", testPath2D.get(0));
        assertEquals("Last node should be N99", "N99", testPath2D.get(1));
    }

    @Test
    public void testSmoothPathAllNodesCollide() {
        // Create path where all direct connections collide
        testPath2D.addNode("A");
        testPath2D.addPosition(new Vector2(0, 0));
        testPath2D.addNode("B");
        testPath2D.addPosition(new Vector2(1, 0));
        testPath2D.addNode("C");
        testPath2D.addPosition(new Vector2(2, 0));
        testPath2D.addNode("D");
        testPath2D.addPosition(new Vector2(3, 0));

        collisionDetector2D.setCollides(true);

        int removedNodes = pathSmoother2D.smoothPath(testPath2D);
        assertEquals("Should remove 0 nodes when all collide", 0, removedNodes);
        assertEquals("Path should remain unchanged", 4, testPath2D.getCount());
    }

    // Test Helper Classes

    /**
     * Test implementation of RaycastCollisionDetector that allows configurable collision behavior.
     */
    private static class TestRaycastCollisionDetector<T extends Vector<T>> implements RaycastCollisionDetector<T> {
        private boolean collides = false;
        private boolean[] collisionPattern;
        private int patternIndex = 0;
        private Ray<T> lastRay;

        public void setCollides(boolean collides) {
            this.collides = collides;
            this.collisionPattern = null;
            this.patternIndex = 0;
        }

        public void setCollisionPattern(boolean... pattern) {
            this.collisionPattern = pattern;
            this.patternIndex = 0;
        }

        public Ray<T> getLastRay() {
            return lastRay;
        }

        @Override
        public boolean collides(Ray<T> ray) {
            lastRay = ray;
            if (collisionPattern != null && patternIndex < collisionPattern.length) {
                return collisionPattern[patternIndex++];
            }
            return collides;
        }

        @Override
        public boolean findCollision(Collision<T> outputCollision, Ray<T> inputRay) {
            lastRay = inputRay;
            return collides;
        }
    }

    /**
     * Test implementation of SmoothableGraphPath for testing purposes.
     */
    private static class TestSmoothableGraphPath<N, V extends Vector<V>> implements SmoothableGraphPath<N, V> {
        private final Array<N> nodes = new Array<>();
        private final Array<V> positions = new Array<>();

        public void addNode(N node) {
            nodes.add(node);
        }

        public void addPosition(V position) {
            positions.add(position);
        }

        @Override
        public int getCount() {
            return nodes.size;
        }

        @Override
        public N get(int index) {
            return nodes.get(index);
        }

        @Override
        public void add(N node) {
            nodes.add(node);
        }

        @Override
        public void clear() {
            nodes.clear();
            positions.clear();
        }

        @Override
        public void reverse() {
            nodes.reverse();
            positions.reverse();
        }

        @Override
        public V getNodePosition(int index) {
            return positions.get(index);
        }

        @Override
        public void swapNodes(int index1, int index2) {
            nodes.swap(index1, index2);
            positions.swap(index1, index2);
        }

        @Override
        public void truncatePath(int newLength) {
            while (nodes.size > newLength) {
                nodes.pop();
                positions.pop();
            }
        }

        @NotNull
        @Override
        public java.util.Iterator<N> iterator() {
            return nodes.iterator();
        }
    }
}
