package com.mbrlabs.mundus.editor.history.commands

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.editor.history.Command

/**
 * A wrapper for modifying the height of multiple terrains.
 */
class TerrainsHeightCommand : Command {
    private var terrainHeightCommands = HashMap<TerrainComponent, TerrainHeightCommand>()

    /**
     * Add a terrain to be modified and set the height data before.
     */
    fun addTerrain(terrain: TerrainComponent) {
        val command = TerrainHeightCommand(terrain)
        command.setHeightDataBefore(terrain.terrainAsset.terrain.heightData)
        terrainHeightCommands[terrain] = command
    }

    /**
     * Call this after modifying the terrains is complete.
     * This will set the height data after.
     */
    fun setHeightDataAfter() {
        for (terrain in terrainHeightCommands.keys) {
            val command = terrainHeightCommands[terrain]
            command!!.setHeightDataAfter(terrain.terrainAsset.terrain.heightData)
        }
    }

    override fun execute() {
        for (terrain in terrainHeightCommands.keys) {
            val command = terrainHeightCommands[terrain]
            command!!.execute()
        }
    }

    override fun undo() {
        for (terrain in terrainHeightCommands.keys) {
            val command = terrainHeightCommands[terrain]
            command!!.undo()
        }
    }

}