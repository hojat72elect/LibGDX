package com.badlogic.gdx.physics.box2d

/**
 * Callback class for AABB queries.
 */
interface QueryCallback {
    /**
     * Called for each fixture found in the query AABB.
     *
     * @return false to terminate the query.
     */
    fun reportFixture(fixture: Fixture): Boolean
}
