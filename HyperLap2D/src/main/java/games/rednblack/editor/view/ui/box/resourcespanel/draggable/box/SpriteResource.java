package games.rednblack.editor.view.ui.box.resourcespanel.draggable.box;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import games.rednblack.editor.renderer.data.SpriteAnimationVO;
import games.rednblack.editor.renderer.resources.IResourceRetriever;
import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.editor.view.ui.widget.actors.SpriteAnimationActor;
import games.rednblack.h2d.common.ResourcePayloadObject;

public class SpriteResource extends BoxItemResource {
    private final SpriteAnimationActor payloadActor;
    private final ResourcePayloadObject payload;

    private boolean isMouseInside = false;

    public SpriteResource(String animationName) {
        // this is not changing the behavior of the former constructor
        // as long as the colors of the super class are not changed
        this(animationName, false);
    }

    /**
     * Creates a new sprite resource from the given animation name.
     *
     * @param animationName          The of the animation for the sprite resource.
     * @param highlightWhenMouseOver Whether to change the border color when the mouse hovers over the image.
     */
    public SpriteResource(String animationName, boolean highlightWhenMouseOver) {
        super(highlightWhenMouseOver);
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animationName;

        IResourceRetriever rm = sandbox.getSceneControl().sceneLoader.getRm();

        SpriteAnimationActor animThumb = new SpriteAnimationActor(animationName, rm);
        animThumb.setFPS(vo.fps);

        if (animThumb.getWidth() > thumbnailSize || animThumb.getHeight() > thumbnailSize) {
            // resizing is needed
            float scaleFactor = 1.0f;
            if (animThumb.getWidth() > animThumb.getHeight()) {
                //scale by width
                scaleFactor = 1.0f / (animThumb.getWidth() / thumbnailSize);
            } else {
                scaleFactor = 1.0f / (animThumb.getHeight() / thumbnailSize);
            }
            animThumb.setScale(scaleFactor);
        }

        // put it in middle
        animThumb.setX((getWidth() - animThumb.getWidth() * animThumb.getScaleX()) / 2f);
        animThumb.setY((getHeight() - animThumb.getHeight() * animThumb.getScaleY()) / 2f);
        animThumb.setPaused(false);


        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isMouseInside = true;
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isMouseInside = false;
                super.enter(event, x, y, pointer, toActor);
            }
        });

        addActor(animThumb);

        payloadActor = new SpriteAnimationActor(animationName, rm);
        payload = new ResourcePayloadObject();
        payload.name = animationName;
        payload.className = getClass().getName();

        setRightClickEvent(UIResourcesBoxMediator.SPRITE_ANIMATION_RIGHT_CLICK, payload.name);
    }


    @Override
    public void act(float delta) {
        if (isMouseInside) {
            super.act(delta);
        }
    }

    @Override
    public Actor getDragActor() {
        return payloadActor;
    }

    @Override
    public ResourcePayloadObject getPayloadData() {
        return payload;
    }
}
