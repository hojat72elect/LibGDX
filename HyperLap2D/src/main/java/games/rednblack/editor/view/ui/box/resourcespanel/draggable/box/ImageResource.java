package games.rednblack.editor.view.ui.box.resourcespanel.draggable.box;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.h2d.common.ResourcePayloadObject;

public class ImageResource extends BoxItemResource {
    private final Image payloadImg;
    private final ResourcePayloadObject payload;

    public ImageResource(AtlasRegion region) {
        // this is not changing the behavior of the former constructor
        // as long as the colors of the super class are not changed
        this(region, false);
    }

    /**
     * Creates a new image resource from the given {@link AtlasRegion}.
     *
     * @param region                 The atlas region for the image resource.
     * @param highlightWhenMouseOver Whether to change the border color when the mouse hovers over the image.
     */
    public ImageResource(AtlasRegion region, boolean highlightWhenMouseOver) {
        super(highlightWhenMouseOver);

        Image img = new Image(region);
        if (img.getWidth() > thumbnailSize || img.getHeight() > thumbnailSize) {
            // resizing is needed
            float scaleFactor = 1.0f;
            if (img.getWidth() > img.getHeight()) {
                //scale by width
                scaleFactor = 1.0f / (img.getWidth() / thumbnailSize);
            } else {
                scaleFactor = 1.0f / (img.getHeight() / thumbnailSize);
            }
            img.setScale(scaleFactor);

            img.setX((getWidth() - img.getWidth() * img.getScaleX()) / 2);
            img.setY((getHeight() - img.getHeight() * img.getScaleY()) / 2);
        } else {
            // put it in middle
            img.setX((getWidth() - img.getWidth()) / 2);
            img.setY((getHeight() - img.getHeight()) / 2);
        }

        addActor(img);

        setRightClickEvent(UIResourcesBoxMediator.IMAGE_RIGHT_CLICK, region.name);

        payloadImg = new Image(region);
        payload = new ResourcePayloadObject();
        payload.name = region.name;
        payload.className = getClass().getName();
    }

    @Override
    public Actor getDragActor() {
        return payloadImg;
    }

    @Override
    public ResourcePayloadObject getPayloadData() {
        return payload;
    }
}
