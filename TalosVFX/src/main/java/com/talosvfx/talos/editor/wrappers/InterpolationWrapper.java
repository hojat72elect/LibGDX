
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.InputModule;
import com.talosvfx.talos.runtime.vfx.modules.InterpolationModule;
import com.talosvfx.talos.runtime.vfx.utils.InterpolationMappings;

public class InterpolationWrapper extends ModuleWrapper<InterpolationModule> {


    SelectBox<String> selectBox;

    public InterpolationWrapper() {
        super();
    }

    @Override
    protected float reportPrefWidth() {
        return 250;
    }

    @Override
    protected void configureSlots() {

        addInputSlot("alpha (0 to 1)", InterpolationModule.ALPHA);

        addOutputSlot("output", 0);

        Array<String> interps = new Array<>();
        InterpolationMappings.getAvailableInterpolations(interps);

        selectBox = addSelectBox(interps);

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedString = selectBox.getSelected();

                Interpolation interp = InterpolationMappings.getInterpolationForName(selectedString);

                module.setInterpolation(interp);
            }
        });
    }


    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {

        if (slot.getIndex() == InterpolationModule.ALPHA) return InputModule.class;
        return null;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        selectBox.setSelected(InterpolationMappings.getNameForInterpolation(module.getInterpolation()));
    }
}
