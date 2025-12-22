package games.rednblack.editor.graph.actions.config;

import static games.rednblack.editor.graph.actions.ActionFieldType.Entity;

import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

public class EntityNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public EntityNodeConfiguration() {
        super("Entity", "Entity", "Entity");

        addNodeOutput(
                new GraphNodeOutputImpl<>("entity", "Entity",
                        Entity));
    }
}
