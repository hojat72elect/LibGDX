package com.rockbite.bongo.engine.events.prefab;

import com.rockbite.bongo.engine.prefab.PrefabConfig;

import net.mostlyoriginal.api.event.common.Event;

import lombok.Data;


@Data
public class PrefabUpdatedEvent implements Event {

    private PrefabConfig prefabConfig;
}
