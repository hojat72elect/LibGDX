package com.badlogic.gdx.math;

import com.badlogic.gdx.utils.NumberUtils;
import java.io.Serializable;

/**
 * A convenient 2D ellipse class, based on the circle class.
 * بیضی
 */
public class Ellipse implements Serializable, Shape2D {

    private static final long serialVersionUID = 7381533206532032099L;
    public float x, y;
    public float width, height;

    /**
     * Construct a new ellipse with all values set to zero.
     */
    public Ellipse() {

    }

    /**
     * Copy constructor (copies characteristics of an ellipse into another ellipse).
     * @param ellipse Ellipse to construct a copy of.
     */
    public Ellipse(Ellipse ellipse) {
        this.x = ellipse.x;
        this.y = ellipse.y;
        this.width = ellipse.width;
        this.height = ellipse.height;
    }

    /**
     * @param x      X coordinate of the center of the ellipse.
     * @param y      Y coordinate of the center of the ellipse.
     * @param width  the width of the ellipse.
     * @param height the height of the ellipse.
     */
    public Ellipse(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @param position Position vector of the center of the ellipse.
     * @param width    the width of the ellipse.
     * @param height   the height of the ellipse.
     */
    public Ellipse(Vector2 position, float width, float height) {
        this.x = position.x;
        this.y = position.y;
        this.width = width;
        this.height = height;
    }

    /**
     * @param position Position vector of the center of the ellipse.
     * @param size Size vector.
     */
    public Ellipse(Vector2 position, Vector2 size) {
        this.x = position.x;
        this.y = position.y;
        this.width = size.x;
        this.height = size.y;
    }

    /**
     * Constructs a new {@link Ellipse} from the position and radius of a {@link Circle} (since circles are special cases of ellipses).
     * @param circle The circle to take the values of.
     */
    public Ellipse(Circle circle) {
        this.x = circle.x;
        this.y = circle.y;
        this.width = circle.radius * 2F;
        this.height = circle.radius * 2F;
    }

    /**
     * Checks whether this ellipse contains the given point.
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if this ellipse contains the given point; false otherwise.
     */
    public boolean contains(float x, float y) {
        x = x - this.x;
        y = y - this.y;

        return (x * x) / (width * 0.5F * width * 0.5F) + (y * y) / (height * 0.5F * height * 0.5F) <= 1.0F;
    }

    /**
     * Checks whether this ellipse contains the given point.
     * @param point Position vector.
     * @return true if this ellipse contains the given point; false otherwise.
     */
    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * Sets a new position and size for this ellipse.
     * @param x      X coordinate of the center of the ellipse.
     * @param y      Y coordinate of the center of the ellipse.
     * @param width  the width of the ellipse.
     * @param height the height of the ellipse.
     */
    public void set(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets a new position and size for this ellipse based upon another ellipse.
     * @param ellipse The ellipse to copy the position and size of.
     */
    public void set(Ellipse ellipse) {
        x = ellipse.x;
        y = ellipse.y;
        width = ellipse.width;
        height = ellipse.height;
    }

    /**
     * If I'm not mistaken, this function converts our ellipse to a circle.
     */
    public void set(Circle circle) {
        this.x = circle.x;
        this.y = circle.y;
        this.width = circle.radius * 2F;
        this.height = circle.radius * 2F;
    }

    public void set(Vector2 position, Vector2 size) {
        this.x = position.x;
        this.y = position.y;
        this.width = size.x;
        this.height = size.y;
    }

    /**
     * Sets the x and y-coordinates of ellipse center from a {@link Vector2}.
     * @param position The position vector of the center of the ellipse
     * @return this ellipse for chaining
     */
    public Ellipse setPosition(Vector2 position) {
        this.x = position.x;
        this.y = position.y;

        return this;
    }

    /**
     * Sets the x and y-coordinates of ellipse center
     * @param x The x-coordinate of the center of the ellipse
     * @param y The y-coordinate of the center of the ellipse
     * @return this ellipse for chaining
     */
    public Ellipse setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * Sets the width and height of this ellipse.
     * @param width  The width.
     * @param height The height.
     * @return this ellipse for chaining.
     */
    public Ellipse setSize(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    /**
     * @return The area of this {@link Ellipse} as {@link MathUtils#PI} * {@link Ellipse#width} * {@link Ellipse#height}.
     */
    public float area() {
        return MathUtils.PI * (this.width * this.height) / 4;
    }

    /**
     * Approximates the circumference of this {@link Ellipse}. Oddly enough, the circumference of an ellipse is actually difficult to compute exactly.
     * @return The Ramanujan approximation to the circumference of an ellipse if one dimension is at least three times longer than the other, else the simpler
     * approximation.
     */
    public float circumference() {
        float a = this.width / 2F;
        float b = this.height / 2F;
        if (a * 3 > b || b * 3 > a) {
            // If one dimension is three times as long as the other...
            return (float) (MathUtils.PI * ((3 * (a + b)) - Math.sqrt((3 * a + b) * (a + 3 * b)))); // This is Ramanujan's first approximation.
        } else {
            // We can use the simpler approximation, then
            return (float) (MathUtils.PI2 * Math.sqrt((a * a + b * b) / 2)); // This is Euler's approximation.
        }
    }

    /**
     * Returns a {@link String} representation of this {@link Ellipse} of the form {@code [x,y,width,height]}.
     */
    @Override
    public String toString() {
        return "[" + x + "," + y + "," + width + "," + height + "]";
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == this) return true;
        if (otherObject == null || otherObject.getClass() != this.getClass()) return false;
        Ellipse e = (Ellipse) otherObject;
        return this.x == e.x && this.y == e.y && this.width == e.width && this.height == e.height;
    }

    @Override
    public int hashCode() {
        final int prime = 53;
        int result = 1;
        result = prime * result + NumberUtils.floatToRawIntBits(this.height);
        result = prime * result + NumberUtils.floatToRawIntBits(this.width);
        result = prime * result + NumberUtils.floatToRawIntBits(this.x);
        result = prime * result + NumberUtils.floatToRawIntBits(this.y);
        return result;
    }
}
