package com.rockbite.bongo.engine.events.commands;

import net.mostlyoriginal.api.event.common.Event;

import lombok.Data;

@Data
public class RawCommandEvent implements Event {


    private String commandText;
}
