package games.rednblack.editor.renderer.scripts;

import games.rednblack.editor.renderer.scene2d.CompositeActor;


public interface IActorScript {
    void init(CompositeActor entity);

    void act(float delta);

    void dispose();
}
