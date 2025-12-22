package games.rednblack.h2d.common.view.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

/**
 * Created by azakhary on 7/8/2014.
 */
public class TintButton extends Group {

    private final Skin skin;
    private final Image colorImg;

    private final Color colorValue = new Color();

    public TintButton(int width, int height) {
        skin = VisUI.getSkin();
        colorImg = new Image(skin.getDrawable("white"));
        Image borderImg = new Image(skin.getDrawable("tint-border"));

        colorImg.setWidth(width - 2);
        colorImg.setHeight(height - 2);
        colorImg.setX(1);
        colorImg.setY(1);
        borderImg.setWidth(width);
        borderImg.setHeight(height);

        addActor(colorImg);
        addActor(borderImg);

        setWidth(borderImg.getWidth());
        setHeight(borderImg.getHeight());
    }

    public Color getColorValue() {
        return colorValue;
    }

    public void setColorValue(Color color) {
        colorImg.setColor(color);
        colorValue.set(color);
    }
}
