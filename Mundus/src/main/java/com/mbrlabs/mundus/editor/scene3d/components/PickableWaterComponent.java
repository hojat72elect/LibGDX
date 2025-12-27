package com.mbrlabs.mundus.editor.scene3d.components;

import com.badlogic.gdx.graphics.g3d.Shader;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.WaterComponent;
import com.mbrlabs.mundus.editor.shader.Shaders;
import com.mbrlabs.mundus.editor.tools.picker.PickerColorEncoder;
import com.mbrlabs.mundus.editor.tools.picker.PickerIDAttribute;

public class PickableWaterComponent extends WaterComponent implements PickableComponent {

    public PickableWaterComponent(GameObject go, Shader shader) {
        super(go, shader);
    }

    @Override
    public void encodeRaypickColorId() {
        PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(gameObject);
        waterAsset.water.modelInstance.materials.first().set(goIDa);
    }

    @Override
    public void renderPick() {
        gameObject.sceneGraph.scene.batch.render(waterAsset.water, Shaders.INSTANCE.getPickerShader());
    }
}
