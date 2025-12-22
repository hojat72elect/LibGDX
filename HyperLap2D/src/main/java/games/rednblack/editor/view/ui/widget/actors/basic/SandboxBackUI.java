package games.rednblack.editor.view.ui.widget.actors.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.view.ui.widget.actors.GridView;
import games.rednblack.puremvc.Facade;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * Created by azakhary on 6/10/2015.
 */
public class SandboxBackUI {

    private final Array<Actor> actors = new Array<>();
    private final Batch batch;

    public SandboxBackUI(Batch batch) {
        this.batch = batch;
        ShapeDrawer shapeDrawer = new ShapeDrawer(batch, WhitePixel.sharedInstance.textureRegion);

        GridView gridView = new GridView(shapeDrawer);
        addActor(gridView);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void render(float delta) {
        ResourceManager resourceManager = Facade.getInstance().retrieveProxy(ResourceManager.NAME);
        batch.begin();
        for (Actor actor : actors) {
            actor.setScale(1f / resourceManager.getProjectVO().pixelToWorld);
            actor.act(delta);
            actor.draw(batch, 1);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }
}
