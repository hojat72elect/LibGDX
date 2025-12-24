
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.SelectBoxWithZoom;
import com.talosvfx.talos.runtime.vfx.ScopePayload;
import com.talosvfx.talos.runtime.vfx.modules.InputModule;

public class InputModuleWrapper extends ModuleWrapper<InputModule> {

    IntMap<String> map;

    SelectBoxWithZoom<String> selectBox;

    public InputModuleWrapper() {
        super();
    }

    @Override
    public void setModule(InputModule module) {
        super.setModule(module);
        //module.setInput(ScopePayload.EMITTER_ALPHA);
    }

    @Override
    protected float reportPrefWidth() {
        return 280;
    }

    @Override
    protected void configureSlots() {
        map = new IntMap<>();
        map.put(ScopePayload.EMITTER_ALPHA, "Emitter.alpha - Duration");
        map.put(ScopePayload.PARTICLE_ALPHA, "Particle.alpha - Lifetime");
        map.put(ScopePayload.EMITTER_ALPHA_AT_P_INIT, "Duration at particle init");
        map.put(ScopePayload.SECONDARY_SEED, "Primary Seed");
        map.put(ScopePayload.PARTICLE_SEED, "Secondary Seed");
        map.put(ScopePayload.PARTICLE_POSITION, "Particle position");
        map.put(ScopePayload.TOTAL_TIME, "Global Time");
        map.put(ScopePayload.SUB_PARTICLE_ALPHA, "Sub Particle Index");


        selectBox = addSelectBox(map.values());
        addOutputSlot("output", 0);


        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedString = selectBox.getSelected();
                int key = map.findKey(selectedString, false, 0);

                module.setInput(key);
            }
        });
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setKey(module.getInput());
    }

    public void setKey(int key) {
        selectBox.setSelected(map.get(key));
        module.setInput(key);
    }
}
