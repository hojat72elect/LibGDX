package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.utils.Array;

/**
 * This interface defines how each {@link FormationMember} is assigned to a slot in the {@link Formation}.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public interface SlotAssignmentStrategy<T extends Vector<T>> {

    /**
     * Updates the assignment of members to slots
     */
    void updateSlotAssignments(Array<SlotAssignment<T>> assignments);

    /**
     * Calculates the number of slots from the assignment data.
     */
    int calculateNumberOfSlots(Array<SlotAssignment<T>> assignments);

    /**
     * Removes the slot assignment at the specified index.
     */
    void removeSlotAssignment(Array<SlotAssignment<T>> assignments, int index);
}
