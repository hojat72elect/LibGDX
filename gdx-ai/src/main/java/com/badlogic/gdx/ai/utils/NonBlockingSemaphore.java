package com.badlogic.gdx.ai.utils;

/**
 * A counting semaphore that does not block the thread when the requested resource is not available. No actual resource objects
 * are used; the semaphore just keeps a count of the number available and acts accordingly.
 */
public interface NonBlockingSemaphore {

    /**
     * Acquires a resource if available.
     * <p>
     * An invocation of this method yields exactly the same result as {@code acquire(1)}
     *
     * @return {@code true} if the resource has been acquired; {@code false} otherwise.
     */
    boolean acquire();

    /**
     * Acquires the specified number of resources if they all are available.
     *
     * @return {@code true} if all the requested resources have been acquired; {@code false} otherwise.
     */
    boolean acquire(int resources);

    /**
     * Releases a resource returning it to this semaphore.
     * <p>
     * An invocation of this method yields exactly the same result as {@code release(1)}
     *
     * @return {@code true} if the resource has been released; {@code false} otherwise.
     */
    boolean release();

    /**
     * Releases the specified number of resources returning it to this semaphore.
     *
     * @return {@code true} if all the requested resources have been released; {@code false} otherwise.
     */
    boolean release(int resources);

    /**
     * Abstract factory for creating concrete instances of classes implementing {@link NonBlockingSemaphore}.
     *
     *
     */
    interface Factory {

        /**
         * Creates a semaphore with the specified name and resources.
         *
         * @param name         the name of the semaphore
         * @param maxResources the maximum number of resource
         * @return the newly created semaphore.
         */
        NonBlockingSemaphore createSemaphore(String name, int maxResources);
    }
}
