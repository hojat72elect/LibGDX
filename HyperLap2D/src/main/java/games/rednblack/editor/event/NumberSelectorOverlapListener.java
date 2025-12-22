package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import games.rednblack.puremvc.Facade;

public class NumberSelectorOverlapListener extends ChangeListener {

    private final String eventName;

    public NumberSelectorOverlapListener(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Facade facade = Facade.getInstance();
        facade.sendNotification(eventName, ((Spinner) actor).getTextField().getText());
    }
}
