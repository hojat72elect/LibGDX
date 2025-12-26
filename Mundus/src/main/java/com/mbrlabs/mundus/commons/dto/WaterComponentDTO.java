package com.mbrlabs.mundus.commons.dto;

import com.mbrlabs.mundus.commons.assets.Asset;

import java.util.Map;

public class WaterComponentDTO implements AssetUsageDTO {

    private String waterId;

    public String getWaterId() {
        return waterId;
    }

    public void setWaterId(String id) {
        this.waterId = id;
    }

    @Override
    public boolean usesAsset(Asset assetToCheck, Map<String, Asset> assetMap) {
        return assetToCheck.getID().equals(waterId);
    }
}
