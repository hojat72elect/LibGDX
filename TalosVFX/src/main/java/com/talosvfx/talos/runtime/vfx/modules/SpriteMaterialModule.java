
package com.talosvfx.talos.runtime.vfx.modules;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.RuntimeContext;
import com.talosvfx.talos.runtime.assets.BaseAssetRepository;
import com.talosvfx.talos.runtime.assets.GameAsset;
import com.talosvfx.talos.runtime.assets.GameAssetType;
import com.talosvfx.talos.runtime.assets.GameResourceOwner;
import com.talosvfx.talos.runtime.vfx.values.ModuleValue;


public class SpriteMaterialModule extends MaterialModule implements GameResourceOwner<AtlasSprite>, GameAsset.GameAssetUpdateListener {


    public GameAsset<AtlasSprite> asset;
    private transient AtlasSprite region;
    /**
     * To be removed, here for backwards compatibility
     */
    @Deprecated
    private String assetIdentifier = "white";
    private ModuleValue<SpriteMaterialModule> moduleOutput;

    @Override
    protected void defineSlots() {
        moduleOutput = new ModuleValue<>();
        moduleOutput.setModule(this);

        createOutputSlot(MATERIAL_MODULE, moduleOutput);
    }

    @Override
    public void processCustomValues() {
    }

    @Override
    public void write(Json json) {
        super.write(json);

        GameResourceOwner.writeGameAsset(json, this);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);

        //deprecated
        assetIdentifier = jsonData.getString("asset", "white");

        GameAsset<AtlasSprite> asset = GameResourceOwner.readAsset(json, jsonData);
        setGameAsset(asset);
    }

    public void setToDefault() {
        BaseAssetRepository baseAssetRepository = RuntimeContext.getInstance().getEditorContext().getBaseAssetRepository();
        GameAsset<AtlasSprite> defaultValue = baseAssetRepository.getAssetForIdentifier("white", GameAssetType.SPRITE);
        setGameAsset(defaultValue);
    }

    @Override
    public void onUpdate() {
        if (asset != null && !asset.isBroken()) {
            region = new AtlasSprite(asset.getResource());
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (asset != null) {
            asset.listeners.removeValue(this, true);
        }
    }

    @Override
    public GameAssetType getGameAssetType() {
        return GameAssetType.SPRITE;
    }

    @Override
    public GameAsset<AtlasSprite> getGameResource() {
        return asset;
    }

    @Override
    public void setGameAsset(GameAsset<AtlasSprite> gameAsset) {
        if (this.asset != null) {
            //Remove from old game asset, it might be the same, but it may also have changed
            this.asset.listeners.removeValue(this, true);
        }

        this.asset = gameAsset;
        asset.listeners.add(this);

        if (asset != null && !asset.isBroken()) {
            region = new AtlasSprite(asset.getResource());
        } else {
            System.out.println("Sprite material asset broken " + asset.nameIdentifier);
        }
    }

    @Override
    public void clearResource() {
        if (asset != null) {
            asset.listeners.removeValue(this, true);
            asset = null;
        }
        region = null;
        moduleOutput = null;
    }

    public AtlasSprite getTextureRegion() {
        return region;
    }
}
