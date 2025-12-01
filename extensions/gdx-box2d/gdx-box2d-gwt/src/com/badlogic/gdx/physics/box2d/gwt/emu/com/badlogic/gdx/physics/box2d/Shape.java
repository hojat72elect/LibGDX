
package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.utils.Disposable;

/** A shape is used for collision detection. You can create a shape however you like. Shapes used for simulation in b2World are
 * created automatically when a b2Fixture is created. Shapes may encapsulate a one or more child shapes.
 * 
 * NOTE: YOU NEED TO DISPOSE SHAPES YOU CREATED YOURSELF AFTER YOU NO LONGER USE THEM! E.g. after calling body.createFixture();
 * @author mzechner */
public abstract class Shape implements Disposable {
	/** Enum describing the type of a shape
	 * @author mzechner */
	public enum Type {
		Circle, Polygon, Edge, Chain,
	}

    public final org.jbox2d.collision.shapes.Shape shape;

	public Shape (org.jbox2d.collision.shapes.Shape shape) {
		this.shape = shape;
	}

	/** Get the type of this shape. You can use this to down cast to the concrete shape.
	 * @return the shape type. */
	public abstract Type getType ();

	/** Returns the radius of this shape */
	public abstract float getRadius ();

	/** Sets the radius of this shape */
	public abstract void setRadius (float radius);

	/** Needs to be called when the shape is no longer used, e.g. after a fixture was created based on the shape. */
	@Override
	public void dispose () {
	}

	/** Get the number of child primitives. */
	public abstract int getChildCount ();
}
