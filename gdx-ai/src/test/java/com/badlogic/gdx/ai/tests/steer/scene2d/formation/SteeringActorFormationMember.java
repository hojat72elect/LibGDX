package com.badlogic.gdx.ai.tests.steer.scene2d.formation;

import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.tests.steer.scene2d.Scene2dLocation;
import com.badlogic.gdx.ai.tests.steer.scene2d.SteeringActor;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@code SteeringActorFormationMember} is a scene2d {@link SteeringActor} implementing the {@link FormationMember} interface.
 */
public class SteeringActorFormationMember extends SteeringActor implements FormationMember<Vector2> {

    public Scene2dLocation target;

    public SteeringActorFormationMember(TextureRegion region) {
        this(region, false);
    }

    public SteeringActorFormationMember(TextureRegion region, boolean independentFacing) {
        super(region, independentFacing);
        this.target = new Scene2dLocation();
    }

    @Override
    public Location<Vector2> getTargetLocation() {
        return target;
    }
}
