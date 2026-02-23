package com.badlogic.gdx.ai.fma.patterns;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;

/**
 * The offensive circle posts members around the circumference of a circle, so their fronts are to the center of the circle. The
 * circle can consist of any number of members. Although a huge number of members might look silly, this implementation doesn't
 * put any fixed limit.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public class OffensiveCircleFormationPattern<T extends Vector<T>> extends DefensiveCircleFormationPattern<T> {

    /**
     * Creates a {@code OffensiveCircleFormationPattern}
     */
    public OffensiveCircleFormationPattern(float memberRadius) {
        super(memberRadius);
    }

    @Override
    public Location<T> calculateSlotLocation(Location<T> outLocation, int slotNumber) {
        super.calculateSlotLocation(outLocation, slotNumber);
        outLocation.setOrientation(outLocation.getOrientation() + MathUtils.PI);
        return outLocation;
    }
}
