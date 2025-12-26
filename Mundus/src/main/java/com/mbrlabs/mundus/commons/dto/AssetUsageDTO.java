package com.mbrlabs.mundus.commons.dto;

import com.mbrlabs.mundus.commons.assets.Asset;

import java.util.Map;

/**
 * Returns True if the DTO object uses the assetToCheck.
 */
public interface AssetUsageDTO {
    boolean usesAsset(Asset assetToCheck, Map<String, Asset> assetMap);
}
