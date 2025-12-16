package com.badlogic.ashley.benchmark.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent extends Component {
    public final Vector3 pos = new Vector3();
    public final Vector2 scale = new Vector2(1.0f, 1.0f);
    public float rotation = 0.0f;
}
