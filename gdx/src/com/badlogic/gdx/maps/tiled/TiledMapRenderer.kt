package com.badlogic.gdx.maps.tiled

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapRenderer

/**
 * Info : This interface was moved to Kerman game engine.
 */
interface TiledMapRenderer : MapRenderer {
    fun renderObjects(layer: MapLayer)
    fun renderObject(mapObject: MapObject)
    fun renderTileLayer(layer: TiledMapTileLayer)
    fun renderImageLayer(layer: TiledMapImageLayer)
}
