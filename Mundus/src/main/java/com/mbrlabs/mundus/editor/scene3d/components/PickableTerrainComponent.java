package com.mbrlabs.mundus.editor.scene3d.components;

import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.editor.shader.Shaders;
import com.mbrlabs.mundus.editor.tools.picker.PickerColorEncoder;
import com.mbrlabs.mundus.editor.tools.picker.PickerIDAttribute;

public class PickableTerrainComponent extends TerrainComponent implements PickableComponent {

    public PickableTerrainComponent(GameObject go) {
        super(go);
    }

    @Override
    public void encodeRaypickColorId() {
        PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(gameObject);
        modelInstance.materials.first().set(goIDa);
    }

    @Override
    public void renderPick() {
        gameObject.sceneGraph.scene.batch.render(modelInstance, Shaders.INSTANCE.getPickerShader());
    }

    @Override
    public Component clone(GameObject go) {
        PickableTerrainComponent terrainComponent = new PickableTerrainComponent(go);
        terrainComponent.setTerrainAsset(terrainAsset);
        terrainComponent.encodeRaypickColorId();
        return terrainComponent;
    }
}
