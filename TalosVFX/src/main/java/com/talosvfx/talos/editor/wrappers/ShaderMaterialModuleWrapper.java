
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.editor.addons.scene.assets.AssetRepository;
import com.talosvfx.talos.editor.widgets.ui.common.GenericAssetSelectionWidget;
import com.talosvfx.talos.runtime.assets.GameAsset;
import com.talosvfx.talos.runtime.assets.GameAssetType;
import com.talosvfx.talos.runtime.shader.BaseShaderData;
import com.talosvfx.talos.runtime.vfx.modules.MaterialModule;
import com.talosvfx.talos.runtime.vfx.modules.ShaderMaterialModule;

public class ShaderMaterialModuleWrapper extends ModuleWrapper<ShaderMaterialModule> {

    private Label assetNameLabel;
    private GenericAssetSelectionWidget<BaseShaderData> selector;

    private GameAsset<BaseShaderData> asset;

    public ShaderMaterialModuleWrapper() {
        super();
    }

    @Override
    public void setModuleToDefaults() {
        module.setToDefault();
    }

    @Override
    protected float reportPrefWidth() {
        return 250;
    }

    @Override
    protected void configureSlots() {

        addOutputSlot("", MaterialModule.MATERIAL_MODULE);

        asset = AssetRepository.getInstance().getAssetForIdentifier("white", GameAssetType.SHADER);

        selector = new GenericAssetSelectionWidget<>(GameAssetType.SHADER);
        selector.setValue(asset);
        contentWrapper.add(selector).growX().right().expandX().padBottom(50);
        contentWrapper.row();


        if (asset.getResource() != null) {
        }

        selector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                asset = selector.getValue();
                module.setGameAsset(asset);
                if (asset.getResource() != null) {
                }
                moduleBoardWidget.app.dataModified();
            }
        });
    }


    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("asset", asset.nameIdentifier);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        String identifier = jsonData.getString("asset", "white");
        asset = AssetRepository.getInstance().getAssetForIdentifier(identifier, GameAssetType.SPRITE);
        selector.setValue(asset);


        if (asset.getResource() != null) {
        }
    }
}
