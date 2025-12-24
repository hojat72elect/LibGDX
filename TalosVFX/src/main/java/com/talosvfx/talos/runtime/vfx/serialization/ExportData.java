
package com.talosvfx.talos.runtime.vfx.serialization;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.assets.GameResourceOwner;
import com.talosvfx.talos.runtime.utils.Supplier;
import com.talosvfx.talos.runtime.vfx.ParticleEffectDescriptor;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;


public class ExportData extends BaseVFXProjectData implements Json.Serializable {

    public Array<EmitterExportData> emitters = new Array<>();
    public ExportMetadata metadata = new ExportMetadata();
    private transient ParticleEffectDescriptor particleEffectDescriptorLoaded;
    private final transient Supplier<ParticleEffectDescriptor> descriptorSupplier = new Supplier<ParticleEffectDescriptor>() {
        @Override
        public ParticleEffectDescriptor get() {
            return particleEffectDescriptorLoaded;
        }
    };

    public ExportData() {
        super();
    }

    @Override
    public Supplier<ParticleEffectDescriptor> getDescriptorSupplier() {
        return descriptorSupplier;
    }

    public void setDescriptorLoaded(ParticleEffectDescriptor particleEffectDescriptor) {
        this.particleEffectDescriptorLoaded = particleEffectDescriptor;
    }

    @Override
    public void write(Json json) {
        json.writeValue("metadata", metadata);
        json.writeValue("emitters", emitters);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        String talosIdentifier = GameResourceOwner.readTalosIdentifier(jsonData);
        metadata = json.readValue("metadata", ExportMetadata.class, jsonData);
        JsonValue emittersJsonValue = jsonData.get("emitters");
        for (int i = 0; i < emittersJsonValue.size; i++) {
            JsonValue emitterJsonValue = emittersJsonValue.get(i);
            emitterJsonValue.addChild("talosIdentifier", new JsonValue(talosIdentifier));
            emitters.add(json.readValue(EmitterExportData.class, emitterJsonValue));
        }
    }

    public static class ExportMetadata {
        public Array<String> resources = new Array<>();
        public String versionString;

        public ExportMetadata() {
        }
    }

    public static class EmitterExportData implements Json.Serializable {
        public String name;
        public Array<AbstractModule> modules = new Array<>();
        public Array<ConnectionData> connections = new Array<>();

        public EmitterExportData() {
        }

        @Override
        public String toString() {
            CharArray stringBuilder = new CharArray();

            stringBuilder.append(name);
            stringBuilder.append("\n");

            for (AbstractModule module : modules) {
                stringBuilder.append("\t");
                stringBuilder.append("ModuleID: ");
                stringBuilder.append(module.getIndex());
                stringBuilder.append("\n");
            }

            stringBuilder.append("\n");
            for (ConnectionData connection : connections) {
                stringBuilder.append("\t");
                stringBuilder.append("Connection: ");
                stringBuilder.append(connection);
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }

        @Override
        public void write(Json json) {
            json.writeValue("name", name);
            json.writeValue("modules", modules);
            json.writeValue("connections", connections);
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            String talosIdentifier = GameResourceOwner.readTalosIdentifier(jsonData);

            name = jsonData.getString("name");
            JsonValue modulesJsonValue = jsonData.get("modules");
            for (int i = 0; i < modulesJsonValue.size; i++) {
                JsonValue abstractModuleJson = modulesJsonValue.get(i);
                abstractModuleJson.addChild("talosIdentifier", new JsonValue(talosIdentifier));
                modules.add(json.readValue(AbstractModule.class, abstractModuleJson));
            }
            connections = json.readValue(Array.class, ConnectionData.class, jsonData.get("connections"));
        }
    }
}
