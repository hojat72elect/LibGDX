package games.rednblack.editor.view.ui.followers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.VisUI;

import games.rednblack.editor.view.stage.Sandbox;

public class ParticleFollower extends BasicFollower {

    private Image icon;

    public ParticleFollower(int entity) {
        super(entity);
        setTransform(false);
    }

    @Override
    public void create() {
        icon = new Image(VisUI.getSkin().getDrawable("icon-particle-over"));
        icon.setTouchable(Touchable.disabled);
        addActor(icon);
    }

    @Override
    public void hide() {
        // Particle followers can't be hidden.
    }

    @Override
    public void update() {
        super.update();

        Sandbox sandbox = Sandbox.getInstance();
        OrthographicCamera camera = Sandbox.getInstance().getCamera();

        int pixelPerWU = sandbox.sceneControl.sceneLoader.getRm().getProjectVO().pixelToWorld;

        float scaleX = transformComponent.scaleX * (transformComponent.flipX ? -1 : 1);
        float scaleY = transformComponent.scaleY * (transformComponent.flipY ? -1 : 1);

        setWidth(pixelPerWU * dimensionsComponent.width * scaleX / camera.zoom);
        setHeight(pixelPerWU * dimensionsComponent.height * scaleY / camera.zoom);

        setX(getX() - getWidth() / 2f);
        setY(getY() - getHeight() / 2f);

        icon.setX((getWidth() - icon.getWidth()) / 2);
        icon.setY((getHeight() - icon.getHeight()) / 2);
    }
}
