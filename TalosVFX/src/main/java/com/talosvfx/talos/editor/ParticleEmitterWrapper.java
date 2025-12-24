
package com.talosvfx.talos.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.editor.widgets.ui.timeline.TimelineItemDataProvider;
import com.talosvfx.talos.runtime.vfx.ParticleEmitterDescriptor;

public class ParticleEmitterWrapper implements TimelineItemDataProvider<ParticleEmitterWrapper> {

    public boolean isMuted;
    private String emitterName = "";
    private boolean isSolo;
    private float position;

    private ParticleEmitterDescriptor moduleGraph;

    public ParticleEmitterDescriptor getGraph() {
        return moduleGraph;
    }

    public void setModuleGraph(ParticleEmitterDescriptor graph) {
        this.moduleGraph = graph;
    }

    public String getName() {
        return emitterName;
    }

    public void setName(String emitterName) {
        this.emitterName = emitterName;
    }

    public ParticleEmitterDescriptor getEmitter() {
        return moduleGraph;
    }

    @Override
    public Array<Button> registerSecondaryActionButtons() {
        return null;
    }

    @Override
    public Array<Button> registerMainActionButtons() {
        return null;
    }

    @Override
    public String getItemName() {
        return emitterName;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    @Override
    public ParticleEmitterWrapper getIdentifier() {
        return this;
    }

    @Override
    public int getIndex() {
        return getEmitter().getSortPosition();
    }

    @Override
    public boolean isFull() {
        if (getEmitter().getParticleModule() == null || getEmitter().getEmitterModule() == null) return false;

        return getEmitter().isContinuous();
    }

    @Override
    public float getDurationOne() {
        if (getEmitter().getParticleModule() == null || getEmitter().getEmitterModule() == null) return 0;

        if (getEmitter().getEffectDescriptor().isContinuous() && !getEmitter().isContinuous()) {
            // apparently if effect is continuous, non continuous effect currently don't play
            return 0;
        }

        return getEmitter().getEmitterModule().getDuration();
    }

    @Override
    public float getDurationTwo() {
        if (getEmitter().getParticleModule() == null || getEmitter().getEmitterModule() == null) return 0;

        if (getEmitter().getEffectDescriptor().isContinuous() && !getEmitter().isContinuous()) {
            // apparently if effect is continuous, non continuous effect currently don't play
            return 0;
        }

        if (getEmitter().isContinuous()) {
            return 0;
        }

        return getEmitter().getParticleModule().getLife();
    }

    @Override
    public float getTimePosition() {
        if (getEmitter().getParticleModule() == null || getEmitter().getEmitterModule() == null) return 0;

        return getEmitter().getEmitterModule().getDelay();
    }

    @Override
    public void setTimePosition(float time) {

    }

    @Override
    public boolean isItemVisible() {
        return !isMuted;
    }
}
