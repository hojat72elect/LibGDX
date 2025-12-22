package games.rednblack.editor.graph.actions.config;

import static games.rednblack.editor.graph.actions.ActionFieldType.Action;

import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class EventActionNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public EventActionNodeConfiguration() {
        super("EventAction", "Event", "Action");
        addNodeOutput(
                new GraphNodeOutputImpl<ActionFieldType>("action", "Action", Action));
    }
}
