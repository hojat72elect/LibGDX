package com.talosvfx.talos.editor.addons.scene.events;

import com.talosvfx.talos.editor.data.RoutineStageData;
import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.assets.GameAsset;

public class RoutineUpdated implements TalosEvent {

    public GameAsset<RoutineStageData> routineAsset;

    public RoutineUpdated set(GameAsset<RoutineStageData> routineAsset) {
        this.routineAsset = routineAsset;
        return this;
    }

    @Override
    public void reset() {
        routineAsset = null;
    }
}
