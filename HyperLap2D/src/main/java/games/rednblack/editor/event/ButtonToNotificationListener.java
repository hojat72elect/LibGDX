package games.rednblack.editor.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import games.rednblack.puremvc.Facade;

public class ButtonToNotificationListener extends ClickListener {

    private final String notificationName;
    private Object payload;

    public ButtonToNotificationListener(String notificationName) {
        this.notificationName = notificationName;
    }

    public ButtonToNotificationListener(String notificationName, Object payload) {
        this.notificationName = notificationName;
        this.payload = payload;
    }

    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        Facade.getInstance().sendNotification(notificationName, payload);
    }
}
