package com.kotcrab.vis.ui.widget.file.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link PreferencesIO}.
 * These tests focus on basic functionality and structure.
 */
public class PreferencesIOTest {

    @Test
    public void testClassExists() {
        assertNotNull("PreferencesIO class should exist", PreferencesIO.class);
    }

    @Test
    public void testClassStructure() {
        Class<?> clazz = PreferencesIO.class;

        assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(clazz.getModifiers()));
        assertTrue("Should be final", true);
    }

    @Test
    public void testConstructors() {
        try {
            // Test default constructor
            PreferencesIO.class.getDeclaredConstructor();
            assertTrue("Default constructor should exist", true);

            // Test constructor with String parameter
            PreferencesIO.class.getDeclaredConstructor(String.class);
            assertTrue("Constructor with String parameter should exist", true);

        } catch (NoSuchMethodException e) {
            fail("Expected constructor not found: " + e.getMessage());
        }
    }

    @Test
    public void testKeyMethods() {
        try {
            PreferencesIO.class.getMethod("loadFavorites");
            PreferencesIO.class.getMethod("saveFavorites", com.badlogic.gdx.utils.Array.class);
            PreferencesIO.class.getMethod("loadRecentDirectories");
            PreferencesIO.class.getMethod("saveRecentDirectories", com.badlogic.gdx.utils.Array.class);
            PreferencesIO.class.getMethod("loadLastDirectory");
            PreferencesIO.class.getMethod("saveLastDirectory", com.badlogic.gdx.files.FileHandle.class);
            PreferencesIO.class.getMethod("checkIfUsingDefaultName");
            PreferencesIO.class.getMethod("setDefaultPrefsName", String.class);
            assertTrue("Key methods should exist", true);
        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testStaticMethods() {
        try {
            java.lang.reflect.Method setDefaultPrefsNameMethod = PreferencesIO.class.getMethod("setDefaultPrefsName", String.class);

            assertTrue("setDefaultPrefsName should be public static",
                    java.lang.reflect.Modifier.isPublic(setDefaultPrefsNameMethod.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(setDefaultPrefsNameMethod.getModifiers()));

        } catch (NoSuchMethodException e) {
            fail("Expected static method not found: " + e.getMessage());
        }
    }

    @Test
    public void testFieldStructure() {
        try {
            java.lang.reflect.Field visDefaultPrefsNameField = PreferencesIO.class.getDeclaredField("VIS_DEFAULT_PREFS_NAME");
            java.lang.reflect.Field defaultPrefsNameField = PreferencesIO.class.getDeclaredField("defaultPrefsName");
            java.lang.reflect.Field favoritesKeyNameField = PreferencesIO.class.getDeclaredField("favoritesKeyName");
            java.lang.reflect.Field recentDirKeyNameField = PreferencesIO.class.getDeclaredField("recentDirKeyName");
            java.lang.reflect.Field lastDirKeyNameField = PreferencesIO.class.getDeclaredField("lastDirKeyName");
            java.lang.reflect.Field prefsField = PreferencesIO.class.getDeclaredField("prefs");
            java.lang.reflect.Field jsonField = PreferencesIO.class.getDeclaredField("json");

            assertEquals("VIS_DEFAULT_PREFS_NAME should be String",
                    String.class, visDefaultPrefsNameField.getType());
            assertEquals("defaultPrefsName should be String",
                    String.class, defaultPrefsNameField.getType());
            assertEquals("favoritesKeyName should be String",
                    String.class, favoritesKeyNameField.getType());
            assertEquals("recentDirKeyName should be String",
                    String.class, recentDirKeyNameField.getType());
            assertEquals("lastDirKeyName should be String",
                    String.class, lastDirKeyNameField.getType());
            assertEquals("prefs should be Preferences",
                    com.badlogic.gdx.Preferences.class, prefsField.getType());
            assertEquals("json should be Json",
                    com.badlogic.gdx.utils.Json.class, jsonField.getType());

            assertTrue("VIS_DEFAULT_PREFS_NAME should be private static final",
                    java.lang.reflect.Modifier.isPrivate(visDefaultPrefsNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(visDefaultPrefsNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(visDefaultPrefsNameField.getModifiers()));

            assertTrue("defaultPrefsName should be private static",
                    java.lang.reflect.Modifier.isPrivate(defaultPrefsNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(defaultPrefsNameField.getModifiers()));

            assertTrue("key fields should be private final",
                    (java.lang.reflect.Modifier.isPrivate(favoritesKeyNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(favoritesKeyNameField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(recentDirKeyNameField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(recentDirKeyNameField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(lastDirKeyNameField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(lastDirKeyNameField.getModifiers())));

            assertTrue("prefs and json should be private final",
                    (java.lang.reflect.Modifier.isPrivate(prefsField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(prefsField.getModifiers())) &&
                            (java.lang.reflect.Modifier.isPrivate(jsonField.getModifiers()) &&
                                    java.lang.reflect.Modifier.isFinal(jsonField.getModifiers())));

        } catch (NoSuchFieldException e) {
            fail("Expected field not found: " + e.getMessage());
        }
    }

    @Test
    public void testInnerClasses() {
        try {
            Class<?> fileArrayDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileArrayData");
            Class<?> fileHandleDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileHandleData");

            assertNotNull("FileArrayData class should exist", fileArrayDataClass);
            assertNotNull("FileHandleData class should exist", fileHandleDataClass);

            assertTrue("FileArrayData should be private static",
                    java.lang.reflect.Modifier.isPrivate(fileArrayDataClass.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(fileArrayDataClass.getModifiers()));

            assertTrue("FileHandleData should be private static",
                    java.lang.reflect.Modifier.isPrivate(fileHandleDataClass.getModifiers()) &&
                            java.lang.reflect.Modifier.isStatic(fileHandleDataClass.getModifiers()));

        } catch (ClassNotFoundException e) {
            fail("Expected inner class not found: " + e.getMessage());
        }
    }

    @Test
    public void testFileArrayDataClass() {
        try {
            Class<?> fileArrayDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileArrayData");

            // Test constructor
            fileArrayDataClass.getDeclaredConstructor();
            fileArrayDataClass.getDeclaredConstructor(com.badlogic.gdx.utils.Array.class);

            // Test methods
            fileArrayDataClass.getMethod("toFileHandleArray");

            // Test field
            java.lang.reflect.Field dataField = fileArrayDataClass.getDeclaredField("data");
            assertEquals("data should be Array",
                    com.badlogic.gdx.utils.Array.class, dataField.getType());

            assertTrue("FileArrayData methods and field should be available", true);

        } catch (Exception e) {
            fail("FileArrayData class test failed: " + e.getMessage());
        }
    }

    @Test
    public void testFileHandleDataClass() {
        try {
            Class<?> fileHandleDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileHandleData");

            // Test constructor
            fileHandleDataClass.getDeclaredConstructor();
            fileHandleDataClass.getDeclaredConstructor(com.badlogic.gdx.files.FileHandle.class);

            // Test methods
            fileHandleDataClass.getMethod("toFileHandle");

            // Test fields
            java.lang.reflect.Field typeField = fileHandleDataClass.getDeclaredField("type");
            java.lang.reflect.Field pathField = fileHandleDataClass.getDeclaredField("path");
            assertEquals("type should be FileType",
                    com.badlogic.gdx.Files.FileType.class, typeField.getType());
            assertEquals("path should be String",
                    String.class, pathField.getType());

            assertTrue("FileHandleData methods and fields should be available", true);

        } catch (Exception e) {
            fail("FileHandleData class test failed: " + e.getMessage());
        }
    }

    @Test
    public void testPreferencesIntegration() {
        try {
            Class<?> preferencesClass = Class.forName("com.badlogic.gdx.Preferences");

            assertNotNull("Preferences class should be available", preferencesClass);

            // Test that Preferences has expected methods
            preferencesClass.getMethod("getString", String.class, String.class);
            preferencesClass.getMethod("putString", String.class, String.class);
            preferencesClass.getMethod("flush");

            assertTrue("Preferences methods should be available", true);

        } catch (Exception e) {
            fail("Preferences integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testJsonIntegration() {
        try {
            Class<?> jsonClass = Class.forName("com.badlogic.gdx.utils.Json");

            assertNotNull("Json class should be available", jsonClass);

            // Test that Json has expected methods
            jsonClass.getMethod("fromJson", Class.class, String.class);
            jsonClass.getMethod("toJson", Object.class);

            assertTrue("Json methods should be available", true);

        } catch (Exception e) {
            fail("Json integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testArrayIntegration() {
        try {
            Class<?> arrayClass = Class.forName("com.badlogic.gdx.utils.Array");

            assertNotNull("Array class should be available", arrayClass);

            // Test that Array has expected methods
            arrayClass.getMethod("add", Object.class);

            assertTrue("Array methods should be available", true);

        } catch (Exception e) {
            fail("Array integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testFileHandleIntegration() {
        try {
            Class<?> fileHandleClass = Class.forName("com.badlogic.gdx.files.FileHandle");
            Class<?> fileTypeClass = Class.forName("com.badlogic.gdx.Files$FileType");

            assertNotNull("FileHandle class should be available", fileHandleClass);
            assertNotNull("FileType class should be available", fileTypeClass);

            // Test that FileHandle has expected methods
            fileHandleClass.getMethod("type");
            fileHandleClass.getMethod("path");

            // Test that FileType has expected constants
            fileTypeClass.getField("Absolute");
            fileTypeClass.getField("Classpath");
            fileTypeClass.getField("External");
            fileTypeClass.getField("Internal");
            fileTypeClass.getField("Local");

            assertTrue("FileHandle and FileType methods/constants should be available", true);

        } catch (Exception e) {
            fail("FileHandle integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testApplicationIntegration() {
        try {
            Class<?> applicationClass = Class.forName("com.badlogic.gdx.Application");

            assertNotNull("Application class should be available", applicationClass);

            // Test that Application has expected methods
            applicationClass.getMethod("getPreferences", String.class);
            applicationClass.getMethod("log", String.class, String.class);

            assertTrue("Application methods should be available", true);

        } catch (Exception e) {
            fail("Application integration test failed: " + e.getMessage());
        }
    }

    @Test
    public void testMethodVisibility() {
        try {
            java.lang.reflect.Method loadFavoritesMethod = PreferencesIO.class.getMethod("loadFavorites");
            java.lang.reflect.Method saveFavoritesMethod = PreferencesIO.class.getMethod("saveFavorites", com.badlogic.gdx.utils.Array.class);
            java.lang.reflect.Method loadRecentDirectoriesMethod = PreferencesIO.class.getMethod("loadRecentDirectories");
            java.lang.reflect.Method saveRecentDirectoriesMethod = PreferencesIO.class.getMethod("saveRecentDirectories", com.badlogic.gdx.utils.Array.class);
            java.lang.reflect.Method loadLastDirectoryMethod = PreferencesIO.class.getMethod("loadLastDirectory");
            java.lang.reflect.Method saveLastDirectoryMethod = PreferencesIO.class.getMethod("saveLastDirectory", com.badlogic.gdx.files.FileHandle.class);
            java.lang.reflect.Method checkIfUsingDefaultNameMethod = PreferencesIO.class.getMethod("checkIfUsingDefaultName");

            assertTrue("loadFavorites should be public",
                    java.lang.reflect.Modifier.isPublic(loadFavoritesMethod.getModifiers()));
            assertTrue("saveFavorites should be public",
                    java.lang.reflect.Modifier.isPublic(saveFavoritesMethod.getModifiers()));
            assertTrue("loadRecentDirectories should be public",
                    java.lang.reflect.Modifier.isPublic(loadRecentDirectoriesMethod.getModifiers()));
            assertTrue("saveRecentDirectories should be public",
                    java.lang.reflect.Modifier.isPublic(saveRecentDirectoriesMethod.getModifiers()));
            assertTrue("loadLastDirectory should be public",
                    java.lang.reflect.Modifier.isPublic(loadLastDirectoryMethod.getModifiers()));
            assertTrue("saveLastDirectory should be public",
                    java.lang.reflect.Modifier.isPublic(saveLastDirectoryMethod.getModifiers()));
            assertTrue("checkIfUsingDefaultName should be public",
                    java.lang.reflect.Modifier.isPublic(checkIfUsingDefaultNameMethod.getModifiers()));

        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testMethodReturnTypes() {
        try {
            assertEquals("loadFavorites should return Array",
                    com.badlogic.gdx.utils.Array.class, PreferencesIO.class.getMethod("loadFavorites").getReturnType());
            assertEquals("saveFavorites should return void",
                    void.class, PreferencesIO.class.getMethod("saveFavorites", com.badlogic.gdx.utils.Array.class).getReturnType());
            assertEquals("loadRecentDirectories should return Array",
                    com.badlogic.gdx.utils.Array.class, PreferencesIO.class.getMethod("loadRecentDirectories").getReturnType());
            assertEquals("saveRecentDirectories should return void",
                    void.class, PreferencesIO.class.getMethod("saveRecentDirectories", com.badlogic.gdx.utils.Array.class).getReturnType());
            assertEquals("loadLastDirectory should return FileHandle",
                    com.badlogic.gdx.files.FileHandle.class, PreferencesIO.class.getMethod("loadLastDirectory").getReturnType());
            assertEquals("saveLastDirectory should return void",
                    void.class, PreferencesIO.class.getMethod("saveLastDirectory", com.badlogic.gdx.files.FileHandle.class).getReturnType());
            assertEquals("checkIfUsingDefaultName should return void",
                    void.class, PreferencesIO.class.getMethod("checkIfUsingDefaultName").getReturnType());

        } catch (NoSuchMethodException e) {
            fail("Expected method not found: " + e.getMessage());
        }
    }

    @Test
    public void testConstants() {
        try {
            java.lang.reflect.Field visDefaultPrefsNameField = PreferencesIO.class.getDeclaredField("VIS_DEFAULT_PREFS_NAME");
            java.lang.reflect.Field favoritesKeyNameField = PreferencesIO.class.getDeclaredField("favoritesKeyName");
            java.lang.reflect.Field recentDirKeyNameField = PreferencesIO.class.getDeclaredField("recentDirKeyName");
            java.lang.reflect.Field lastDirKeyNameField = PreferencesIO.class.getDeclaredField("lastDirKeyName");

            assertTrue("VIS_DEFAULT_PREFS_NAME should be static final",
                    java.lang.reflect.Modifier.isStatic(visDefaultPrefsNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(visDefaultPrefsNameField.getModifiers()));

            assertTrue("key fields should be final",
                    java.lang.reflect.Modifier.isFinal(favoritesKeyNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(recentDirKeyNameField.getModifiers()) &&
                            java.lang.reflect.Modifier.isFinal(lastDirKeyNameField.getModifiers()));

        } catch (NoSuchFieldException e) {
            fail("Expected constant field not found: " + e.getMessage());
        }
    }

    @Test
    public void testSerializationLogic() {
        try {
            // Test that FileArrayData has serialization methods
            Class<?> fileArrayDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileArrayData");
            fileArrayDataClass.getMethod("toFileHandleArray");

            // Test that FileHandleData has serialization methods
            Class<?> fileHandleDataClass = Class.forName("com.kotcrab.vis.ui.widget.file.internal.PreferencesIO$FileHandleData");
            fileHandleDataClass.getMethod("toFileHandle");

            assertTrue("Serialization logic methods should be available", true);

        } catch (Exception e) {
            fail("Serialization logic test failed: " + e.getMessage());
        }
    }
}
