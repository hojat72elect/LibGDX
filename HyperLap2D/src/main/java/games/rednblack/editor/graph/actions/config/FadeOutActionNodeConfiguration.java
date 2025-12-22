package games.rednblack.editor.graph.actions.config;

import static games.rednblack.editor.graph.actions.ActionFieldType.Action;
import static games.rednblack.editor.graph.actions.ActionFieldType.Float;
import static games.rednblack.editor.graph.actions.ActionFieldType.Interpolation;
import static games.rednblack.editor.graph.actions.ActionFieldType.Param;

import games.rednblack.editor.graph.GraphNodeInputImpl;
import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class FadeOutActionNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public FadeOutActionNodeConfiguration() {
        super("FadeOutAction", "Fade Out", "Action");

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("duration", "Duration", true, Float, Param));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("interpolation", "Interpolation", false, Interpolation, Param));

        addNodeOutput(
                new GraphNodeOutputImpl<>("action", "Action", Action));
    }
}
