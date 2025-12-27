package com.mbrlabs.mundus.runtime.converter;

import com.mbrlabs.mundus.commons.assets.AssetManager;
import com.mbrlabs.mundus.commons.assets.WaterAsset;
import com.mbrlabs.mundus.commons.dto.WaterComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.WaterComponent;
import com.mbrlabs.mundus.runtime.Shaders;

/**
 * The converter for Water.
 */
public class WaterComponentConverter {

    private final static String TAG = WaterComponentConverter.class.getSimpleName();

    /**
     * Converts {@link WaterComponentDTO} to {@link WaterComponent}.
     */
    public static WaterComponent convert(WaterComponentDTO waterComponentDTO, GameObject gameObject,
                                         Shaders shaders, AssetManager assetManager) {
        WaterComponent wc = new WaterComponent(gameObject, null);
        wc.setWaterAsset((WaterAsset) assetManager.findAssetByID(waterComponentDTO.getWaterId()));
        gameObject.hasWaterComponent = true;

        return wc;
    }


}
