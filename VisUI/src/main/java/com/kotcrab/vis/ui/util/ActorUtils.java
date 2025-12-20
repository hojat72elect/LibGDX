package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

/**
 * {@link Actor} related utils.
 */
public class ActorUtils {
    /**
     * Makes sures that actor will be fully visible in stage. If it's necessary actor position will be changed to fit it
     * on screen.
     *
     * @throws IllegalStateException if actor does not belong to any stage.
     */
    public static void keepWithinStage(Actor actor) {
        Stage stage = actor.getStage();
        if (stage == null) {
            throw new IllegalStateException("keepWithinStage cannot be used on Actor that doesn't belong to any stage. ");
        }
        keepWithinStage(actor.getStage(), actor);
    }

    /**
     * Makes sures that actor will be fully visible in stage. If it's necessary actor position will be changed to fit it
     * on screen.
     */
    public static void keepWithinStage(Stage stage, Actor actor) {
        //taken from scene2d.ui Window
        Camera camera = stage.getCamera();
        if (camera instanceof OrthographicCamera orthographicCamera) {
            float parentWidth = stage.getWidth();
            float parentHeight = stage.getHeight();
            if (actor.getX(Align.right) - camera.position.x > parentWidth / 2 / orthographicCamera.zoom)
                actor.setPosition(camera.position.x + parentWidth / 2 / orthographicCamera.zoom, actor.getY(Align.right), Align.right);
            if (actor.getX(Align.left) - camera.position.x < -parentWidth / 2 / orthographicCamera.zoom)
                actor.setPosition(camera.position.x - parentWidth / 2 / orthographicCamera.zoom, actor.getY(Align.left), Align.left);
            if (actor.getY(Align.top) - camera.position.y > parentHeight / 2 / orthographicCamera.zoom)
                actor.setPosition(actor.getX(Align.top), camera.position.y + parentHeight / 2 / orthographicCamera.zoom, Align.top);
            if (actor.getY(Align.bottom) - camera.position.y < -parentHeight / 2 / orthographicCamera.zoom)
                actor.setPosition(actor.getX(Align.bottom), camera.position.y - parentHeight / 2 / orthographicCamera.zoom, Align.bottom);
        } else if (actor.getParent() == stage.getRoot()) {
            float parentWidth = stage.getWidth();
            float parentHeight = stage.getHeight();
            if (actor.getX() < 0) actor.setX(0);
            if (actor.getRight() > parentWidth) actor.setX(parentWidth - actor.getWidth());
            if (actor.getY() < 0) actor.setY(0);
            if (actor.getTop() > parentHeight) actor.setY(parentHeight - actor.getHeight());
        }
    }
}
