
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.editor.widgets.FloatInputWidget;
import com.talosvfx.talos.runtime.vfx.modules.StaticValueModule;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class StaticValueModuleWrapper extends ModuleWrapper<StaticValueModule> {

    private FloatInputWidget floatInput;

    public StaticValueModuleWrapper() {
        super();
    }

    @Override
    public void setModule(StaticValueModule module) {
        super.setModule(module);
        floatInput.setValue(module.getStaticValue());
    }

    @Override
    public void attachModuleToMyOutput(ModuleWrapper moduleWrapper, int mySlot, int targetSlot) {
        super.attachModuleToMyOutput(moduleWrapper, mySlot, targetSlot);

        floatInput.setFlavour(module.getOutputValue().getFlavour());
    }

    @Override
    public void setSlotInactive(int slotTo, boolean isInput) {
        super.setSlotInactive(slotTo, isInput);
        if (!isInput) {
            floatInput.setFlavour(NumericalValue.Flavour.REGULAR);
            floatInput.setText("Number");
        }
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        floatInput = new FloatInputWidget("Number", getSkin());

        contentWrapper.add(floatInput).left().padLeft(4);
        contentWrapper.add().expandX();


        addOutputSlot("output", 0);


        floatInput.setListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = floatInput.getValue();
                module.setStaticValue(value);
            }
        });
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        floatInput.setValue(module.getStaticValue());
    }

    public void setValue(int val) {
        floatInput.setValue(val);
        module.setStaticValue(val);
    }
}
