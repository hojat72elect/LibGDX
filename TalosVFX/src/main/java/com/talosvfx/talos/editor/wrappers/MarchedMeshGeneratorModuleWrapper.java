
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.editor.widgets.FloatInputWidget;
import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.MarchedMeshGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.StaticValueModule;

public class MarchedMeshGeneratorModuleWrapper extends ModuleWrapper<MarchedMeshGeneratorModule> {

    FloatInputWidget scale;

    public MarchedMeshGeneratorModuleWrapper() {
        super();
    }

    @Override
    public void setModule(MarchedMeshGeneratorModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        addInputSlot("radius", MarchedMeshGeneratorModule.RADIUS);
        addOutputSlot("marched", MarchedMeshGeneratorModule.MODULE);

        scale = new FloatInputWidget("scale", getSkin());
        scale.setValue(1f);
        scale.setListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                module.setScale(scale.getValue());
            }
        });

        leftWrapper.add(scale).left().expandX().padLeft(3).row();
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {
        if (slot.getIndex() == MarchedMeshGeneratorModule.RADIUS) return StaticValueModule.class;

        return null;
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
    }

    @Override
    public void write(Json json) {
        super.write(json);
    }
}
