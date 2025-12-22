package games.rednblack.h2d.common.view.tools;

import games.rednblack.puremvc.interfaces.INotification;

public interface Tool {
    void initTool();

    boolean stageMouseDown(float x, float y);

    void stageMouseUp(float x, float y);

    void stageMouseDragged(float x, float y);

    void stageMouseDoubleClick(float x, float y);

    boolean stageMouseScrolled(float amountX, float amountY);

    boolean itemMouseDown(int entity, float x, float y);

    void itemMouseUp(int entity, float x, float y);

    void itemMouseDragged(int entity, float x, float y);

    void itemMouseDoubleClick(int entity, float x, float y);

    String getName();

    String getTitle();

    String getShortcut();

    void handleNotification(INotification notification);

    void keyDown(int entity, int keycode);

    void keyUp(int entity, int keycode);
}
