package com.badlogic.gdx.ai.tests.steer.scene2d.tests;

import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.tests.SteeringBehaviorsTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.Scene2dSteeringTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.Scene2dTargetInputProcessor;
import com.badlogic.gdx.ai.tests.steer.scene2d.SteeringActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;


/**
 * A class to test and experiment with the {@link Seek} behavior.
 *
 * @autor davebaol
 */
public class Scene2dSeekTest extends Scene2dSteeringTest {

    SteeringActor character;
    SteeringActor target;

    public Scene2dSeekTest(SteeringBehaviorsTest container) {
        super(container, "Seek");
    }

    @Override
    public void create() {
        super.create();

        character = new SteeringActor(container.badlogicSmall, false);
        target = new SteeringActor(container.target);
        inputProcessor = new Scene2dTargetInputProcessor(target);

        character.setMaxLinearSpeed(250);
        character.setMaxLinearAcceleration(2000);

        final Seek<Vector2> seekSB = new Seek<Vector2>(character, target);
        character.setSteeringBehavior(seekSB);

        testTable.addActor(character);
        testTable.addActor(target);

        character.setPosition(container.stageWidth / 2, container.stageHeight / 2, Align.center);
        target.setPosition(MathUtils.random(container.stageWidth), MathUtils.random(container.stageHeight), Align.center);

        Table detailTable = new Table(container.skin);

        detailTable.row();
        addMaxLinearAccelerationController(detailTable, character, 0, 10000, 20);

        detailTable.row();
        addSeparator(detailTable);

        detailTable.row();
        addMaxLinearSpeedController(detailTable, character);

        detailWindow = createDetailWindow(detailTable);
    }

    @Override
    public void draw() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
