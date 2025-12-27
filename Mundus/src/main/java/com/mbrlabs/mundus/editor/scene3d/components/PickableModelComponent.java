package com.mbrlabs.mundus.editor.scene3d.components;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent;
import com.mbrlabs.mundus.editor.shader.Shaders;
import com.mbrlabs.mundus.editor.tools.picker.PickerColorEncoder;
import com.mbrlabs.mundus.editor.tools.picker.PickerIDAttribute;

public class PickableModelComponent extends ModelComponent implements PickableComponent {

    public PickableModelComponent(GameObject go) {
        super(go);
    }

    @Override
    public void encodeRaypickColorId() {
        PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(gameObject);
        for (Material material : modelInstance.materials) {
            material.set(goIDa);
        }
    }

    @Override
    public void renderPick() {
        gameObject.sceneGraph.scene.batch.render(modelInstance, Shaders.INSTANCE.getPickerShader());
    }

    @Override
    public Component clone(GameObject go) {
        PickableModelComponent mc = new PickableModelComponent(go);
        mc.modelAsset = this.modelAsset;
        mc.setModel(new ModelInstance(modelAsset.getModel()));
        mc.materials.putAll(this.materials);
        mc.setUseModelCache(useModelCache);
        mc.encodeRaypickColorId();
        gameObject.sceneGraph.scene.modelCacheManager.requestModelCacheRebuild();
        return mc;
    }
}
