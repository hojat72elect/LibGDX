
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.talosvfx.talos.editor.addons.scene.assets.AssetRepository;
import com.talosvfx.talos.editor.widgets.ui.common.GenericAssetSelectionWidget;
import com.talosvfx.talos.runtime.assets.GameAsset;
import com.talosvfx.talos.runtime.assets.GameAssetType;
import com.talosvfx.talos.runtime.vfx.modules.MaterialModule;
import com.talosvfx.talos.runtime.vfx.modules.SpriteMaterialModule;

public class SpriteMaterialModuleWrapper extends ModuleWrapper<SpriteMaterialModule> {

    private Label assetNameLabel;
    private GenericAssetSelectionWidget<AtlasSprite> selector;

    private GameAsset<AtlasSprite> asset;
    private Image texturePreview;

    public SpriteMaterialModuleWrapper() {
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

        asset = AssetRepository.getInstance().getAssetForIdentifier("white", GameAssetType.SPRITE);

        selector = new GenericAssetSelectionWidget<>(GameAssetType.SPRITE);
        selector.setValue(asset);
        contentWrapper.add(selector).growX().right().expandX();
        contentWrapper.row();

        texturePreview = new Image();
        texturePreview.setScaling(Scaling.fit);
        contentWrapper.add(texturePreview).size(200);

        if (asset.getResource() != null) {
            texturePreview.setDrawable(new TextureRegionDrawable(asset.getResource()));
        }

        selector.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                asset = selector.getValue();
                module.setGameAsset(asset);
                if (asset.getResource() != null) {
                    texturePreview.setDrawable(new TextureRegionDrawable(asset.getResource()));
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
            texturePreview.setDrawable(new TextureRegionDrawable(asset.getResource()));
        }
    }
}
