package com.kotcrab.vis.ui.test.manual

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener

abstract class WindowResizeListener() : EventListener {
    override fun handle(event: Event): Boolean {
        if (event !is WindowResizeEvent) return false
        resize()
        return false
    }

    abstract fun resize()
}

class WindowResizeEvent : Event()
