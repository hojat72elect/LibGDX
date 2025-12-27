package com.mbrlabs.mundus.runtime.converter;

import com.mbrlabs.mundus.commons.assets.AssetManager;
import com.mbrlabs.mundus.commons.assets.MaterialAsset;
import com.mbrlabs.mundus.commons.assets.ModelAsset;
import com.mbrlabs.mundus.commons.dto.ModelComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;

import java.util.Map;

/**
 * The converter for model component.
 */
public class ModelComponentConverter {

    /**
     * Converts {@link ModelComponentDTO} to {@link ModelComponent}.
     */
    public static ModelComponent convert(ModelComponentDTO modelComponentDTO, GameObject gameObject, AssetManager assetManager) {
        ModelComponent mc = new ModelComponent(gameObject);
        mc.setModel((ModelAsset) assetManager.findAssetByID(modelComponentDTO.getModelID()), false);
        mc.setUseModelCache(modelComponentDTO.isUseModelCache());

        for(Map.Entry<String, String> entry : modelComponentDTO.getMaterials().entrySet()) {
            mc.getMaterials().put(entry.getKey(), (MaterialAsset) assetManager.findAssetByID(entry.getValue()));
        }
        mc.applyMaterials();

        return mc;
    }
}
