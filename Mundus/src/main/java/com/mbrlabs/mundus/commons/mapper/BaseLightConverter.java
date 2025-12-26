package com.mbrlabs.mundus.commons.mapper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.mbrlabs.mundus.commons.dto.BaseLightDTO;

/**
 * The converter for lights.
 */
public class BaseLightConverter {

    /**
     * Converts {@link BaseLightDTO} to {@link Color}.
     */
    public static Color convert(BaseLightDTO dto) {
        if (dto == null) return null;
        return new Color(dto.getColor());
    }

    /**
     * Converts {@link ColorAttribute} to {@link BaseLightDTO}.
     */
    public static BaseLightDTO convert(ColorAttribute light) {
        if (light == null) return null;
        BaseLightDTO lightDescriptor = new BaseLightDTO();
        lightDescriptor.setColor(Color.rgba8888(light.color));

        return lightDescriptor;
    }
}
