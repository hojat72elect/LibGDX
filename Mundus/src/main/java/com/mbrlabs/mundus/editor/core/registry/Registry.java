package com.mbrlabs.mundus.editor.core.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Manages global settings.
 * <p>
 * Files are stored in ~/.mundus/
 */
public class Registry {

    public static final String HOME_DIR = FilenameUtils.concat(FileUtils.getUserDirectoryPath(), ".mundus/");
    public static final String LOGS_DIR = FilenameUtils.concat(HOME_DIR, "logs/");
    public static final String TEMP_DIR = FilenameUtils.concat(HOME_DIR, "temp/");
    public static final String PLUGINS_DIR = FilenameUtils.concat(HOME_DIR, "plugins/");
    public static final String HOME_DATA_FILE = FilenameUtils.concat(HOME_DIR, "mundus.registry");

    private final List<ProjectRef> projects;
    private ProjectRef lastProject;
    private Settings settings;

    public Registry() {
        projects = new ArrayList<>();
        settings = new Settings();
    }

    public FileHandle createTempFolder() {
        String tempFolderPath = FilenameUtils.concat(TEMP_DIR, UUID.randomUUID().toString()) + "/";
        FileHandle tempFolder = Gdx.files.absolute(tempFolderPath);
        tempFolder.mkdirs();

        return tempFolder;
    }

    public void purgeTempDirectory() {
        for (FileHandle f : Gdx.files.absolute(TEMP_DIR).list()) {
            f.deleteDirectory();
        }
    }

    public ProjectRef createProjectRef(String name, String folder) {
        ProjectRef projectRef = new ProjectRef();
        projectRef.setName(name);
        projectRef.setPath(FilenameUtils.concat(folder, name));
        projects.add(projectRef);

        return projectRef;
    }

    public List<ProjectRef> getProjects() {
        return projects;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ProjectRef getLastOpenedProject() {
        return lastProject;
    }

    public ProjectRef getLastProject() {
        return lastProject;
    }

    public void setLastProject(ProjectRef lastProject) {
        this.lastProject = lastProject;
    }
}
