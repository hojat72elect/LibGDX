package games.rednblack.editor.view.stage.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.h2d.common.proxy.CursorManager;
import games.rednblack.h2d.common.view.ui.Cursors;
import games.rednblack.puremvc.Facade;

public class PanTool extends SimpleTool {
    public static final String NAME = "PAN_TOOL";
    private static final String EVENT_PREFIX = "games.rednblack.editor.view.stage.tools.PanTool";
    public static final String SCENE_PANNED = EVENT_PREFIX + ".SCENE_PANNED";
    float currX, currY;
    private final Vector2 lastCoordinates = new Vector2();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortcut() {
        return null;
    }

    @Override
    public String getTitle() {
        return "Pan Tool";
    }

    @Override
    public void initTool() {
        CursorManager cursorManager = Facade.getInstance().retrieveProxy(CursorManager.NAME);
        cursorManager.setCursor(Cursors.HAND);
    }

    @Override
    public boolean stageMouseDown(float x, float y) {
        lastCoordinates.set(Gdx.input.getX(), Gdx.input.getY());
        currX = Sandbox.getInstance().getCamera().position.x;
        currY = Sandbox.getInstance().getCamera().position.y;
        return false;
    }

    @Override
    public void stageMouseUp(float x, float y) {

    }

    @Override
    public void stageMouseDragged(float x, float y) {
        doPanning(x, y);
    }

    @Override
    public boolean stageMouseScrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void stageMouseDoubleClick(float x, float y) {

    }

    @Override
    public boolean itemMouseDown(int entity, float x, float y) {
        lastCoordinates.set(Gdx.input.getX(), Gdx.input.getY());
        currX = Sandbox.getInstance().getCamera().position.x;
        currY = Sandbox.getInstance().getCamera().position.y;
        return true;
    }

    @Override
    public void itemMouseUp(int entity, float x, float y) {

    }

    @Override
    public void itemMouseDragged(int entity, float x, float y) {
        doPanning(x, y);
    }

    @Override
    public void itemMouseDoubleClick(int entity, float x, float y) {

    }

    private void doPanning(float x, float y) {
        ResourceManager resourceManager = Facade.getInstance().retrieveProxy(ResourceManager.NAME);

        Sandbox sandbox = Sandbox.getInstance();
        OrthographicCamera camera = sandbox.getCamera();

        currX += (lastCoordinates.x - Gdx.input.getX()) * camera.zoom / resourceManager.getProjectVO().pixelToWorld;
        currY += (Gdx.input.getY() - lastCoordinates.y) * camera.zoom / resourceManager.getProjectVO().pixelToWorld;

        sandbox.panSceneTo(currX, currY);

        lastCoordinates.set(Gdx.input.getX(), Gdx.input.getY());

        Facade.getInstance().sendNotification(SCENE_PANNED);
    }
}
