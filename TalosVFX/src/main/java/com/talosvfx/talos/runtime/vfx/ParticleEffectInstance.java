
package com.talosvfx.talos.runtime.vfx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.runtime.vfx.render.ParticleRenderer;

import java.util.Comparator;

import lombok.Getter;

public class ParticleEffectInstance {

    private final ParticleEffectDescriptor descriptor;
    public boolean loopable = false;
    public int nodeCalls = 0;
    public float alpha = 1f;
    Vector3 position = new Vector3();
    @Getter
    float worldRotation;
    @Getter
    Vector2 worldScale = new Vector2(1f, 1f);
    ScopePayload scopePayload = new ScopePayload();
    int particleCount = 0;
    private final Array<IEmitter> emitters = new Array<>();
    private float totalTime = 0;
    private final EmitterComparator emitterComparator = new EmitterComparator();
    private boolean paused = false;

    public ParticleEffectInstance(ParticleEffectDescriptor particleEffectDescriptor) {
        this.descriptor = particleEffectDescriptor;
    }

    public void init() {
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).init();
        }
    }

    public float getTotalTime() {
        return totalTime;
    }

    public boolean isPaused() {
        return paused;
    }

    public ScopePayload getScope() {
        return scopePayload;
    }

    public void setScope(ScopePayload scope) {
        this.scopePayload = scope;
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).setScope(scope);
        }
    }

    public void update(float delta) {
        if (paused) return;

        if (isComplete() && !loopable) return;

        descriptor.setEffectReference(this);

        if (totalTime > 3600) totalTime = 0; //TODO: maybe just supple TimeUtils time now instead...
        totalTime += delta;

        if (scopePayload != null) {
            scopePayload.set(ScopePayload.TOTAL_TIME, totalTime);
        }

        particleCount = 0;
        nodeCalls = 0;

        sortEmitters();

        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).update(delta);
            particleCount += emitters.get(i).getActiveParticleCount();
        }

        if (particleCount == 0 && loopable) {
            for (int i = 0; i < emitters.size; i++) {
                if (!emitters.get(i).isContinuous()) {
                    if (emitters.get(i).getDelayRemaining() == 0) {
                        emitters.get(i).restart();
                    }
                }
            }
        }
    }

    public void render(ParticleRenderer particleRenderer) {
        particleRenderer.render(this);
    }

    public void addEmitter(ParticleEmitterDescriptor particleEmitterDescriptor) {
        final ParticleEmitterInstance particleEmitterInstance = new ParticleEmitterInstance(particleEmitterDescriptor, this);
        emitters.add(particleEmitterInstance);
    }

    public void removeEmitterForEmitterDescriptor(ParticleEmitterDescriptor emitter) {
        for (int i = emitters.size - 1; i >= 0; i--) {
            if (emitters.get(i).getEmitterGraph() == emitter) {
                emitters.removeIndex(i);
            }
        }
    }

    public boolean isContinuous() {
        for (ParticleEmitterDescriptor emitterDescriptor : descriptor.emitterModuleGraphs) {
            if (emitterDescriptor.isContinuous()) {
                return true;
            }
        }

        return false;
    }

    public boolean isComplete() {
        if (loopable) return false;

        boolean allEmittersContinuous = true;
        for (int i = 0; i < emitters.size; i++) {
            if (!emitters.get(i).isContinuous()) {
                allEmittersContinuous = false;
                break;
            }
        }

        if (allEmittersContinuous) {
            return false;
        }


        for (int i = 0; i < emitters.size; i++) {
            if (!emitters.get(i).isComplete()) {
                return false;
            }
        }

        return true;
    }

    public void allowCompletion() {
        loopable = false;
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).stop();
        }
    }

    public void pause() {
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).pause();
        }
        paused = true;
    }

    public void resume() {
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).resume();
        }
        paused = false;
    }

    public void restart() {
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).restart();
        }
        paused = false;
        totalTime = 0;
    }

    public void reset() {
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).reset();
        }
        paused = true;
        totalTime = 0;
    }

    public Array<IEmitter> getEmitters() {
        return emitters;
    }

    public IEmitter getEmitter(ParticleEmitterDescriptor descriptor) {
        for (IEmitter instance : emitters) {
            if (instance.getEmitterGraph() == descriptor) {
                return instance;
            }
        }

        return null;
    }

    public void setWorldRotation(float worldRotation) {
        this.worldRotation = worldRotation;
    }

    public void setWorldScale(Vector2 worldScale) {
        this.worldScale.set(worldScale);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public int getNodeCalls() {
        return nodeCalls;
    }

    public void reportNodeCall() {
        nodeCalls++;
    }

    public void sortEmitters() {
        emitters.sort(emitterComparator);
        for (int i = 0; i < emitters.size; i++) {
            emitters.get(i).getEmitterGraph().setSortPosition(i);
        }
    }

    public static class EmitterComparator implements Comparator<IEmitter> {

        @Override
        public int compare(IEmitter o1, IEmitter o2) {
            return o1.getEmitterGraph().getSortPosition() - o2.getEmitterGraph().getSortPosition();
        }
    }
}
