package com.mbrlabs.mundus.commons.mapper;

import com.badlogic.gdx.utils.OrderedMap;
import com.mbrlabs.mundus.commons.dto.CustomPropertiesComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.CustomPropertiesComponent;

public class CustomPropertiesComponentConverter {

    public static CustomPropertiesComponent convert(final CustomPropertiesComponentDTO dto, final GameObject go) {
        final CustomPropertiesComponent component = new CustomPropertiesComponent(go);
        final OrderedMap<String, String> dtoMap = dto.getCustomProperties();

        for (final OrderedMap.Entry<String, String> entry : dtoMap.iterator()) {
            component.put(entry.key, entry.value);
        }

        return component;
    }

    public static CustomPropertiesComponentDTO convert(final CustomPropertiesComponent component) {
        final CustomPropertiesComponentDTO dto = new CustomPropertiesComponentDTO();
        dto.setCustomProperties(component.getMap());

        return dto;
    }
}
