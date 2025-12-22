package games.rednblack.editor.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GraphChangedEvent extends Event {
    private final boolean structure;
    private final boolean data;

    public GraphChangedEvent(boolean structure, boolean data) {
        this.structure = structure;
        this.data = data;
    }

    public boolean isStructure() {
        return structure;
    }

    public boolean isData() {
        return data;
    }
}
