package games.rednblack.editor.graph.actions.config;

import static games.rednblack.editor.graph.actions.ActionFieldType.Action;
import static games.rednblack.editor.graph.actions.ActionFieldType.Interpolation;
import static games.rednblack.editor.graph.actions.ActionFieldType.Param;

import games.rednblack.editor.graph.GraphNodeInputImpl;
import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class MoveByActionNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public MoveByActionNodeConfiguration() {
        super("MoveByAction", "Move By", "Action");

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("position", "Position", true, ActionFieldType.Vector2, Param));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("duration", "Duration", false, ActionFieldType.Float, Param));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("interpolation", "Interpolation", false, Interpolation, Param));

        addNodeOutput(
                new GraphNodeOutputImpl<>("action", "Action", Action));
    }
}
