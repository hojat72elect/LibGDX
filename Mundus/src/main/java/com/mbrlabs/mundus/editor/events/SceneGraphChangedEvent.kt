package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class SceneGraphChangedEvent {

    interface SceneGraphChangedListener {
        @Subscribe
        fun onSceneGraphChanged(event: SceneGraphChangedEvent)
    }

}
