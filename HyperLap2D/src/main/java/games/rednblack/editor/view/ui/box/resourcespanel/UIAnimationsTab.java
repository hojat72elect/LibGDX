package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import games.rednblack.puremvc.Facade;

public class UIAnimationsTab extends UIResourcesTab {
    private VisTable animationsTable;

    @Override
    public String getTabTitle() {
        return "Animations";
    }

    @Override
    public String getTabIconStyle() {
        return "animation-button";
    }

    public void setThumbnailBoxes(Array<DraggableResource> draggableResources) {
        animationsTable.clearChildren();
        for (int i = 0; i < draggableResources.size; i++) {
            DraggableResource draggableResource = draggableResources.get(i);
            animationsTable.add((Actor) draggableResource.getViewComponent()).pad(4);
            if ((i - 7) % 4 == 0) {
                animationsTable.row();
            }
        }
    }

    @Override
    protected VisScrollPane crateScrollPane() {
        animationsTable = new VisTable();
        Facade.getInstance().sendNotification(UIResourcesBoxMediator.ADD_RESOURCES_BOX_TABLE_SELECTION_MANAGEMENT, animationsTable);
        VisScrollPane scrollPane = StandardWidgetsFactory.createScrollPane(animationsTable);
        scrollPane.setScrollingDisabled(true, false);
        return scrollPane;
    }
}
