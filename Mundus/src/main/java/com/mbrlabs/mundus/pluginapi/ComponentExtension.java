package com.mbrlabs.mundus.pluginapi;

import com.badlogic.gdx.utils.Array;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.pluginapi.ui.RootWidget;
import org.pf4j.ExtensionPoint;

public interface ComponentExtension extends ExtensionPoint {

    /**
     * Returns with the supported component types.
     * If the game object contains a supported component type then the component can be added via plugin.
     * The default value is null, in this case the Editor doesn't check component types.
     *
     * @return Null or array of supported component types.
     */
    default Array<Component.Type> getSupportedComponentTypes() {
        return null;
    }

    /**
     * @return The component type what the plugin use.
     */
    Component.Type getComponentType();

    /**
     * @return The component name.
     */
    String getComponentName();

    /**
     * @param gameObject The game object.
     * @return The created component for given game object.
     */
    Component createComponent(GameObject gameObject);

    /**
     * Setups widget for custom component for Inspector.
     *
     * @param component The component.
     * @param rootWidget The root widget.
     */
    void setupComponentInspectorWidget(Component component, RootWidget rootWidget);

    /**
     * @return The converter for load and save properties for custom component.
     */
    CustomComponentConverter getConverter();
}
