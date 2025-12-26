package com.mbrlabs.mundus.commons.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mbrlabs.mundus.commons.assets.Asset;

import java.util.Map;

/**
 * Utils for assets.
 */
public class AssetUtils {

    public static ObjectMap<String, Asset> getAssetsById(final Array<String> assetIds, final Map<String, Asset> assetMap) {
        final ObjectMap<String, Asset> retMap = new ObjectMap<>();
        if (assetIds != null) {
            for (int i = 0; i < assetIds.size; ++i) {
                final String assetId = assetIds.get(i);
                if (assetId != null) {
                    final Asset asset = assetMap.get(assetId);

                    if (asset != null) {
                        retMap.put(assetId, asset);
                    }
                }
            }
        }

        return retMap;
    }
}
