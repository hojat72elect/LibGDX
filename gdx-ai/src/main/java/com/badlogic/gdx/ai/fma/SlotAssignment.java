package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.math.Vector;

/**
 * A {@code SlotAssignment} instance represents the assignment of a single {@link FormationMember} to its slot in the
 * {@link Formation}.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public class SlotAssignment<T extends Vector<T>> {
    public FormationMember<T> member;
    public int slotNumber;

    /**
     * Creates a {@code SlotAssignment} for the given {@code member}.
     *
     * @param member the member of this slot assignment
     */
    public SlotAssignment(FormationMember<T> member) {
        this(member, 0);
    }

    /**
     * Creates a {@code SlotAssignment} for the given {@code member} and {@code slotNumber}.
     *
     * @param member the member of this slot assignment
     */
    public SlotAssignment(FormationMember<T> member, int slotNumber) {
        this.member = member;
        this.slotNumber = slotNumber;
    }
}
