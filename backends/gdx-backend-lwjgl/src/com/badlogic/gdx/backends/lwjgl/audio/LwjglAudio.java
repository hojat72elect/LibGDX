
package com.badlogic.gdx.backends.lwjgl.audio;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public interface LwjglAudio extends Audio {

    /**
     * Updates audio state (usually called every frame by the {@link LwjglApplication})
     */
    void update();
}
