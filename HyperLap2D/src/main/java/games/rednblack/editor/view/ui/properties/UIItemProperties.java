package games.rednblack.editor.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

public abstract class UIItemProperties extends UIAbstractProperties {

    final Skin skin;

    public UIItemProperties() {
        skin = VisUI.getSkin();
    }
}
