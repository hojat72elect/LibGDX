package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class FullScreenEvent(val isFullScreen: Boolean) {

    interface FullScreenEventListener {
        @Subscribe
        fun onFullScreenEvent(event: FullScreenEvent)
    }

}
