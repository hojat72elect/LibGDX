package com.badlogic.ashley.benchmark.artemis.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.ashley.benchmark.artemis.components.MovementComponent;
import com.badlogic.ashley.benchmark.artemis.components.PositionComponent;
import com.badlogic.gdx.math.Vector2;

public class MovementSystem extends EntityProcessingSystem {
    @Mapper
    ComponentMapper<PositionComponent> pm;
    @Mapper
    ComponentMapper<MovementComponent> mm;
    private final Vector2 tmp = new Vector2();

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, MovementComponent.class));
    }

    @Override
    protected void initialize() {
        pm = world.getMapper(PositionComponent.class);
        mm = world.getMapper(MovementComponent.class);
    }

    @Override
    protected void process(Entity entity) {
        PositionComponent pos = pm.get(entity);
        MovementComponent mov = mm.get(entity);

        tmp.set(mov.accel).scl(world.getDelta());
        mov.velocity.add(tmp);

        tmp.set(mov.velocity).scl(world.getDelta());
        pos.pos.add(tmp.x, tmp.y, 0.0f);
    }
}
