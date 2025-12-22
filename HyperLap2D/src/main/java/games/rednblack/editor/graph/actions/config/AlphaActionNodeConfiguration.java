package games.rednblack.editor.graph.actions.config;

import static games.rednblack.editor.graph.actions.ActionFieldType.Action;
import static games.rednblack.editor.graph.actions.ActionFieldType.Float;
import static games.rednblack.editor.graph.actions.ActionFieldType.Interpolation;
import static games.rednblack.editor.graph.actions.ActionFieldType.Param;

import games.rednblack.editor.graph.GraphNodeInputImpl;
import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class AlphaActionNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public AlphaActionNodeConfiguration() {
        super("AlphaAction", "Alpha", "Action");

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("alpha", "Alpha", true, Float, Param));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("duration", "Duration", false, Float, Param));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("interpolation", "Interpolation", false, Interpolation, Param));

        addNodeOutput(
                new GraphNodeOutputImpl<>("action", "Action", Action));
    }
}
