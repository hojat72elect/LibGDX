package com.mbrlabs.mundus.editor.core.converter;

import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.assets.MaterialAsset;
import com.mbrlabs.mundus.commons.assets.ModelAsset;
import com.mbrlabs.mundus.commons.dto.ModelComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.editor.scene3d.components.PickableModelComponent;
import com.mbrlabs.mundus.editor.utils.Log;

import java.util.Map;

/**
 * Converter for model component.
 */
public class ModelComponentConverter {

    private final static String TAG = ModelComponentConverter.class.getSimpleName();

    /**
     * Converts {@link ModelComponentDTO} to {@link PickableModelComponent}.
     */
    public static PickableModelComponent convert(ModelComponentDTO dto, GameObject go,
                                                 Map<String, Asset> assets) {
        ModelAsset model = (ModelAsset) assets.get(dto.getModelID());

        if (model == null) {
            Log.fatal(TAG, "MModel for MModelInstance not found: {}", dto.getModelID());
            return null;
        }

        PickableModelComponent component = new PickableModelComponent(go);
        component.setModel(model, false);
        component.setUseModelCache(dto.isUseModelCache());

        for (String g3dbMatID : dto.getMaterials().keySet()) {
            String uuid = dto.getMaterials().get(g3dbMatID);
            MaterialAsset matAsset = (MaterialAsset) assets.get(uuid);
            component.getMaterials().put(g3dbMatID, matAsset);
        }

        return component;
    }

    /**
     * Converts {@link PickableModelComponent} to {@link ModelComponentDTO}.
     */
    public static ModelComponentDTO convert(PickableModelComponent modelComponent) {
        ModelComponentDTO dto = new ModelComponentDTO();
        dto.setModelID(modelComponent.getModelAsset().getID());
        dto.setUseModelCache(modelComponent.shouldCache());

        // materials
        for (String g3dbMatID : modelComponent.getMaterials().keys()) {
            dto.getMaterials().put(g3dbMatID, modelComponent.getMaterials().get(g3dbMatID).getID());
        }

        return dto;
    }
}
