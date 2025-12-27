package com.mbrlabs.mundus.editor.history.commands

import com.badlogic.gdx.graphics.Pixmap
import com.mbrlabs.mundus.commons.terrain.Terrain
import com.mbrlabs.mundus.editor.history.DisposableCommand

class TerrainPaintCommand(private var terrain: Terrain?) : DisposableCommand {

    private var after: Pixmap? = null
    private var before: Pixmap? = null

    fun setAfter(data: Pixmap) {
        after = Pixmap(data.width, data.height, data.format)
        after!!.blending = Pixmap.Blending.None
        after!!.drawPixmap(data, 0, 0)
    }

    fun setBefore(data: Pixmap) {
        before = Pixmap(data.width, data.height, data.format)
        before!!.blending = Pixmap.Blending.None
        before!!.drawPixmap(data, 0, 0)
    }

    override fun execute() {
        val sm = terrain!!.terrainTexture.splatmap
        if (sm != null) {
            sm.pixmap.drawPixmap(after!!, 0, 0)
            sm.updateTexture()
        }
    }

    override fun undo() {
        val sm = terrain!!.terrainTexture.splatmap
        if (sm != null) {
            sm.pixmap.drawPixmap(before!!, 0, 0)
            sm.updateTexture()
        }
    }

    override fun dispose() {
        after!!.dispose()
        before!!.dispose()
    }

}
