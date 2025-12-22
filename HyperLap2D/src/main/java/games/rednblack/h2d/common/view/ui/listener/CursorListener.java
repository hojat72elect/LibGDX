package games.rednblack.h2d.common.view.ui.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import games.rednblack.h2d.common.proxy.CursorManager;
import games.rednblack.h2d.common.vo.CursorData;
import games.rednblack.puremvc.Facade;

public class CursorListener extends InputListener {
    private final CursorData cursor;
    private final CursorManager cursorManager;

    public CursorListener(CursorData cursor, Facade facade) {
        this.cursor = cursor;
        cursorManager = facade.retrieveProxy(CursorManager.NAME);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer == -1) {
            cursorManager.setOverrideCursor(cursor);
            cursorManager.displayCustomCursor();
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer == -1) {
            cursorManager.removeOverrideCursor();
            cursorManager.hideCustomCursor();
        }
    }
}