package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisSelectBox;

import games.rednblack.puremvc.Facade;

public class SelectBoxChangeListener extends ChangeListener {

    private final String eventName;

    private String lastSelected = "";

    public SelectBoxChangeListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void changed(ChangeEvent changeEvent, Actor actor) {
        Facade facade = Facade.getInstance();
        String selected = (String) ((VisSelectBox) actor).getSelected();
        if (!lastSelected.equals(selected)) {
            lastSelected = selected;
            facade.sendNotification(eventName, selected);
        }
    }
}
