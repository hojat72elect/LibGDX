package com.mbrlabs.mundus.pluginapi.manager;

import com.badlogic.gdx.files.FileHandle;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.assets.CustomAsset;
import com.mbrlabs.mundus.editorcommons.exceptions.AssetAlreadyExistsException;

public interface AssetManager {

    /**
     * Creates new asset.
     *
     * @param file The file for asset.
     *
     * @throws AssetAlreadyExistsException If asset already exists.
     */
    CustomAsset createNewAsset(FileHandle file) throws AssetAlreadyExistsException;

    /**
     * Marks asset as modified asset.
     *
     * @param asset The modified asset.
     */
    void markAsModifiedAsset(Asset asset);

    /**
     * Deletes custom asset.
     *
     * @param asset The asset.
     */
    void deleteAsset(CustomAsset asset);
}
