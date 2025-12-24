
package com.talosvfx.talos.editor.wrappers;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.VisUI;
import com.talosvfx.talos.editor.widgets.FloatRangeInputWidget;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.CheckboxWithZoom;
import com.talosvfx.talos.runtime.vfx.modules.RandomRangeModule;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class RandomRangeModuleWrapper extends ModuleWrapper<RandomRangeModule> {

    FloatRangeInputWidget inputRange;
    private CheckboxWithZoom distribution;

    @Override
    public void attachModuleToMyOutput(ModuleWrapper moduleWrapper, int mySlot, int targetSlot) {
        super.attachModuleToMyOutput(moduleWrapper, mySlot, targetSlot);

        inputRange.setFlavour(module.getOutputValue().getFlavour());
    }

    @Override
    public void setSlotInactive(int slotTo, boolean isInput) {
        super.setSlotInactive(slotTo, isInput);
        if (!isInput) {
            inputRange.setFlavour(NumericalValue.Flavour.REGULAR);
            inputRange.setText("Min", "Max");
        }
    }

    @Override
    protected float reportPrefWidth() {
        return 190;
    }

    @Override
    protected void configureSlots() {

        addInputSlot("min", RandomRangeModule.MIN_INPUT);
        addInputSlot("max", RandomRangeModule.MAX_INPUT);

        addOutputSlot("result", 0);

        distribution = new CheckboxWithZoom("distributed", VisUI.getSkin());
        rightWrapper.add(distribution).right().expandX().padRight(3).row();

        inputRange = new FloatRangeInputWidget("Min", "Max", getSkin());
        inputRange.setValue(1, 1);
        contentWrapper.add(inputRange).left().padTop(40).padLeft(4).expandX();

        leftWrapper.add(new Table()).expandY();
        rightWrapper.add(new Table()).expandY();

        inputRange.setListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateValues();
            }
        });

        distribution.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                module.setDistributed(distribution.isChecked());
            }
        });
    }

    private void updateValues() {
        float min = inputRange.getMinValue();
        float max = inputRange.getMaxValue();

        module.setMinMax(min, max);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setData(module.getMin(), module.getMax());

        inputRange.getEqualsButton().setChecked(jsonData.getBoolean("equals", true));
        inputRange.getMirrorButton().setChecked(jsonData.getBoolean("mirror", false));
    }

    @Override
    public void write(Json json) {
        super.write(json);

        json.writeValue("mirror", inputRange.getMirrorButton().isChecked());
        json.writeValue("equals", inputRange.getEqualsButton().isChecked());
    }

    public void setData(float min, float max) {
        inputRange.setValue(min, max);
        updateValues();
        distribution.setChecked(module.isDistributed());
    }
}
