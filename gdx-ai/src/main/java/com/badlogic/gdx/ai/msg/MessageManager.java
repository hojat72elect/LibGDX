package com.badlogic.gdx.ai.msg;

/**
 * The {@code MessageManager} is a singleton {@link MessageDispatcher} in charge of the creation, dispatch, and management of
 * telegrams.
 */
public final class MessageManager extends MessageDispatcher {

    private static final MessageManager INSTANCE = new MessageManager();

    /**
     * Don't let anyone else instantiate this class
     */
    private MessageManager() {
    }

    /**
     * Returns the singleton instance of the message dispatcher.
     */
    public static MessageManager getInstance() {
        return INSTANCE;
    }
}
