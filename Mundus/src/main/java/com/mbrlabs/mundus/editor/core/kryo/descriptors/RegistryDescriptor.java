package com.mbrlabs.mundus.editor.core.kryo.descriptors;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

import java.util.ArrayList;
import java.util.List;

public class RegistryDescriptor {

    @Tag(0)
    private SettingsDescriptor settingsDescriptor;
    @Tag(1)
    private List<ProjectRefDescriptor> projects;
    @Tag(2)
    private ProjectRefDescriptor lastProject = null;

    public RegistryDescriptor() {
        projects = new ArrayList<>();
        settingsDescriptor = new SettingsDescriptor();
    }

    public SettingsDescriptor getSettingsDescriptor() {
        return settingsDescriptor;
    }

    public void setSettingsDescriptor(SettingsDescriptor settingsDescriptor) {
        this.settingsDescriptor = settingsDescriptor;
    }

    public List<ProjectRefDescriptor> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectRefDescriptor> projects) {
        this.projects = projects;
    }

    public ProjectRefDescriptor getLastProject() {
        return lastProject;
    }

    public void setLastProject(ProjectRefDescriptor lastProject) {
        this.lastProject = lastProject;
    }
}
