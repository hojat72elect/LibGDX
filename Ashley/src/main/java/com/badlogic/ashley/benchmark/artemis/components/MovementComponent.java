package com.badlogic.ashley.benchmark.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent extends Component {
    public final Vector2 velocity = new Vector2();
    public final Vector2 accel = new Vector2();
}
