
package com.talosvfx.talos.runtime.vfx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.talosvfx.talos.runtime.assets.BaseAssetRepository;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.DrawableModule;
import com.talosvfx.talos.runtime.vfx.modules.EmitterModule;
import com.talosvfx.talos.runtime.vfx.modules.ParticleModule;
import com.talosvfx.talos.runtime.vfx.serialization.ConnectionData;
import com.talosvfx.talos.runtime.vfx.serialization.ExportData;

public class ParticleEffectDescriptor {

    /**
     * graph per each emitter
     */
    public Array<ParticleEmitterDescriptor> emitterModuleGraphs = new Array<>();

    private ParticleEffectInstance processsingEffectReference;

    public ParticleEffectDescriptor() {

    }

    public ParticleEffectDescriptor(FileHandle fileHandle, BaseAssetRepository assetRepository) {
        load(fileHandle, assetRepository.getTalosContext().getIdentifier());
    }

    public static ExportData getExportData(FileHandle fileHandle, String talosIdentifier) {
        Json json = new Json();
        ParticleEmitterDescriptor.registerModules();
        for (Class clazz : ParticleEmitterDescriptor.registeredModules) {
            json.addClassTag(clazz.getSimpleName(), clazz);
        }

        JsonValue jsonValue = new JsonReader().parse(fileHandle.readString());
        jsonValue.addChild("talosIdentifier", new JsonValue(talosIdentifier));
        final ExportData exportData = json.readValue(ExportData.class, jsonValue);
        return exportData;
    }

    public void addEmitter(ParticleEmitterDescriptor emitter) {
        emitterModuleGraphs.add(emitter);
    }

    public void removeEmitter(ParticleEmitterDescriptor emitter) {
        emitterModuleGraphs.removeValue(emitter, true);
    }

    public ParticleEmitterDescriptor createEmitterDescriptor() {
        return new ParticleEmitterDescriptor(this);
    }

    public void load(FileHandle fileHandle, String talosIdentifier) {
        final ExportData exportData = getExportData(fileHandle, talosIdentifier);
        load(exportData);
    }

    public void load(ExportData exportData) {

        if (exportData.metadata.versionString == null) {
            exportData.metadata.versionString = "1.4.0"; //Default for unknown versions
        }

        for (ExportData.EmitterExportData emitter : exportData.emitters) {
            ParticleEmitterDescriptor emitterDescriptor = new ParticleEmitterDescriptor(this);

            IntMap<AbstractModule> idMap = new IntMap<>();

            for (int i = 0; i < emitter.modules.size; i++) {
                AbstractModule module = emitter.modules.get(i);

                module.setModuleGraph(emitterDescriptor);
                if (module instanceof ParticleModule) {
                    emitterDescriptor.particleModule = (ParticleModule) module;
                }
                if (module instanceof EmitterModule) {
                    emitterDescriptor.emitterModule = (EmitterModule) module;
                }
                if (module instanceof DrawableModule) {
                    emitterDescriptor.drawableModule = (DrawableModule) module;
                }
                idMap.put(module.getIndex(), module);
                emitterDescriptor.modules.add(module); // I cannot understand how this was working before. This is needed so that it can later reset requesters.
            }

            for (int i = 0; i < emitter.connections.size; i++) {
                ConnectionData connection = emitter.connections.get(i);

                final int moduleFromId = connection.moduleFrom;
                final int moduleToId = connection.moduleTo;
                final int slotFrom = connection.slotFrom;
                final int slotTo = connection.slotTo;

                AbstractModule moduleFrom = idMap.get(moduleFromId);
                AbstractModule moduleTo = idMap.get(moduleToId);

                if (moduleFrom == null) {
                    throw new GdxRuntimeException("No module from found for id: " + moduleFromId);
                }
                if (moduleTo == null) {
                    throw new GdxRuntimeException("No module to found for id: " + moduleToId);
                }

                emitterDescriptor.connectNode(moduleFrom, moduleTo, slotFrom, slotTo);
            }

            emitterModuleGraphs.add(emitterDescriptor);
        }
    }

    public ParticleEffectInstance createEffectInstance() {
        ParticleEffectInstance particleEffectInstance = new ParticleEffectInstance(this);
        setEffectReference(particleEffectInstance);

        for (ParticleEmitterDescriptor emitterDescriptor : emitterModuleGraphs) {
            particleEffectInstance.addEmitter(emitterDescriptor);
        }

        particleEffectInstance.sortEmitters();

        // create default scope
        particleEffectInstance.setScope(new ScopePayload());

        return particleEffectInstance;
    }

    public boolean isContinuous() {
        for (ParticleEmitterDescriptor emitterDescriptor : emitterModuleGraphs) {
            if (emitterDescriptor.getEmitterModule() == null || emitterDescriptor.getParticleModule() == null) {
                return false;
            }
            if (getInstanceReference() == null) {
                return false;
            }
            if (emitterDescriptor.isContinuous()) {
                return true;
            }
        }

        return false;
    }

    public void setEffectReference(ParticleEffectInstance particleEffectInstance) {
        processsingEffectReference = particleEffectInstance;
    }

    public ParticleEffectInstance getInstanceReference() {
        return processsingEffectReference;
    }

    public boolean different(ParticleEffectDescriptor descriptor) {
        if (this != descriptor)
            return true;
        return this.emitterModuleGraphs.size != descriptor.emitterModuleGraphs.size;
    }
}
