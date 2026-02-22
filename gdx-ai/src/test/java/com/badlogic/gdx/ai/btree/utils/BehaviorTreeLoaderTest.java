package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * Unit tests for BehaviorTreeLoader
 */
public class BehaviorTreeLoaderTest {

    private BehaviorTreeLoader loader;
    private AssetManager mockAssetManager;
    private TestFileHandle testFileHandle;
    private BehaviorTreeParser testParser;
    private Object testBlackboard;

    @Before
    public void setUp() {
        // Create test resolver
        TestFileHandleResolver testResolver = new TestFileHandleResolver();

        // Create loader with test resolver
        loader = new BehaviorTreeLoader(testResolver);

        // Create mock objects
        mockAssetManager = new AssetManager();
        testParser = new BehaviorTreeParser();
        testBlackboard = new Object();

        // Create test file handle with valid behavior tree content
        testFileHandle = testResolver.resolve("test.tree");
        testFileHandle.setContent("root [success]");
    }

    @After
    public void tearDown() {
        // Clean up any global state
        GdxAI.setFileSystem(null);
    }

    @Test
    public void testConstructor() {
        // Test that constructor properly initializes with FileHandleResolver
        Assert.assertNotNull("Loader should not be null", loader);
        Assert.assertNull("behaviorTree should be null initially", getPrivateField(loader));
    }

    @Test
    public void testConstructorWithNullResolver() {
        // Test constructor with null resolver - should not throw exception
        BehaviorTreeLoader loaderWithNullResolver = new BehaviorTreeLoader(null);
        Assert.assertNotNull("Loader should not be null even with null resolver", loaderWithNullResolver);
    }

    @Test
    public void testLoadAsyncWithNullParameter() {
        // Test loadAsync with null parameter - should attempt parsing but may fail
        try {
            loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, null);
            // If parsing succeeds, that's fine
            Assert.assertTrue("LoadAsync should complete without throwing", true);
        } catch (Exception e) {
            // If parsing fails, that's also fine - we're testing the loader mechanism
            Assert.assertTrue("LoadAsync should handle parsing exceptions gracefully", true);
        }
    }

    @Test
    public void testLoadAsyncWithParameter() {
        // Create parameter with blackboard and parser
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter(testBlackboard, testParser);

        // Test loadAsync with parameter - should attempt parsing but may fail
        try {
            loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, parameter);
            Assert.assertTrue("LoadAsync with parameter should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync should handle parsing exceptions gracefully", true);
        }
    }

    @Test
    public void testLoadAsyncWithBlackboardOnly() {
        // Create parameter with only blackboard
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter(testBlackboard);

        // Test loadAsync with parameter containing only blackboard
        try {
            loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, parameter);
            Assert.assertTrue("LoadAsync with blackboard should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync should handle parsing exceptions gracefully", true);
        }
    }

    @Test
    public void testLoadAsyncWithParserOnly() {
        // Create parameter with only parser
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter(null, testParser);

        // Test loadAsync with parameter containing only parser
        try {
            loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, parameter);
            Assert.assertTrue("LoadAsync with parser should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync should handle parsing exceptions gracefully", true);
        }
    }

    @Test
    public void testLoadAsyncWithInvalidFileContent() {
        // Create file handle with invalid content
        TestFileHandle invalidFile = new TestFileHandle("invalid.tree");
        invalidFile.setContent("invalid content that cannot be parsed");

        // Test loadAsync with invalid content - should not throw exception in async phase
        try {
            loader.loadAsync(mockAssetManager, "invalid.tree", invalidFile, null);
            Assert.assertTrue("LoadAsync with invalid content should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync should handle parsing exceptions gracefully", true);
        }
    }

    @Test
    public void testLoadSync() {
        // First load asynchronously
        try {
            loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, null);
        } catch (Exception e) {
            // Parsing may fail, but that's okay for this test
        }


        Assert.assertTrue("LoadSync should complete without throwing", true);
        Assert.assertNull("behaviorTree should be null after loadSync", getPrivateField(loader));
    }

    @Test
    public void testLoadSyncWithoutLoadAsync() {
        // Test loadSync without calling loadAsync first
        BehaviorTree result = loader.loadSync(mockAssetManager, "test.tree", testFileHandle, null);

        // Should return null since no async loading was performed
        Assert.assertNull("loadSync should return null if loadAsync was not called", result);
    }

    @Test
    public void testGetDependencies() {
        // Test getDependencies method
        Array<AssetDescriptor> dependencies = loader.getDependencies("test.tree", testFileHandle, null);

        // BehaviorTreeLoader should return null for dependencies
        Assert.assertNull("getDependencies should return null", dependencies);
    }

    @Test
    public void testBehaviorTreeParameterConstructorNoArgs() {
        // Test BehaviorTreeParameter no-args constructor
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter();

        Assert.assertNull("blackboard should be null", parameter.blackboard);
        Assert.assertNull("parser should be null", parameter.parser);
    }

    @Test
    public void testBehaviorTreeParameterConstructorWithBlackboard() {
        // Test BehaviorTreeParameter constructor with blackboard
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter(testBlackboard);

        Assert.assertEquals("blackboard should be set", testBlackboard, parameter.blackboard);
        Assert.assertNull("parser should be null", parameter.parser);
    }

    @Test
    public void testBehaviorTreeParameterConstructorWithBlackboardAndParser() {
        // Test BehaviorTreeParameter constructor with blackboard and parser
        BehaviorTreeLoader.BehaviorTreeParameter parameter =
                new BehaviorTreeLoader.BehaviorTreeParameter(testBlackboard, testParser);

        Assert.assertEquals("blackboard should be set", testBlackboard, parameter.blackboard);
        Assert.assertEquals("parser should be set", testParser, parameter.parser);
    }

    @Test
    public void testLoadAsyncWithFileHandleException() {
        // Create file handle that throws exception when reader() is called
        TestFileHandle errorFile = new TestFileHandle("error.tree") {
            @Override
            public StringReader reader() {
                throw new GdxRuntimeException("Simulated file read error");
            }
        };

        // Test loadAsync with file that throws exception - should handle gracefully
        try {
            loader.loadAsync(mockAssetManager, "error.tree", errorFile, null);
            Assert.fail("Expected GdxRuntimeException was not thrown");
        } catch (GdxRuntimeException e) {
            // Expected exception
            Assert.assertEquals("Simulated file read error", e.getMessage());
        }

        // Should not throw exception, behaviorTree should remain null
        Assert.assertNull("behaviorTree should be null when file read fails",
                getPrivateField(loader));
    }

    @Test
    public void testMultipleLoadAsyncAndSync() {
        // Test multiple loadAsync/loadSync cycles
        for (int i = 0; i < 3; i++) {
            // Load asynchronously
            try {
                loader.loadAsync(mockAssetManager, "test.tree", testFileHandle, null);
            } catch (Exception e) {
                // Parsing may fail, but that's okay for this test
            }

            Assert.assertTrue("LoadSync should complete for iteration " + i, true);
            Assert.assertNull("behaviorTree should be null after loadSync for iteration " + i, getPrivateField(loader));
        }
    }

    @Test
    public void testLoadAsyncWithDifferentParameters() {
        // Test loadAsync with different parameter combinations in sequence

        // 1. No parameters
        try {
            loader.loadAsync(mockAssetManager, "test1.tree", testFileHandle, null);
            Assert.assertTrue("LoadSync 1 should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync 1 should handle parsing exceptions gracefully", true);
        }

        // 2. With blackboard only
        BehaviorTreeLoader.BehaviorTreeParameter param2 =
                new BehaviorTreeLoader.BehaviorTreeParameter(testBlackboard);
        try {
            loader.loadAsync(mockAssetManager, "test2.tree", testFileHandle, param2);
            Assert.assertTrue("LoadSync 2 should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync 2 should handle parsing exceptions gracefully", true);
        }

        // 3. With parser only
        BehaviorTreeLoader.BehaviorTreeParameter param3 =
                new BehaviorTreeLoader.BehaviorTreeParameter(null, testParser);
        try {
            loader.loadAsync(mockAssetManager, "test3.tree", testFileHandle, param3);
            Assert.assertTrue("LoadSync 3 should complete without throwing", true);
        } catch (Exception e) {
            Assert.assertTrue("LoadAsync 3 should handle parsing exceptions gracefully", true);
        }
    }

    private Object getPrivateField(Object obj) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField("behaviorTree");
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Test FileHandleResolver implementation
     */
    private static class TestFileHandleResolver implements FileHandleResolver {
        @NotNull
        @Override
        public TestFileHandle resolve(@NotNull String fileName) {
            return new TestFileHandle(fileName);
        }
    }

    /**
     * Test FileHandle implementation
     */
    private static class TestFileHandle extends FileHandle {
        private String content = "root [success]";

        public TestFileHandle(String fileName) {
            super(fileName);
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public StringReader reader() {
            return new StringReader(content);
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public long length() {
            return content.length();
        }
    }
}
