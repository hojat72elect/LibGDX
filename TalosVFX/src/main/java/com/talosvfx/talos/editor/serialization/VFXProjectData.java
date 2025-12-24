
package com.talosvfx.talos.editor.serialization;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.talosvfx.talos.editor.ParticleEmitterWrapper;
import com.talosvfx.talos.editor.data.ModuleWrapperGroup;
import com.talosvfx.talos.editor.widgets.ui.ModuleBoardWidget;
import com.talosvfx.talos.editor.wrappers.ModuleWrapper;
import com.talosvfx.talos.runtime.utils.Supplier;
import com.talosvfx.talos.runtime.vfx.ParticleEffectDescriptor;
import com.talosvfx.talos.runtime.vfx.serialization.BaseVFXProjectData;
import com.talosvfx.talos.runtime.vfx.serialization.ConnectionData;

import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;


public class VFXProjectData extends BaseVFXProjectData implements Json.Serializable {


    private final Array<EmitterData> emitters = new Array<>();

    @Getter
    private final transient VFXEditorState editorState = new VFXEditorState();
    @Setter
    private transient ParticleEffectDescriptor descriptor;
    @Getter
    private final transient Supplier<ParticleEffectDescriptor> descriptorSupplier = new Supplier<ParticleEffectDescriptor>() {
        @Override
        public ParticleEffectDescriptor get() {
            return descriptor;
        }
    };

    public VFXProjectData() {

    }

    public void setFrom(ModuleBoardWidget moduleBoardWidget) {
        final ObjectMap<ParticleEmitterWrapper, Array<ModuleWrapper>> moduleWrappers = moduleBoardWidget.moduleWrappers;
        final ObjectMap<ParticleEmitterWrapper, Array<ModuleBoardWidget.NodeConnection>> nodeConnections = moduleBoardWidget.nodeConnections;

        emitters.clear();

        for (ParticleEmitterWrapper key : moduleWrappers.keys()) {
            final EmitterData emitterData = new EmitterData();
            emitterData.name = key.getName();
            emitterData.sortPosition = key.getEmitter().getSortPosition();
            emitterData.modules.addAll(moduleWrappers.get(key));
            emitterData.isMuted = key.isMuted;

            final Array<ModuleBoardWidget.NodeConnection> nodeConns = nodeConnections.get(key);
            if (nodeConns != null) {
                for (ModuleBoardWidget.NodeConnection nodeConn : nodeConns) {
                    emitterData.connections.add(new ConnectionData(nodeConn.fromModule.getId(), nodeConn.toModule.getId(), nodeConn.fromSlot, nodeConn.toSlot));
                }
            }

            // add groups
            for (ModuleWrapperGroup group : moduleBoardWidget.getGroups(key)) {
                GroupData groupData = new GroupData();
                groupData.text = group.getText();
                groupData.modules = new Array<>();
                groupData.color = group.getFrameColor().toFloatBits();
                for (ModuleWrapper wrapper : group.getModuleWrappers()) {
                    groupData.modules.add(wrapper.getId());
                }
                emitterData.groups.add(groupData);
            }

            emitters.add(emitterData);
        }
    }

    public Array<EmitterData> getEmitters() {
        return emitters;
    }

    @Override
    public void write(Json json) {
        emitters.sort(new Comparator<EmitterData>() {
            @Override
            public int compare(EmitterData o1, EmitterData o2) {
                return o1.sortPosition - o2.sortPosition;
            }
        });
        json.writeValue("emitters", emitters);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        String talosIdentifier = jsonData.getString("talosIdentifier", "default");

        JsonValue emittersJson = jsonData.get("emitters");
        for (int i = 0; i < emittersJson.size; i++) {
            JsonValue emitterJsonValue = emittersJson.get(i);
            emitterJsonValue.addChild("talosIdentifier", new JsonValue(talosIdentifier));
            EmitterData value = json.readValue(EmitterData.class, emitterJsonValue);
            emitters.add(value);
        }
    }
}
