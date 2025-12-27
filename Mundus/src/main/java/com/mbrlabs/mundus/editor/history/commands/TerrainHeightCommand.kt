package com.mbrlabs.mundus.editor.history.commands

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.history.Command
import com.mbrlabs.mundus.editorcommons.events.TerrainVerticesChangedEvent

class TerrainHeightCommand(private var terrain: TerrainComponent?) : Command {

    private var heightDataBefore: FloatArray? = null
    private var heightDataAfter: FloatArray? = null

    fun setHeightDataBefore(data: FloatArray) {
        heightDataBefore = FloatArray(data.size)
        System.arraycopy(data, 0, heightDataBefore!!, 0, data.size)
    }

    fun setHeightDataAfter(data: FloatArray) {
        heightDataAfter = FloatArray(data.size)
        System.arraycopy(data, 0, heightDataAfter!!, 0, data.size)
    }

    override fun execute() {
        copyHeightData(heightDataAfter!!)
        terrain!!.terrainAsset.terrain.update()
        terrain!!.lodManager.disable()
        Mundus.postEvent(TerrainVerticesChangedEvent(terrain!!))
    }

    override fun undo() {
        copyHeightData(heightDataBefore!!)
        terrain!!.terrainAsset.terrain.update()
        terrain!!.lodManager.disable()
        Mundus.postEvent(TerrainVerticesChangedEvent(terrain!!))
    }

    private fun copyHeightData(data: FloatArray) {
        for (i in data.indices) {
            terrain!!.terrainAsset.terrain.heightData!![i] = data[i]
        }
    }

}
