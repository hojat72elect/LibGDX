package com.mbrlabs.mundus.commons.assets.meta;

import com.badlogic.gdx.utils.ObjectMap;

public class MetaCustom {

    public static final String JSON_PROPERTIES = "properties";

    private final ObjectMap<String, String> properties = new ObjectMap<>();

    public ObjectMap<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "MetaCustom{" +
                "properties=" + properties +
                '}';
    }
}
