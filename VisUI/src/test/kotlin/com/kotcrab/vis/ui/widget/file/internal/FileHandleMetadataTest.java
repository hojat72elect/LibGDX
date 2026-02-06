package com.kotcrab.vis.ui.widget.file.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link FileHandleMetadata}.
 * These tests focus on basic functionality and structure.
 */
public class FileHandleMetadataTest {

    @Test
    public void testClassExists() {
        assertNotNull("FileHandleMetadata class should exist", FileHandleMetadata.class);
    }

    @Test
    public void testConstructor() {
        try {
            FileHandleMetadata.class.getDeclaredConstructor(com.badlogic.gdx.files.FileHandle.class);
            assertTrue("Private constructor with FileHandle parameter should exist", true);
        } catch (NoSuchMethodException e) {
            fail("Expected constructor not found: " + e.getMessage());
        }
    }

    @Test
    public void testStaticFactoryMethod() {
        try {
            java.lang.reflect.Method ofMethod = FileHandleMetadata.class.getMethod("of", com.badlogic.gdx.files.FileHandle.class);

            assertTrue("of method should be public static",
                    java.lang.reflect.Modifier.isPublic(ofMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(ofMethod.getModifiers()));

            assertEquals("of method should return FileHandleMetadata",
                    FileHandleMetadata.class, ofMethod.getReturnType());

        } catch (NoSuchMethodException e) {
            fail("of method not found: " + e.getMessage());
        }
    }

    @Test
    public void testInstanceMethods() {
        try {
            FileHandleMetadata.class.getMethod("name");
            FileHandleMetadata.class.getMethod("isDirectory");
            FileHandleMetadata.class.getMethod("lastModified");
            FileHandleMetadata.class.getMethod("length");
            FileHandleMetadata.class.getMethod("readableFileSize");
            assertTrue("Instance methods should exist", true);
        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testFieldStructure() {
        try {
            java.lang.reflect.Field nameField = FileHandleMetadata.class.getDeclaredField("name");
            java.lang.reflect.Field directoryField = FileHandleMetadata.class.getDeclaredField("directory");
            java.lang.reflect.Field lastModifiedField = FileHandleMetadata.class.getDeclaredField("lastModified");
            java.lang.reflect.Field lengthField = FileHandleMetadata.class.getDeclaredField("length");
            java.lang.reflect.Field readableFileSizeField = FileHandleMetadata.class.getDeclaredField("readableFileSize");

            assertEquals("name should be String",
                    String.class, nameField.getType());
            assertEquals("directory should be boolean",
                    boolean.class, directoryField.getType());
            assertEquals("lastModified should be long",
                    long.class, lastModifiedField.getType());
            assertEquals("length should be long",
                    long.class, lengthField.getType());
            assertEquals("readableFileSize should be String",
                    String.class, readableFileSizeField.getType());

            assertTrue("All fields should be private final",
                    (java.lang.reflect.Modifier.isPrivate(nameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(nameField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(directoryField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(directoryField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(lastModifiedField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(lastModifiedField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(lengthField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(lengthField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(readableFileSizeField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(readableFileSizeField.getModifiers())));

        } catch (NoSuchFieldException e) {
            fail("Expected field not found: " + e.getMessage());
        }
    }

    @Test
    public void testMethodReturnTypes() {
        try {
            assertEquals("name method should return String",
                    String.class, FileHandleMetadata.class.getMethod("name").getReturnType());
            assertEquals("isDirectory method should return boolean",
                    boolean.class, FileHandleMetadata.class.getMethod("isDirectory").getReturnType());
            assertEquals("lastModified method should return long",
                    long.class, FileHandleMetadata.class.getMethod("lastModified").getReturnType());
            assertEquals("length method should return long",
                    long.class, FileHandleMetadata.class.getMethod("length").getReturnType());
            assertEquals("readableFileSize method should return String",
                    String.class, FileHandleMetadata.class.getMethod("readableFileSize").getReturnType());

        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testMethodVisibility() {
        try {
            java.lang.reflect.Method nameMethod = FileHandleMetadata.class.getMethod("name");
            java.lang.reflect.Method isDirectoryMethod = FileHandleMetadata.class.getMethod("isDirectory");
            java.lang.reflect.Method lastModifiedMethod = FileHandleMetadata.class.getMethod("lastModified");
            java.lang.reflect.Method lengthMethod = FileHandleMetadata.class.getMethod("length");
            java.lang.reflect.Method readableFileSizeMethod = FileHandleMetadata.class.getMethod("readableFileSize");

            assertTrue("All methods should be public",
                    java.lang.reflect.Modifier.isPublic(nameMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isPublic(isDirectoryMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isPublic(lastModifiedMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isPublic(lengthMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isPublic(readableFileSizeMethod.getModifiers()));

        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testFileHandleIntegration() {
        try {
            Class<?> fileHandleClass = Class.forName("com.badlogic.gdx.files.FileHandle");

            assertNotNull("FileHandle class should be available", fileHandleClass);

            // Test that FileHandle has expected methods
            fileHandleClass.getMethod("name");
            fileHandleClass.getMethod("isDirectory");
            fileHandleClass.getMethod("lastModified");
            fileHandleClass.getMethod("length");

            assertTrue("FileHandle methods should be available", true);

        } catch (Exception e) {
            fail("FileHandle integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testFileUtilsIntegration() {
        try {
            Class<?> fileUtilsClass = Class.forName("com.kotcrab.vis.ui.widget.file.FileUtils");

            assertNotNull("FileUtils class should be available", fileUtilsClass);

            // Test that FileUtils has expected methods
            fileUtilsClass.getMethod("readableFileSize", long.class);

            assertTrue("FileUtils methods should be available", true);

        } catch (Exception e) {
            fail("FileUtils integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testImmutableDesign() {
        try {
            // Test that all fields are final (immutable design)
            java.lang.reflect.Field[] fields = FileHandleMetadata.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                assertTrue("Field " + field.getName() + " should be final",
                        java.lang.reflect.Modifier.isFinal(field.getModifiers()));
            }

            // Test that there are no setter methods
            java.lang.reflect.Method[] methods = FileHandleMetadata.class.getDeclaredMethods();
            for (java.lang.reflect.Method method : methods) {
                if (method.getName().startsWith("set")) {
                    fail("Class should not have setter methods for immutability");
                }
            }

            assertTrue("Class should be immutable", true);

        } catch (Exception e) {
            fail("Immutability test failed: " + e.getMessage());
        }
    }

    @Test
    public void testFactoryMethodPattern() {
        try {
            // Test that constructor is private
            java.lang.reflect.Constructor<?> constructor = FileHandleMetadata.class.getDeclaredConstructor(com.badlogic.gdx.files.FileHandle.class);
            assertTrue("Constructor should be private",
                    java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));

            // Test that factory method is the only way to create instances
            java.lang.reflect.Method ofMethod = FileHandleMetadata.class.getMethod("of", com.badlogic.gdx.files.FileHandle.class);
            assertTrue("Factory method should be public static",
                    java.lang.reflect.Modifier.isPublic(ofMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(ofMethod.getModifiers()));

            assertTrue("Factory method pattern should be implemented", true);

        } catch (Exception e) {
            fail("Factory method pattern test failed: " + e.getMessage());
        }
    }

    @Test
    public void testRecordLikeBehavior() {
        try {
            // Test that the class behaves like a record (immutable data carrier)
            java.lang.reflect.Method[] methods = FileHandleMetadata.class.getDeclaredMethods();

            // Should have exactly 5 accessor methods (name, isDirectory, lastModified, length, readableFileSize)
            // plus 1 factory method (of)
            int accessorCount = 0;
            for (java.lang.reflect.Method method : methods) {
                if (!method.getName().equals("of") &&
                        (method.getName().startsWith("get") || method.getName().startsWith("is") ||
                                method.getParameterCount() == 0)) {
                    accessorCount++;
                }
            }

            assertTrue("Class should have record-like accessor methods", accessorCount >= 5);

        } catch (Exception e) {
            fail("Record-like behavior test failed: " + e.getMessage());
        }
    }
}
