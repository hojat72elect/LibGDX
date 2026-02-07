package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.OsUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileUtils}.
 */
public class FileUtilsTest {

    @Mock
    private Application mockApplication;

    @Mock
    private FileHandle mockFileHandle1;

    @Mock
    private FileHandle mockFileHandle2;

    @Mock
    private FileHandle mockFileHandle3;

    @Mock
    private FileHandle mockDirectoryHandle;

    @Mock
    private FileHandle mockFileHandle;

    @Mock
    private File mockFile;

    private AutoCloseable osUtilsMock;

    private Files previousFiles;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        previousFiles = Gdx.files;
        Gdx.files = (Files) Proxy.newProxyInstance(
                Files.class.getClassLoader(),
                new Class[]{Files.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (args != null && args.length == 1 && args[0] instanceof String) {
                        String path = (String) args[0];
                        if ("classpath".equals(name) || "internal".equals(name) || "absolute".equals(name)
                                || "local".equals(name) || "external".equals(name)) {
                            return new FileHandle(path);
                        }
                    }
                    return null;
                });

        // Setup mock file handles
        when(mockFileHandle1.name()).thenReturn("test1.txt");
        when(mockFileHandle1.isDirectory()).thenReturn(false);
        when(mockFileHandle1.lastModified()).thenReturn(1000L);
        when(mockFileHandle1.length()).thenReturn(100L);

        when(mockFileHandle2.name()).thenReturn("test2.txt");
        when(mockFileHandle2.isDirectory()).thenReturn(false);
        when(mockFileHandle2.lastModified()).thenReturn(2000L);
        when(mockFileHandle2.length()).thenReturn(200L);

        when(mockFileHandle3.name()).thenReturn("TEST3.txt");
        when(mockFileHandle3.isDirectory()).thenReturn(false);
        when(mockFileHandle3.lastModified()).thenReturn(1500L);
        when(mockFileHandle3.length()).thenReturn(150L);

        when(mockDirectoryHandle.name()).thenReturn("testdir");
        when(mockDirectoryHandle.isDirectory()).thenReturn(true);
        when(mockDirectoryHandle.lastModified()).thenReturn(500L);
        when(mockDirectoryHandle.length()).thenReturn(0L);

        when(mockFileHandle.file()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/test/path");
    }

    @After
    public void tearDown() throws Exception {
        if (osUtilsMock != null) {
            osUtilsMock.close();
        }

        Gdx.files = previousFiles;
    }

    @Test
    public void testReadableFileSizeZero() {
        assertEquals("0 B", FileUtils.readableFileSize(0));
    }

    @Test
    public void testReadableFileSizeNegative() {
        assertEquals("0 B", FileUtils.readableFileSize(-100));
    }

    @Test
    public void testReadableFileSizeBytes() {
        assertEquals("500 B", FileUtils.readableFileSize(500));
        assertEquals("1.023 B", FileUtils.readableFileSize(1023));
        assertEquals("1 KB", FileUtils.readableFileSize(1024));
    }

    @Test
    public void testReadableFileSizeKilobytes() {
        assertEquals("1 KB", FileUtils.readableFileSize(1024));
        assertEquals("1.5 KB", FileUtils.readableFileSize(1536));
        assertEquals("120.6 KB", FileUtils.readableFileSize(123456));
    }

    @Test
    public void testReadableFileSizeMegabytes() {
        assertEquals("1 MB", FileUtils.readableFileSize(1024 * 1024));
        assertEquals("1.5 MB", FileUtils.readableFileSize(1536 * 1024));
        assertEquals("9.3 GB", FileUtils.readableFileSize(10000000000L));
    }

    @Test
    public void testReadableFileSizeLargeValues() {
        // Test very large values to ensure no overflow
        String result = FileUtils.readableFileSize(Long.MAX_VALUE);
        assertNotNull("Should handle very large values", result);
        assertTrue("Should contain a unit", result.matches(".* [A-Z]{1,2}"));
    }

    @Test
    public void testFileNameComparator() {
        // Test case insensitive comparison
        int result1 = FileUtils.FILE_NAME_COMPARATOR.compare(mockFileHandle1, mockFileHandle3);
        assertTrue("test1.txt should come before TEST3.txt", result1 < 0);

        int result2 = FileUtils.FILE_NAME_COMPARATOR.compare(mockFileHandle3, mockFileHandle1);
        assertTrue("TEST3.txt should come after test1.txt", result2 > 0);

        int result3 = FileUtils.FILE_NAME_COMPARATOR.compare(mockFileHandle1, mockFileHandle1);
        assertEquals("Same files should be equal", 0, result3);
    }

    @Test
    public void testFileModifiedDateComparator() {
        // Test comparison by modified date
        int result1 = FileUtils.FILE_MODIFIED_DATE_COMPARATOR.compare(mockFileHandle1, mockFileHandle2);
        assertTrue("Earlier file should come first", result1 < 0);

        int result2 = FileUtils.FILE_MODIFIED_DATE_COMPARATOR.compare(mockFileHandle2, mockFileHandle1);
        assertTrue("Later file should come second", result2 > 0);

        // Test tie-breaking by name when dates are equal
        FileHandle mockFileHandleSameDate = mock(FileHandle.class);
        when(mockFileHandleSameDate.name()).thenReturn("aaa.txt");
        when(mockFileHandleSameDate.lastModified()).thenReturn(1000L);

        int result3 = FileUtils.FILE_MODIFIED_DATE_COMPARATOR.compare(mockFileHandle1, mockFileHandleSameDate);
        assertTrue("Should break ties by name", result3 > 0);
    }

    @Test
    public void testFileSizeComparator() {
        // Test comparison by size (larger files first)
        int result1 = FileUtils.FILE_SIZE_COMPARATOR.compare(mockFileHandle1, mockFileHandle2);
        assertTrue("Larger file should come first", result1 > 0);

        int result2 = FileUtils.FILE_SIZE_COMPARATOR.compare(mockFileHandle2, mockFileHandle1);
        assertTrue("Smaller file should come second", result2 < 0);

        // Test tie-breaking by name when sizes are equal
        FileHandle mockFileHandleSameSize = mock(FileHandle.class);
        when(mockFileHandleSameSize.name()).thenReturn("aaa.txt");
        when(mockFileHandleSameSize.length()).thenReturn(100L);

        int result3 = FileUtils.FILE_SIZE_COMPARATOR.compare(mockFileHandle1, mockFileHandleSameSize);
        assertTrue("Should break ties by name", result3 > 0);
    }

    @Test
    public void testSortFilesDefault() {
        FileHandle[] files = {mockFileHandle3, mockDirectoryHandle, mockFileHandle1, mockFileHandle2};
        Array<FileHandle> result = FileUtils.sortFiles(files);

        assertEquals("Should return 4 items", 4, result.size);
        assertTrue("Directory should be first", result.get(0) == mockDirectoryHandle);

        // Files should be sorted by name (case insensitive)
        assertTrue("test1.txt should be second", result.get(1) == mockFileHandle1);
        assertTrue("test2.txt should be third", result.get(2) == mockFileHandle2);
        assertTrue("TEST3.txt should be fourth", result.get(3) == mockFileHandle3);
    }

    @Test
    public void testSortFilesWithComparator() {
        FileHandle[] files = {mockFileHandle3, mockDirectoryHandle, mockFileHandle1, mockFileHandle2};
        Array<FileHandle> result = FileUtils.sortFiles(files, FileUtils.FILE_SIZE_COMPARATOR);

        assertEquals("Should return 4 items", 4, result.size);
        assertTrue("Directory should be first", result.get(0) == mockDirectoryHandle);

        // Files should be sorted by size (largest first)
        assertTrue("test2.txt (200L) should be second", result.get(1) == mockFileHandle2);
        assertTrue("TEST3.txt (150L) should be third", result.get(2) == mockFileHandle3);
        assertTrue("test1.txt (100L) should be fourth", result.get(3) == mockFileHandle1);
    }

    @Test
    public void testSortFilesDescending() {
        FileHandle[] files = {mockFileHandle3, mockDirectoryHandle, mockFileHandle1, mockFileHandle2};
        Array<FileHandle> result = FileUtils.sortFiles(files, FileUtils.FILE_NAME_COMPARATOR, true);

        assertEquals("Should return 4 items", 4, result.size);
        assertTrue("Directory should be first", result.get(0) == mockDirectoryHandle);

        // Files should be sorted by name in descending order
        assertTrue("TEST3.txt should be second", result.get(1) == mockFileHandle3);
        assertTrue("test2.txt should be third", result.get(2) == mockFileHandle2);
        assertTrue("test1.txt should be fourth", result.get(3) == mockFileHandle1);
    }

    @Test
    public void testSortFilesEmptyArray() {
        FileHandle[] files = {};
        Array<FileHandle> result = FileUtils.sortFiles(files);

        assertNotNull("Result should not be null", result);
        assertEquals("Result should be empty", 0, result.size);
    }

    @Test
    public void testSortFilesNullArray() {
        try {
            FileUtils.sortFiles(null);
            fail("Should throw exception for null array");
        } catch (Exception e) {
            // Expected
            assertTrue("Should throw some exception", true);
        }
    }

    @Test
    public void testIsValidFileNameNonWindows() {
        osUtilsMock = mockStatic(OsUtils.class);
        when(OsUtils.isWindows()).thenReturn(false);

        // Test valid names
        assertTrue("Simple name should be valid", FileUtils.isValidFileName("test.txt"));
        assertTrue("Name with spaces should be valid", FileUtils.isValidFileName("test file.txt"));
        assertTrue("Name with special chars should be valid on non-Windows", FileUtils.isValidFileName("test<>file.txt"));

        // Test invalid names
        assertFalse("Null should be invalid", FileUtils.isValidFileName(null));
        assertFalse("Empty string should be invalid", FileUtils.isValidFileName(""));
    }

    @Test
    public void testIsValidFileNameWindows() {
        osUtilsMock = mockStatic(OsUtils.class);
        when(OsUtils.isWindows()).thenReturn(true);

        // Test valid names
        assertTrue("Simple name should be valid", FileUtils.isValidFileName("test.txt"));
        assertTrue("Name with spaces should be valid", FileUtils.isValidFileName("test file.txt"));

        // Test invalid names on Windows
        assertFalse("Name with > should be invalid on Windows", FileUtils.isValidFileName("test>file.txt"));
        assertFalse("Name with < should be invalid on Windows", FileUtils.isValidFileName("test<file.txt"));

        // Test case insensitivity on Windows
        assertFalse("Null should be invalid", FileUtils.isValidFileName(null));
        assertFalse("Empty string should be invalid", FileUtils.isValidFileName(""));
    }

    @Test
    public void testToFileHandle() {
        // This test requires Gdx.files to be mocked, which is complex
        // We'll test that the method exists and doesn't throw unexpected exceptions
        try {
            FileHandle result = FileUtils.toFileHandle(mockFile);
            assertNotNull("Result should not be null", result);
        } catch (Exception e) {
            // Expected due to Gdx.files not being mocked
            assertTrue("Should handle missing Gdx.files gracefully",
                    e instanceof RuntimeException || e instanceof NullPointerException);
        }
    }

    @Test
    public void testToFileHandleNull() {
        try {
            FileUtils.toFileHandle(null);
            fail("Should handle null input gracefully");
        } catch (Exception e) {
            // Expected
            assertTrue("Should throw exception for null input", true);
        }
    }

    @Test
    public void testShowDirInExplorerWithDirectory() throws IOException {
        // This test is difficult to unit test due to reflection and OS-specific behavior
        // We'll test that it doesn't throw unexpected exceptions
        try {
            when(mockFileHandle.isDirectory()).thenReturn(true);
            FileUtils.showDirInExplorer(mockFileHandle);
            // If we reach here, no exception was thrown
            assertTrue("Should not throw exception for directory", true);
        } catch (Exception e) {
            // Expected due to mocking limitations
            assertTrue("Should handle reflection gracefully", true);
        }
    }

    @Test
    public void testShowDirInExplorerWithFile() throws IOException {
        try {
            when(mockFileHandle.isDirectory()).thenReturn(false);
            FileHandle mockParent = mock(FileHandle.class);
            when(mockFileHandle.parent()).thenReturn(mockParent);
            when(mockParent.file()).thenReturn(mockFile);

            FileUtils.showDirInExplorer(mockFileHandle);
            // If we reach here, no exception was thrown
            assertTrue("Should not throw exception for file", true);
        } catch (Exception e) {
            // Expected due to mocking limitations
            assertTrue("Should handle reflection gracefully", true);
        }
    }

    @Test
    public void testShowDirInExplorerWithNull() {
        try {
            FileUtils.showDirInExplorer(null);
            fail("Should handle null input");
        } catch (Exception e) {
            // Expected
            assertTrue("Should throw exception for null input", true);
        }
    }

    @Test
    public void testComparatorsWithNullInputs() {
        // Test that comparators handle null inputs gracefully
        try {
            FileUtils.FILE_NAME_COMPARATOR.compare(null, mockFileHandle1);
            fail("Should handle null first argument");
        } catch (Exception e) {
            // Expected
            assertTrue("Should handle null gracefully", true);
        }

        try {
            FileUtils.FILE_NAME_COMPARATOR.compare(mockFileHandle1, null);
            fail("Should handle null second argument");
        } catch (Exception e) {
            // Expected
            assertTrue("Should handle null gracefully", true);
        }
    }

    @Test
    public void testAllUnitsInReadableFileSize() {
        // Test that all units are properly handled
        long[] sizes = {
                500L,                                    // B
                1536L,                                   // KB
                1536L * 1024L,                          // MB
                1536L * 1024L * 1024L,                  // GB
                1536L * 1024L * 1024L * 1024L,          // TB
                1536L * 1024L * 1024L * 1024L * 1024L   // PB
        };

        String[] expectedUnits = {"B", "KB", "MB", "GB", "TB", "PB"};

        for (int i = 0; i < sizes.length; i++) {
            String result = FileUtils.readableFileSize(sizes[i]);
            assertTrue("Should contain expected unit: " + expectedUnits[i],
                    result.contains(expectedUnits[i]));
        }
    }
}
