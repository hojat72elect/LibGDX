package com.rockbite.bongo.engine.events.entity;

import net.mostlyoriginal.api.event.common.Event;

import lombok.Data;

@Data
public class EntityUpdateEvent implements Event {

    private int entityID;
}
