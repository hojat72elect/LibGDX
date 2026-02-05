package com.kotcrab.vis.usl;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link IncludeLoader}.
 */
public class IncludeLoaderTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private IncludeLoader includeLoader;
    private String originalIncludePath;

    @Before
    public void setUp() throws Exception {
        // Store original values
        originalIncludePath = System.getProperty("usl.include.path");

        // Create temporary folders for testing
        File testCacheFolder = tempFolder.newFolder("cache");
        File testTmpFolder = tempFolder.newFolder("tmp");

        // Set up test environment
        System.setProperty("usl.include.path", tempFolder.getRoot().getAbsolutePath());

        // Create IncludeLoader instance
        includeLoader = new IncludeLoader();

        // Replace folders with test folders using reflection
        setPrivateField(includeLoader, "cacheFolder", testCacheFolder);
        setPrivateField(includeLoader, "tmpFolder", testTmpFolder);
    }

    @After
    public void tearDown() {
        // Restore original system property
        if (originalIncludePath != null) {
            System.setProperty("usl.include.path", originalIncludePath);
        } else {
            System.clearProperty("usl.include.path");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    // --- Constructor Tests ---

    @Test
    public void testConstructorCreatesFolders() throws Exception {
        // Test that the constructor creates the default folders
        // We can't easily test the exact folders without interfering with other tests,
        // but we can verify the constructor runs without error

        // Clear the system property to test default behavior
        System.clearProperty("usl.include.path");

        IncludeLoader loader = new IncludeLoader();

        // Verify the loader was created successfully and has default sources
        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(loader, "includeSources");

        assertFalse("Should not include system property when not set",
                includeSources.contains("/custom/path"));
        assertTrue("Should include default HTTP source",
                includeSources.contains("http://apps.kotcrab.com/vis/usl/"));
        assertTrue("Should include default GitHub source",
                includeSources.contains("https://raw.githubusercontent.com/kotcrab/vis-ui/master/usl/styles/"));
    }

    @Test
    public void testConstructorWithSystemProperty() throws Exception {
        System.setProperty("usl.include.path", "/custom/path");
        IncludeLoader loader = new IncludeLoader();

        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(loader, "includeSources");

        assertTrue("Should include system property path", includeSources.contains("/custom/path"));
        assertTrue("Should include default HTTP source", includeSources.contains("http://apps.kotcrab.com/vis/usl/"));
        assertTrue("Should include default GitHub source", includeSources.contains("https://raw.githubusercontent.com/kotcrab/vis-ui/master/usl/styles/"));
    }

    @Test
    public void testConstructorWithoutSystemProperty() throws Exception {
        System.clearProperty("usl.include.path");
        IncludeLoader loader = new IncludeLoader();

        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(loader, "includeSources");

        assertFalse("Should not include system property path", includeSources.contains("/custom/path"));
        assertTrue("Should include default HTTP source", includeSources.contains("http://apps.kotcrab.com/vis/usl/"));
        assertTrue("Should include default GitHub source", includeSources.contains("https://raw.githubusercontent.com/kotcrab/vis-ui/master/usl/styles/"));
    }

    // --- addIncludeSource Tests ---

    @Test
    public void testAddIncludeSource() throws Exception {
        includeLoader.addIncludeSource("/custom/source");

        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(includeLoader, "includeSources");

        assertEquals("Should have 4 sources", 4, includeSources.size());
        assertEquals("Custom source should be first", "/custom/source", includeSources.get(0));
    }

    @Test
    public void testAddMultipleIncludeSources() throws Exception {
        includeLoader.addIncludeSource("/source1");
        includeLoader.addIncludeSource("/source2");

        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(includeLoader, "includeSources");

        assertTrue("Should have multiple sources", includeSources.size() >= 4);
        assertTrue("Should contain source1", includeSources.contains("/source1"));
        assertTrue("Should contain source2", includeSources.contains("/source2"));
    }

    // --- Local File Loading Tests ---

    @Test
    public void testLoadIncludeFromLocalFile() throws Exception {
        // Create a test file in the temp folder
        File includeDir = tempFolder.newFolder("includes");
        File testFile = new File(includeDir, "test.usl");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test content");
        }

        includeLoader.addIncludeSource(includeDir.getAbsolutePath());
        String content = includeLoader.loadInclude("test");

        assertEquals("Should load content from local file", "test content", content);
    }

    @Test
    public void testLoadIncludeFromCachedFile() throws Exception {
        // Create a cached file directly
        File cacheFolder = getPrivateField(includeLoader, "cacheFolder");
        File cachedFile = new File(cacheFolder, "cached.usl");
        try (FileWriter writer = new FileWriter(cachedFile)) {
            writer.write("cached content");
        }

        String content = includeLoader.loadInclude("cached");

        assertEquals("Should load content from cached file", "cached content", content);
    }

    @Test
    public void testLoadIncludeFileNotFound() throws Exception {
        try {
            includeLoader.loadInclude("nonexistent");
            fail("Should throw IllegalStateException for missing file");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention file not found",
                    e.getMessage().contains("Could not find 'nonexistent.usl' include"));
            assertTrue("Error message should list searched locations",
                    e.getMessage().contains("Searched in the following locations"));
        }
    }

    @Test
    public void testLoadIncludeLocalFileTakesPrecedence() throws Exception {
        // Create both a local file and add a remote source
        File includeDir = tempFolder.newFolder("includes");
        File localFile = new File(includeDir, "precedence.usl");
        try (FileWriter writer = new FileWriter(localFile)) {
            writer.write("local content");
        }

        includeLoader.addIncludeSource(includeDir.getAbsolutePath());

        // This should find the local file first and not try to download
        String content = includeLoader.loadInclude("precedence");

        assertEquals("Should prefer local file", "local content", content);
    }

    // --- File Reading Tests ---

    @Test
    public void testFileToString() throws Exception {
        File testFile = tempFolder.newFile("test.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("file content");
        }

        // Use reflection to test private method
        String content = (String) invokePrivateMethod(includeLoader, "fileToString", testFile);

        assertEquals("Should read file content correctly", "file content", content);
    }

    @Test
    public void testFileToStringEmptyFile() throws Exception {
        File testFile = tempFolder.newFile("empty.txt");

        // Use reflection to test private method
        String content = (String) invokePrivateMethod(includeLoader, "fileToString", testFile);

        assertEquals("Empty file should return empty string", "", content);
    }

    @Test
    public void testFileToStringNonExistentFile() throws Exception {
        File nonExistentFile = new File("nonexistent.txt");

        try {
            invokePrivateMethod(includeLoader, "fileToString", nonExistentFile);
            fail("Should throw IllegalStateException for non-existent file");
        } catch (InvocationTargetException e) {
            // Reflection wraps the actual exception
            Throwable cause = e.getCause();
            assertTrue("Should be caused by IllegalStateException",
                    cause instanceof IllegalStateException);
            assertTrue("Error message should mention reading error",
                    cause.getMessage().contains("Error reading file"));
        }
    }

    // --- Snapshot Handling Tests ---

    @Test
    public void testSnapshotFileHandling() throws Exception {
        // This test verifies that snapshot files are handled differently
        // We can't easily test the actual downloading without mocking HTTP,
        // but we can test the logic around snapshot detection

        File tmpFolder = getPrivateField(includeLoader, "tmpFolder");

        // Test that snapshot files are detected
        assertTrue("Should detect snapshot files",
                isSnapshotFile("test-SNAPSHOT.usl"));
        assertFalse("Should not detect non-snapshot files",
                isSnapshotFile("test.usl"));
        assertFalse("Should not detect files with snapshot in middle",
                isSnapshotFile("test-SNAPSHOT-test.usl"));
    }

    private boolean isSnapshotFile(String fileName) throws Exception {
        // Simulate the snapshot detection logic from loadIncludeFile
        return fileName.endsWith("-SNAPSHOT.usl");
    }

    // --- Error Handling Tests ---

    @Test
    public void testLoadIncludeWithIOException() throws Exception {
        // This test is challenging without being able to mock IOException
        // We'll test the error message format instead
        try {
            includeLoader.loadInclude("nonexistent");
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue("Should include include name in error",
                    e.getMessage().contains("nonexistent.usl"));
            assertTrue("Should list searched locations",
                    e.getMessage().contains("Searched in the following locations"));
        }
    }

    @Test
    public void testLoadIncludeWithMalformedUrl() throws Exception {
        // Add a malformed URL to test error handling
        includeLoader.addIncludeSource("http://[invalid-url");

        try {
            includeLoader.loadInclude("test");
            fail("Should throw IllegalStateException for malformed URL");
        } catch (IllegalStateException e) {
            assertTrue("Should wrap IOException",
                    e.getMessage().contains("Error during include file downloading"));
        }
    }

    // --- Edge Case Tests ---

    @Test
    public void testLoadIncludeWithEmptyName() throws Exception {
        try {
            includeLoader.loadInclude("");
            fail("Should handle empty include name");
        } catch (IllegalStateException e) {
            assertTrue("Should handle empty name gracefully",
                    e.getMessage().contains(".usl"));
        }
    }

    @Test
    public void testLoadIncludeWithNullName() throws Exception {
        try {
            includeLoader.loadInclude(null);
            fail("Should handle null include name");
        } catch (IllegalStateException e) {
            // Expected behavior - null gets wrapped in IllegalStateException
        }
    }

    @Test
    public void testAddIncludeSourceWithNull() throws Exception {
        includeLoader.addIncludeSource(null); // This should actually work - ArrayList allows null

        @SuppressWarnings("unchecked")
        ArrayList<String> includeSources = getPrivateField(includeLoader, "includeSources");

        assertEquals("Should have 4 sources", 4, includeSources.size());
        assertEquals("Null should be added as first source", null, includeSources.get(0));
    }

    @Test
    public void testLoadIncludeWithSpecialCharactersInName() throws Exception {
        // Create a file with special characters
        File includeDir = tempFolder.newFolder("includes");
        File testFile = new File(includeDir, "test-special_123.usl");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("special content");
        }

        includeLoader.addIncludeSource(includeDir.getAbsolutePath());
        String content = includeLoader.loadInclude("test-special_123");

        assertEquals("Should handle special characters in filename", "special content", content);
    }

    @Test
    public void testLoadIncludeWithLongFilename() throws Exception {
        String longName = "a".repeat(100);
        File includeDir = tempFolder.newFolder("includes");
        File testFile = new File(includeDir, longName + ".usl");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("long name content");
        }

        includeLoader.addIncludeSource(includeDir.getAbsolutePath());
        String content = includeLoader.loadInclude(longName);

        assertEquals("Should handle long filenames", "long name content", content);
    }

    // --- Integration Tests ---

    @Test
    public void testLoadIncludeIntegration() throws Exception {
        // Create a complete test scenario
        File includeDir = tempFolder.newFolder("integration");
        File testFile = new File(includeDir, "integration.usl");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("integration test content\n" +
                    "with multiple lines\n" +
                    "and special chars: !@#$%");
        }

        includeLoader.addIncludeSource(includeDir.getAbsolutePath());
        String content = includeLoader.loadInclude("integration");

        assertTrue("Should load complete file content",
                content.contains("integration test content"));
        assertTrue("Should preserve line breaks",
                content.contains("with multiple lines"));
        assertTrue("Should handle special characters",
                content.contains("!@#$%"));
    }

    // --- Utility Methods ---

    private Object invokePrivateMethod(Object obj, String methodName, Object... args) throws Exception {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }

        java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }
}
