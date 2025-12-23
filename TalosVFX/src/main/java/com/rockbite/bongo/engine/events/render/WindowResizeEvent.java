package com.rockbite.bongo.engine.events.render;

import net.mostlyoriginal.api.event.common.Event;

import lombok.Data;

@Data
public class WindowResizeEvent implements Event {
    private int width;
    private int height;
}
