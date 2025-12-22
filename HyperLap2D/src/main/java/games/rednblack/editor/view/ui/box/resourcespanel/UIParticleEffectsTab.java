package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;

public class UIParticleEffectsTab extends UIResourcesTab {

    private VisTable list;

    @Override
    protected VisScrollPane crateScrollPane() {
        list = new VisTable();
        return StandardWidgetsFactory.createScrollPane(list);
    }

    @Override
    public String getTabTitle() {
        return "Particles";
    }

    @Override
    public String getTabIconStyle() {
        return "particle-button";
    }

    public void setItems(Array<DraggableResource> items) {
        list.clearChildren();
        for (DraggableResource box : items) {
            box.initDragDrop();
            list.add((Actor) box.getViewComponent()).expandX().fillX();
            list.row();
        }
    }
}
