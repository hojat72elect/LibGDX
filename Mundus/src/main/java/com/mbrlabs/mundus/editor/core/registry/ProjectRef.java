package com.mbrlabs.mundus.editor.core.registry;

/**
 * A reference to a Mundus project, stored in the registry.
 */
public class ProjectRef {

    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
