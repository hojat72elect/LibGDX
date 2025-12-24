
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.VisUI;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.CheckboxWithZoom;
import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.QuadMeshGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.Vector2Module;

public class QuadMeshGeneratorModuleWrapper extends ModuleWrapper<QuadMeshGeneratorModule> {

    private CheckboxWithZoom billboard;

    public QuadMeshGeneratorModuleWrapper() {
        super();
    }

    @Override
    public void setModule(QuadMeshGeneratorModule module) {
        super.setModule(module);
    }

    @Override
    protected float reportPrefWidth() {
        return 150;
    }

    @Override
    protected void configureSlots() {
        addInputSlot("size", QuadMeshGeneratorModule.SIZE);
        addOutputSlot("quad", QuadMeshGeneratorModule.MODULE);

        billboard = new CheckboxWithZoom("billboard", VisUI.getSkin());
        billboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                module.setBillboard(billboard.isChecked());
            }
        });

        leftWrapper.add(billboard).left().expandX().padLeft(3).row();
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {
        if (slot.getIndex() == QuadMeshGeneratorModule.SIZE) return Vector2Module.class;

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
