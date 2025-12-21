package games.rednblack.editor.renderer.components.physics;

/**
 * This enum is used to check which body sensor collides with another box2d object.
 * <p>
 * Each surrounding sensor sets the user data to the corresponding enum.
 *
 *
 */
public enum SensorUserData {

    /**
     * Bottom sensor.
     */
    BOTTOM,
    /**
     * Left sensor.
     */
    LEFT,
    /**
     * Right sensor.
     */
    RIGHT,
    /**
     * Top sensor.
     */
    TOP
}
