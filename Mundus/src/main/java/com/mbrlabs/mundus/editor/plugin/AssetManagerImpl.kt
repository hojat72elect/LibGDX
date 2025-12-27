package com.mbrlabs.mundus.editor.plugin

import com.badlogic.gdx.files.FileHandle
import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.commons.assets.CustomAsset
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.events.AssetDeletedEvent
import com.mbrlabs.mundus.editor.events.AssetImportEvent
import com.mbrlabs.mundus.editorcommons.exceptions.AssetAlreadyExistsException
import com.mbrlabs.mundus.pluginapi.manager.AssetManager

class AssetManagerImpl : AssetManager {

    private val projectManager = Mundus.inject<ProjectManager>()

    @Throws(AssetAlreadyExistsException::class)
    override fun createNewAsset(file: FileHandle): CustomAsset {
        val assetManager = projectManager.current().assetManager
        val customAsset = assetManager.createCustomAsset(file)
        Mundus.postEvent(AssetImportEvent(customAsset))
        return customAsset
    }

    override fun markAsModifiedAsset(asset: Asset) {
        projectManager.current().assetManager.addModifiedAsset(asset)
    }

    override fun deleteAsset(asset: CustomAsset) {
        projectManager.current().assetManager.deleteAsset(asset)
        Mundus.postEvent(AssetDeletedEvent())
    }
}
