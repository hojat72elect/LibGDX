package com.mbrlabs.mundus.commons.mapper;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;

public interface CustomComponentConverter {

    /**
     * @return The component type of custom component.
     */
    Component.Type getComponentType();

    /**
     * Converts component into map for persisting.
     *
     * @param component The component.
     * @return The map.
     */
    OrderedMap<String, String> convert(Component component);

    /**
     * @param component The component.
     * @return The ID of used assets.
     */
    default Array<String> getAssetIds(Component component) {
        return null;
    }

    /**
     * Converts map into custom component.
     *
     * @param gameObject          The game object.
     * @param componentProperties The properties of custom component.
     * @param assets              The assets of component.
     * @return The custom component.
     */
    Component convert(GameObject gameObject, OrderedMap<String, String> componentProperties, ObjectMap<String, Asset> assets);
}
