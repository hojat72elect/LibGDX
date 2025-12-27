package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import java.util.Random

object TestUtils {

    fun createABunchOfModelsOnTheTerrain(count: Int, model: Model, terrainComponent: TerrainComponent): Array<ModelInstance> {
        val boxInstances = Array<ModelInstance>()
        val rand = Random()

        val tv3 = Vector3()

        for (i in 0..count - 1) {
            val mi = ModelInstance(model)
            terrainComponent.modelInstance.transform.getTranslation(tv3)
            mi.transform.setTranslation(tv3)
            val x = terrainComponent.terrainAsset.terrain.terrainWidth * rand.nextFloat()
            val z = terrainComponent.terrainAsset.terrain.terrainDepth * rand.nextFloat()
            val y = terrainComponent.terrainAsset.terrain.getHeightAtWorldCoord(x, z, terrainComponent.modelInstance.transform)
            mi.transform.translate(x, y, z)
            boxInstances.add(mi)
        }

        return boxInstances
    }

}
