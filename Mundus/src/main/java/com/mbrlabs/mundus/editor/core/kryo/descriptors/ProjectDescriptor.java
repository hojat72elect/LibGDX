package com.mbrlabs.mundus.editor.core.kryo.descriptors;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

import java.util.ArrayList;
import java.util.List;

public class ProjectDescriptor {

    @Tag(0)
    private String name;
    @Tag(1)
    private int nextAvailableID;
    @Tag(2)
    private List<String> sceneNames;
    @Tag(3)
    private String currentSceneName;
    @Tag(4)
    private ProjectSettingsDescriptor settings;
    @Tag(5)
    private List<SceneRefDescriptor> sceneRefDescriptor;

    public ProjectDescriptor() {
        sceneNames = new ArrayList<>();
        sceneRefDescriptor = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextAvailableID() {
        return nextAvailableID;
    }

    public void setNextAvailableID(int nextAvailableID) {
        this.nextAvailableID = nextAvailableID;
    }

    public List<String> getSceneNames() {
        return sceneNames;
    }

    public void setSceneNames(List<String> sceneNames) {
        this.sceneNames = sceneNames;
    }

    public String getCurrentSceneName() {
        return currentSceneName;
    }

    public void setCurrentSceneName(String currentSceneName) {
        this.currentSceneName = currentSceneName;
    }

    public ProjectSettingsDescriptor getSettings() {
        return settings;
    }

    public void setSettings(ProjectSettingsDescriptor settings) {
        this.settings = settings;
    }

    public List<SceneRefDescriptor> getSceneRefDescriptor() {
        return sceneRefDescriptor;
    }

    public void setSceneRefDescriptor(List<SceneRefDescriptor> sceneRefDescriptor) {
        this.sceneRefDescriptor = sceneRefDescriptor;
    }
}
