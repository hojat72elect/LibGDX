package com.mbrlabs.mundus.commons.dto;

import com.badlogic.gdx.utils.OrderedMap;

public class CustomPropertiesComponentDTO {

    private OrderedMap<String, String> customProperties;

    public OrderedMap<String, String> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(final OrderedMap<String, String> customProperties) {
        this.customProperties = customProperties;
    }
}
