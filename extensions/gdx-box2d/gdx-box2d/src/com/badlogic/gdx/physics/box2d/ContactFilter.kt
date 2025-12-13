package com.badlogic.gdx.physics.box2d

/**
 * Implement this class to provide collision filtering. In other words, you can implement this class if you want finer control
 * over contact creation.
 */
interface ContactFilter {
    fun shouldCollide(fixtureA: Fixture, fixtureB: Fixture): Boolean
}
