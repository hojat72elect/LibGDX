package com.mbrlabs.mundus.commons.dto;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.mbrlabs.mundus.commons.assets.Asset;

import java.util.Map;

public class CustomComponentDTO implements AssetUsageDTO {

    private String componentType;

    private OrderedMap<String, String> properties;

    private Array<String> assetIds;

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(final String componentType) {
        this.componentType = componentType;
    }

    public OrderedMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final OrderedMap<String, String> properties) {
        this.properties = properties;
    }

    public Array<String> getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(final Array<String> assetIds) {
        this.assetIds = assetIds;
    }

    @Override
    public boolean usesAsset(final Asset assetToCheck, final Map<String, Asset> assetMap) {
        if (assetIds == null) {
            return false;
        }

        for (int i = 0; i < assetIds.size; ++i) {
            if (assetToCheck.getID().equals(assetIds.get(i))) {
                return true;
            }
        }

        return false;
    }
}
