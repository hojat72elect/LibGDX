package com.badlogic.gdx.ai;

import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.steer.behaviors.Jump;

/**
 * The {@code Timepiece} is the AI clock which gives you the current time and the last delta time i.e., the time span between the
 * current frame and the last frame in seconds. This is the only service provider that does not depend on the environment, whether
 * libgdx or not. It is needed because some parts of gdx-ai (like for instance {@link MessageDispatcher}, {@link Jump} steering
 * behavior and {@link Wait} task) have a notion of spent time and we want to support game pause. It's developer's responsibility
 * to update the timepiece on each game loop. When the game is paused you simply don't update the timepiece.
 */
public interface Timepiece {

    /**
     * Returns the time accumulated up to the current frame in seconds.
     */
    float getTime();

    /**
     * Returns the time span between the current frame and the last frame in seconds.
     */
    float getDeltaTime();

    /**
     * Updates this timepiece with the given delta time.
     *
     * @param deltaTime the time in seconds since the last frame.
     */
    void update(float deltaTime);
}
