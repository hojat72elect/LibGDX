package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code FormationMotionModerator} moderates the movement of the formation based on the current positions of the members in its
 * slots: in effect to keep the anchor point on a leash. If the members in the slots are having trouble reaching their targets,
 * then the formation as a whole should be held back to give them a chance to catch up.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public abstract class FormationMotionModerator<T extends Vector<T>> {

    private Location<T> tempLocation;

    /**
     * Update the anchor point to moderate formation motion. This method is called at each frame.
     *
     * @param anchor the anchor point
     */
    public abstract void updateAnchorPoint(Location<T> anchor);

    /**
     * Calculates the drift offset when members are in the given set of slots for the specified pattern.
     *
     * @param centerOfMass    the output location set to the calculated drift offset
     * @param slotAssignments the set of slots
     * @param pattern         the pattern
     * @return the given location for chaining.
     */
    public Location<T> calculateDriftOffset(Location<T> centerOfMass, Array<SlotAssignment<T>> slotAssignments,
                                            FormationPattern<T> pattern) {

        // Clear the center of mass
        centerOfMass.getPosition().setZero();
        float centerOfMassOrientation = 0;

        // Make sure tempLocation is instantiated
        if (tempLocation == null) tempLocation = centerOfMass.newLocation();

        T centerOfMassPos = centerOfMass.getPosition();
        T tempLocationPos = tempLocation.getPosition();

        // Go through each assignment and add its contribution to the center
        float numberOfAssignments = slotAssignments.size;
        for (int i = 0; i < numberOfAssignments; i++) {
            pattern.calculateSlotLocation(tempLocation, slotAssignments.get(i).slotNumber);
            centerOfMassPos.add(tempLocationPos);
            centerOfMassOrientation += tempLocation.getOrientation();
        }

        // Divide through to get the drift offset.
        centerOfMassPos.scl(1f / numberOfAssignments);
        centerOfMassOrientation /= numberOfAssignments;
        centerOfMass.setOrientation(centerOfMassOrientation);

        return centerOfMass;
    }
}
