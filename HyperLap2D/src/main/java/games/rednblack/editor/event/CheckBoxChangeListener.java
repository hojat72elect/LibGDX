package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;

import games.rednblack.puremvc.Facade;

public class CheckBoxChangeListener extends ChangeListener {

    private final String eventName, type;

    public CheckBoxChangeListener(String eventName) {
        this(eventName, null);
    }

    public CheckBoxChangeListener(String eventName, String type) {
        this.eventName = eventName;
        this.type = type;
    }

    @Override
    public void changed(ChangeEvent changeEvent, Actor actor) {
        Facade facade = Facade.getInstance();
        facade.sendNotification(eventName, ((VisCheckBox) actor).isChecked(), type);
    }
}