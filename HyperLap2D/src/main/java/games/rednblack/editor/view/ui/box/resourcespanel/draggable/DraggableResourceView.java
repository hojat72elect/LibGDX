package games.rednblack.editor.view.ui.box.resourcespanel.draggable;

import com.badlogic.gdx.scenes.scene2d.Actor;

import games.rednblack.h2d.common.ResourcePayloadObject;

public interface DraggableResourceView {
    Actor getDragActor();

    ResourcePayloadObject getPayloadData();
}
