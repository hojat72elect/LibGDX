package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link Draggable}.
 */
public class DraggableTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        if (args != null && args.length == 1 && args[0] instanceof String) {
                            String path = (String) args[0];
                            if ("classpath".equals(name) || "internal".equals(name) || "absolute".equals(name)
                                    || "local".equals(name) || "external".equals(name)) {
                                return new FileHandle(path);
                            }
                        }
                        if ("classpath".equals(name)) {
                            return new FileHandle("test");
                        }
                        return null;
                    });
        }
        if (Gdx.app == null) {
            Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }
        if (Gdx.graphics == null) {
            Gdx.graphics = (Graphics) Proxy.newProxyInstance(
                    Graphics.class.getClassLoader(),
                    new Class[]{Graphics.class},
                    (proxy, method, args) -> {
                        if ("getWidth".equals(method.getName())) return 800;
                        if ("getHeight".equals(method.getName())) return 600;
                        if ("getBackBufferWidth".equals(method.getName())) return 800;
                        if ("getBackBufferHeight".equals(method.getName())) return 600;
                        if ("getDeltaTime".equals(method.getName())) return 1 / 60f;
                        if ("getDensity".equals(method.getName())) return 1f;
                        return null;
                    });
        }

        // Required by Viewport.apply() -> HdpiUtils.glViewport().
        if (Gdx.gl20 == null) {
            Gdx.gl20 = (GL20) Proxy.newProxyInstance(
                    GL20.class.getClassLoader(),
                    new Class[]{GL20.class},
                    (proxy, method, args) -> null);
        }
        if (Gdx.gl == null) {
            Gdx.gl = Gdx.gl20;
        }
    }

    private static Stage newHeadlessStage(final float width, final float height) {
        Viewport viewport = new Viewport() {
            @Override
            public void apply(boolean centerCamera) {
                // No-op: avoids calling HdpiUtils.glViewport() and camera.update(), which depend on native libs.
            }

            @Override
            public void update(int screenWidth, int screenHeight, boolean centerCamera) {
                // No-op: Stage constructor calls update(), so avoid camera updates.
                setScreenBounds(0, 0, screenWidth, screenHeight);
            }
        };
        viewport.setWorldSize(width, height);
        viewport.setCamera(new OrthographicCamera());

        Batch batch = (Batch) Proxy.newProxyInstance(
                Batch.class.getClassLoader(),
                new Class[]{Batch.class},
                (proxy, method, args) -> {
                    // Stage.act does not require a functioning batch; only Stage.draw does.
                    // Return sensible defaults for any getters.
                    if ("getProjectionMatrix".equals(method.getName())) return new Matrix4();
                    if ("getTransformMatrix".equals(method.getName())) return new Matrix4();
                    if ("getColor".equals(method.getName())) return new com.badlogic.gdx.graphics.Color();
                    return null;
                });

        Stage stage = new Stage(viewport, batch);
        stage.getRoot().setSize(width, height);
        return stage;
    }

    @Test
    public void testAttachToRemovesPreviousDraggableListeners() {
        Actor actor = new Actor();

        Draggable first = new Draggable(null);
        Draggable second = new Draggable(null);

        actor.addListener(first);
        Assert.assertTrue(actor.getListeners().contains(first, true));

        second.attachTo(actor);

        Assert.assertFalse(actor.getListeners().contains(first, true));
        Assert.assertTrue(actor.getListeners().contains(second, true));
    }

    @Test
    public void testTouchDownAttachesMimicAndOptionallyHidesActor() {
        Stage stage = newHeadlessStage(800, 600);
        Table parent = new Table();
        parent.setSize(300, 200);
        stage.addActor(parent);

        Actor actor = new Actor();
        actor.setSize(50, 20);
        parent.addActor(actor);

        Draggable draggable = new Draggable(new Draggable.DragAdapter());
        draggable.setInvisibleWhenDragged(true);
        draggable.setBlockInput(false);

        draggable.attachTo(actor);

        InputEvent event = new InputEvent();
        event.setStage(stage);
        event.setListenerActor(actor);
        event.setStageX(100);
        event.setStageY(80);

        boolean handled = draggable.touchDown(event, 5, 6, 0, 0);

        Assert.assertTrue(handled);
        Assert.assertTrue(draggable.isDragged());
        Assert.assertFalse(actor.isVisible());
        Assert.assertEquals(-5f, draggable.getOffsetX(), 0.0001f);
        Assert.assertEquals(-6f, draggable.getOffsetY(), 0.0001f);
    }

    @Test
    public void testTouchUpApproveAddsRemoveActionsAndRestoresVisibility() {
        Stage stage = newHeadlessStage(800, 600);

        Actor actor = new Actor();
        actor.setSize(20, 10);
        stage.addActor(actor);

        Draggable draggable = new Draggable(new Draggable.DragAdapter());
        draggable.setInvisibleWhenDragged(true);
        draggable.setBlockInput(false);
        draggable.setFadingTime(0.5f);

        draggable.attachTo(actor);

        InputEvent down = new InputEvent();
        down.setStage(stage);
        down.setListenerActor(actor);
        down.setStageX(10);
        down.setStageY(20);
        Assert.assertTrue(draggable.touchDown(down, 1, 2, 0, 0));

        InputEvent up = new InputEvent();
        up.setStage(stage);
        up.setListenerActor(actor);
        up.setStageX(30);
        up.setStageY(40);
        draggable.touchUp(up, 1, 2, 0, 0);

        Assert.assertTrue(draggable.isDragged());
        Assert.assertEquals(1, actor.getActions().size);

        stage.act(1.0f);
        stage.act(0.0f);

        try {
            Field mimicField = Draggable.class.getDeclaredField("mimic");
            mimicField.setAccessible(true);
            Draggable.MimicActor mimic = (Draggable.MimicActor) mimicField.get(draggable);
            Assert.assertNull(mimic.getParent());
            Assert.assertNull(mimic.getActor());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(0, actor.getActions().size);
        Assert.assertTrue(actor.isVisible());
        Assert.assertFalse(draggable.isDragged());
    }

    @Test
    public void testKeepWithinParentClampsMimicPosition() {
        Stage stage = newHeadlessStage(800, 600);
        Table parent = new Table();
        parent.setSize(100, 100);
        stage.addActor(parent);

        Actor actor = new Actor();
        actor.setSize(20, 10);
        parent.addActor(actor);

        Draggable draggable = new Draggable(new Draggable.DragAdapter());
        draggable.setKeepWithinParent(true);
        draggable.setBlockInput(false);
        draggable.attachTo(actor);

        InputEvent down = new InputEvent();
        down.setStage(stage);
        down.setListenerActor(actor);
        down.setStageX(5);
        down.setStageY(5);
        Assert.assertTrue(draggable.touchDown(down, 0, 0, 0, 0));

        InputEvent drag = new InputEvent();
        drag.setStage(stage);
        drag.setListenerActor(actor);
        drag.setStageX(-100);
        drag.setStageY(-100);
        draggable.touchDragged(drag, 0, 0, 0);

        Actor mimic = stage.getActors().peek();
        Assert.assertTrue(mimic.getX() >= 0f);
        Assert.assertTrue(mimic.getY() >= 0f);

        InputEvent drag2 = new InputEvent();
        drag2.setStage(stage);
        drag2.setListenerActor(actor);
        drag2.setStageX(1000);
        drag2.setStageY(1000);
        draggable.touchDragged(drag2, 0, 0, 0);

        Assert.assertTrue(mimic.getX() <= 100f - mimic.getWidth() + 0.0001f);
        Assert.assertTrue(mimic.getY() <= 100f - mimic.getHeight() + 0.0001f);
    }
}
