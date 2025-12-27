package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class UpdateNoiseTextureEvent {
    interface UpdateNoiseTextureListener {
        @Subscribe
        fun onTextureUpdate(event: UpdateNoiseTextureEvent)
    }
}