package com.badlogic.ashley.benchmark.ashley.systems;

import com.badlogic.ashley.benchmark.ashley.components.MovementComponent;
import com.badlogic.ashley.benchmark.ashley.components.PositionComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class MovementSystem extends IteratingSystem {
    private final Vector2 tmp = new Vector2();
    private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        MovementComponent mov = mm.get(entity);

        tmp.set(mov.accel).scl(deltaTime);
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(deltaTime);
        pos.pos.add(tmp.x, tmp.y, 0.0f);
    }
}
