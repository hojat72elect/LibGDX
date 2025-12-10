package com.badlogic.gdx.graphics.g3d.decals

import com.badlogic.gdx.utils.Array

/**
 * Handles a single group's pre and post render arrangements. Can be plugged into [PluggableGroupStrategy] to build modular
 * [GroupStrategies][GroupStrategy].
 */
interface GroupPlug {
    fun beforeGroup(contents: Array<Decal>)

    fun afterGroup()
}
