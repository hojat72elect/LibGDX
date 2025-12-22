package games.rednblack.editor.view.ui.box.resourcespanel.draggable.list;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.h2d.common.ResourcePayloadObject;

public class LibraryItemResource extends ListItemResource {

    private final Image payloadImg;
    private final ResourcePayloadObject payload;
    private final String key;

    public LibraryItemResource(String key) {
        super(key, "library");
        this.key = key;
        payloadImg = new Image(getStyle().resourceOver) {
            @Override
            public void setScale(float scaleXY) {
                //Do not scale
            }
        };
        payloadImg.setScale(2);
        payloadImg.getColor().a = .85f;
        payload = new ResourcePayloadObject();
        payload.name = key;
        payload.className = getClass().getName();

        setRightClickEvent(UIResourcesBoxMediator.LIBRARY_ITEM_RIGHT_CLICK, payload.name);
    }

    public String getKey() {
        return key;
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
