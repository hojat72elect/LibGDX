package games.rednblack.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;

import games.rednblack.editor.event.SelectBoxChangeListener;
import games.rednblack.editor.view.ui.properties.UIItemCollapsibleProperties;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpineAnimationItemProperties extends UIItemCollapsibleProperties {

    private final VisSelectBox<String> animationsSelectBox;
    private final VisSelectBox<String> skinSelectBox;

    public UISpineAnimationItemProperties() {
        super("Spine Animations");
        animationsSelectBox = StandardWidgetsFactory.createSelectBox(String.class);
        skinSelectBox = StandardWidgetsFactory.createSelectBox(String.class);

        mainTable.add(StandardWidgetsFactory.createLabel("Animation:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(animationsSelectBox).width(120).colspan(2).row();

        mainTable.add().padTop(7).colspan(4).row();

        mainTable.add(StandardWidgetsFactory.createLabel("Skin:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(skinSelectBox).width(120).colspan(2).row();
        setListeners();
    }

    public Array<String> getAnimations() {
        return animationsSelectBox.getItems();
    }

    public void setAnimations(Array<String> animations) {
        animationsSelectBox.setItems(animations);
    }

    public Array<String> getSkins() {
        return skinSelectBox.getItems();
    }

    public void setSkins(Array<String> animations) {
        skinSelectBox.setItems(animations);
    }

    public String getSelectedAnimation() {
        return animationsSelectBox.getSelected();
    }

    public void setSelectedAnimation(String currentAnimationName) {
        animationsSelectBox.setSelected(currentAnimationName);
    }

    public String getSelectedSkin() {
        return skinSelectBox.getSelected();
    }

    public void setSelectedSkin(String currentSkinName) {
        skinSelectBox.setSelected(currentSkinName);
    }

    @Override
    public String getPrefix() {
        return this.getClass().getCanonicalName();
    }

    private void setListeners() {
        animationsSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
        skinSelectBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
    }
}
