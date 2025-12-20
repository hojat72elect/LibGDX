package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class TestColorPicker extends VisWindow {
    private static final Drawable white = VisUI.getSkin().getDrawable("white");

    private final ColorPicker picker;

    public TestColorPicker() {
        super("color picker");

        final Image image = new Image(white);

        picker = new ColorPicker("color picker", new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
            }
        });

        VisTextButton showPickerButton = new VisTextButton("show color picker");
        showPickerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(picker.fadeIn());
            }
        });

        Color c = new Color(27 / 255.0f, 161 / 255.0f, 226 / 255.0f, 1);
        picker.setColor(c);
        image.setColor(c);

        TableUtils.setSpacingDefaults(this);

        add(showPickerButton);
        add(image).size(32).pad(3);

        pack();
        setPosition(948, 148);
    }

    @Override
    protected void close() {
        super.close();
        picker.dispose();
    }
}
