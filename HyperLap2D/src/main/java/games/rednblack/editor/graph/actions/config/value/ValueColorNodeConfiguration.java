package games.rednblack.editor.graph.actions.config.value;

import static games.rednblack.editor.graph.actions.ActionFieldType.Color;

import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class ValueColorNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public ValueColorNodeConfiguration() {
        super("ValueColor", "Color", "Value");
        addNodeOutput(
                new GraphNodeOutputImpl<ActionFieldType>("value", "Value", Color));
    }
}

