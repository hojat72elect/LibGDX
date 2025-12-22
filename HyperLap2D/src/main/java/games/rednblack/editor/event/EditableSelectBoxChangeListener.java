package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import games.rednblack.editor.view.ui.widget.EditableSelectBox;
import games.rednblack.puremvc.Facade;

public class EditableSelectBoxChangeListener extends ChangeListener {

    private final String eventName;

    private String lastSelected = "";

    public EditableSelectBoxChangeListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void changed(ChangeEvent changeEvent, Actor actor) {
        Facade facade = Facade.getInstance();
        String selected = ((EditableSelectBox) actor).getSelected();
        if (!lastSelected.equals(selected)) {
            lastSelected = selected;
            facade.sendNotification(eventName, selected);
        }
    }
}