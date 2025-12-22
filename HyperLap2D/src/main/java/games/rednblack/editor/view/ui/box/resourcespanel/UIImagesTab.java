package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import games.rednblack.puremvc.Facade;

public class UIImagesTab extends UIResourcesTab {

    private VisTable imagesTable;

    public UIImagesTab() {
        super();
    }

    @Override
    protected VisScrollPane crateScrollPane() {
        imagesTable = new VisTable();
        Facade.getInstance().sendNotification(UIResourcesBoxMediator.ADD_RESOURCES_BOX_TABLE_SELECTION_MANAGEMENT, imagesTable);
        return StandardWidgetsFactory.createScrollPane(imagesTable);
    }

    @Override
    public String getTabTitle() {
        return "Images";
    }

    @Override
    public String getTabIconStyle() {
        return "image-button";
    }

    public void setThumbnailBoxes(Array<DraggableResource> draggableResources) {
        imagesTable.clearChildren();
        for (int i = 0; i < draggableResources.size; i++) {
            DraggableResource draggableResource = draggableResources.get(i);

            imagesTable.add((Actor) draggableResource.getViewComponent()).padRight(5).padBottom(5);
            if ((i - 7) % 4 == 0) {
                imagesTable.row();
            }
        }
    }
}
