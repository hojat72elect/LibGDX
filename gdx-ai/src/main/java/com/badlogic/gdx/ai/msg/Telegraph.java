package com.badlogic.gdx.ai.msg;

/**
 * Any object implementing the {@code Telegraph} interface can act as the sender or the receiver of a {@link Telegram}.
 */
public interface Telegraph {

    /**
     * Handles the telegram just received.
     *
     * @param msg The telegram
     * @return {@code true} if the telegram has been successfully handled; {@code false} otherwise.
     */
    boolean handleMessage(Telegram msg);
}
