package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import games.rednblack.puremvc.Facade;

public class ClickNotifier extends ClickListener {

    private final String eventName;

    public ClickNotifier(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Facade facade = Facade.getInstance();
        facade.sendNotification(eventName);
    }
}
