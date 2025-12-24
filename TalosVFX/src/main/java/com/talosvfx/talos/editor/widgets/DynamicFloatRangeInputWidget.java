
package com.talosvfx.talos.editor.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class DynamicFloatRangeInputWidget extends Table {

    private final CurveWidget curveWidget;

    private final FloatRangeInputWidget lowInput;
    private final FloatRangeInputWidget highInput;

    public DynamicFloatRangeInputWidget(Skin skin) {
        setSkin(skin);

        Table container = new Table();

        highInput = new FloatRangeInputWidget("HMin", "HMax", getSkin());
        lowInput = new FloatRangeInputWidget("LMin", "LMax", getSkin());

        lowInput.setValue(0, 0);
        highInput.setValue(1, 1);

        container.add(highInput).row();
        container.add().height(3).row();
        container.add(lowInput);

        add(container).left().expandX();

        curveWidget = new CurveWidget(getSkin());
        add(curveWidget).left().growY().width(100).padTop(23).padRight(3).padLeft(4).padBottom(3);
    }

    public void setFlavour(NumericalValue.Flavour flavour) {
        lowInput.setFlavour(flavour);
        highInput.setFlavour(flavour);
    }
}
