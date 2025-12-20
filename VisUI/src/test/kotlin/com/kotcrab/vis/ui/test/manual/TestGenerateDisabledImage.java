package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestGenerateDisabledImage extends VisWindow {
    public TestGenerateDisabledImage() {
        super("generate disabled image");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addVisWidgets();

        setSize(300, 150);
        centerWindow();
    }

    private void addVisWidgets() {
        Drawable icon = VisUI.getSkin().getDrawable("icon-folder");
        VisImageButton normal = new VisImageButton(icon);
        VisImageButton disabled = new VisImageButton(icon);
        disabled.setGenerateDisabledImage(true);
        disabled.setDisabled(true);
        add(new VisLabel("VisImageButton normal"));
        add(normal).row();
        add(new VisLabel("VisImageButton disabled"));
        add(disabled).row();

        VisImageTextButton normalText = new VisImageTextButton("text", icon);
        VisImageTextButton disabledText = new VisImageTextButton("text", icon);
        disabledText.setGenerateDisabledImage(true);
        disabledText.setDisabled(true);
        add(new VisLabel("VisImageTextButton normal"));
        add(normalText).row();
        add(new VisLabel("VisImageTextButton disabled"));
        add(disabledText).padBottom(3f).row();
    }
}
