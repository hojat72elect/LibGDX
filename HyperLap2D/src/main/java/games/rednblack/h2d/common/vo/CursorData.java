package games.rednblack.h2d.common.vo;

import com.badlogic.gdx.graphics.Cursor;

/**
 * Created by azakhary on 5/15/2015.
 */
public class CursorData {

    public String region;
    public int hotspotX;
    public int hotspotY;
    public Cursor.SystemCursor systemCursor;

    public CursorData(String region, int x, int y) {
        this.region = region;
        hotspotX = x;
        hotspotY = y;
    }

    public CursorData(Cursor.SystemCursor cursor) {
        systemCursor = cursor;
    }

    public int getHotspotX() {
        return hotspotX;
    }

    public int getHotspotY() {
        return hotspotY;
    }
}
