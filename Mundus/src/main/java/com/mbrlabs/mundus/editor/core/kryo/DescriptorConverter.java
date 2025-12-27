package com.mbrlabs.mundus.editor.core.kryo;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonWriter;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.ProjectDescriptor;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.ProjectRefDescriptor;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.ProjectSettingsDescriptor;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.RegistryDescriptor;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.SceneRefDescriptor;
import com.mbrlabs.mundus.editor.core.kryo.descriptors.SettingsDescriptor;
import com.mbrlabs.mundus.editor.core.project.ProjectContext;
import com.mbrlabs.mundus.editor.core.project.ProjectSettings;
import com.mbrlabs.mundus.editor.core.registry.KeyboardLayout;
import com.mbrlabs.mundus.editor.core.registry.ProjectRef;
import com.mbrlabs.mundus.editor.core.registry.Registry;
import com.mbrlabs.mundus.editor.core.registry.Settings;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Converts runtime formats into Kryo compatible formats for internal project
 * persistence.
 */
public class DescriptorConverter {

    private final static String TAG = DescriptorConverter.class.getSimpleName();

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Registry

    /// //////////////////////////////////////////////////////////////////////////////////////////////////

    public static RegistryDescriptor convert(Registry registry) {
        RegistryDescriptor descriptor = new RegistryDescriptor();

        descriptor.setLastProject(convert(registry.getLastOpenedProject()));
        for (ProjectRef projectRef : registry.getProjects()) {
            descriptor.getProjects().add(convert(projectRef));
        }
        descriptor.setSettingsDescriptor(convert(registry.getSettings()));

        return descriptor;
    }

    public static Registry convert(RegistryDescriptor descriptor) {
        boolean lastOpenedProjectDeleted = false;
        Registry registry = new Registry();

        registry.setLastProject(convert(descriptor.getLastProject()));
        for (ProjectRefDescriptor projectRef : descriptor.getProjects()) {

            // If the project files were deleted, do not convert
            boolean directoryExists = Files.isDirectory(Paths.get(projectRef.getPath()));

            if (directoryExists) {
                registry.getProjects().add(convert(projectRef));
            } else if (projectRef.getPath().equals(descriptor.getLastProject().getPath())) {
                // Uh oh, the last project opened no longer exists, lets set a different one as last opened
                lastOpenedProjectDeleted = true;
            }
        }
        registry.setSettings(convert(descriptor.getSettingsDescriptor()));

        if (lastOpenedProjectDeleted) {
            if (!registry.getProjects().isEmpty()) {
                // Open the last project in the list
                registry.setLastProject(registry.getProjects().get(registry.getProjects().size() - 1));
            }
        }

        return registry;
    }

    private static ProjectRef convert(ProjectRefDescriptor descriptor) {
        ProjectRef project = new ProjectRef();
        project.setName(descriptor.getName());
        project.setPath(descriptor.getPath());

        return project;
    }

    private static ProjectRefDescriptor convert(ProjectRef project) {
        ProjectRefDescriptor descriptor = new ProjectRefDescriptor();
        descriptor.setPath(project.getPath());
        descriptor.setName(project.getName());

        return descriptor;
    }

    private static Settings convert(SettingsDescriptor descriptor) {
        Settings settings = new Settings();
        settings.setFbxConvBinary(descriptor.getFbxConvBinary());
        settings.setKeyboardLayout(descriptor.getKeyboardLayout());

        if (settings.getKeyboardLayout() == null) {
            if (Locale.getDefault().equals(Locale.GERMAN) || Locale.getDefault().equals(Locale.GERMANY)) {
                settings.setKeyboardLayout(KeyboardLayout.QWERTZ);
            } else {
                settings.setKeyboardLayout(KeyboardLayout.QWERTY);
            }
        }

        return settings;
    }

    private static SettingsDescriptor convert(Settings settings) {
        SettingsDescriptor descriptor = new SettingsDescriptor();
        descriptor.setKeyboardLayout(settings.getKeyboardLayout());
        descriptor.setFbxConvBinary(settings.getFbxConvBinary());

        return descriptor;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Project

    /// //////////////////////////////////////////////////////////////////////////////////////////////////

    public static ProjectDescriptor convert(ProjectContext project) {
        ProjectDescriptor descriptor = new ProjectDescriptor();
        descriptor.setName(project.name);
        descriptor.setCurrentSceneName(project.currScene.getName());
        descriptor.setNextAvailableID(project.inspectCurrentID());
        descriptor.setSettings(convert(project.settings));

        // scenes
        for (String sceneName : project.scenes) {
            descriptor.getSceneRefDescriptor().add(new SceneRefDescriptor(sceneName));
        }

        return descriptor;
    }

    public static ProjectContext convert(ProjectDescriptor projectDescriptor) {
        ProjectContext context = new ProjectContext(projectDescriptor.getNextAvailableID());
        context.name = projectDescriptor.getName();

        // project settings
        context.settings = convert(projectDescriptor.getSettings());

        // scenes
        for (SceneRefDescriptor sceneRefDescriptor : projectDescriptor.getSceneRefDescriptor()) {
            context.scenes.add(sceneRefDescriptor.getName());
        }

        return context;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Project Settings

    /// //////////////////////////////////////////////////////////////////////////////////////////////////

    public static ProjectSettingsDescriptor convert(ProjectSettings settings) {
        ProjectSettingsDescriptor descriptor = new ProjectSettingsDescriptor();

        // export settings
        descriptor.setExportAllAssets(settings.getExport().allAssets);
        descriptor.setExportCompressScenes(settings.getExport().compressScenes);
        if (settings.getExport().outputFolder != null) {
            descriptor.setExportOutputFolder(settings.getExport().outputFolder.path());
        }
        descriptor.setJsonType(settings.getExport().jsonType.toString());

        return descriptor;
    }

    public static ProjectSettings convert(ProjectSettingsDescriptor descriptor) {
        ProjectSettings settings = new ProjectSettings();
        if (descriptor == null) return settings;

        // export settings
        settings.getExport().allAssets = descriptor.isExportAllAssets();
        settings.getExport().compressScenes = descriptor.isExportCompressScenes();
        if (descriptor.getExportOutputFolder() != null && descriptor.getExportOutputFolder().length() > 0) {
            settings.getExport().outputFolder = new FileHandle(descriptor.getExportOutputFolder());
        }
        settings.getExport().jsonType = JsonWriter.OutputType.valueOf(descriptor.getJsonType());

        return settings;
    }
}
