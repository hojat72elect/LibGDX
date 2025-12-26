package com.mbrlabs.mundus.commons.dto;

import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.assets.MaterialAsset;
import com.mbrlabs.mundus.commons.assets.TextureAsset;

import java.util.HashMap;
import java.util.Map;

public class ModelComponentDTO implements AssetUsageDTO {

    private String modelID;
    private HashMap<String, String> materials; // g3db material id to material asset uuid
    private boolean useModelCache;

    public ModelComponentDTO() {
        materials = new HashMap<>();
    }

    public HashMap<String, String> getMaterials() {
        return materials;
    }

    public void setMaterials(HashMap<String, String> materials) {
        this.materials = materials;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public boolean isUseModelCache() {
        return useModelCache;
    }

    public void setUseModelCache(boolean useModelCache) {
        this.useModelCache = useModelCache;
    }

    @Override
    public boolean usesAsset(Asset assetToCheck, Map<String, Asset> assetMap) {
        if (assetToCheck.getID().equals(modelID)) {
            return true;
        }

        if (assetToCheck instanceof MaterialAsset) {
            for (String matID : materials.keySet()) {
                if (materials.get(matID).equals(assetToCheck.getID())) {
                    return true;
                }
            }
        }

        if (assetToCheck instanceof TextureAsset) {
            for (String matID : materials.keySet()) {
                MaterialAsset mat = (MaterialAsset) assetMap.get(materials.get(matID));
                if (mat != null && mat.usesAsset(assetToCheck)) {
                    return true;
                }
            }
        }

        return false;
    }
}
