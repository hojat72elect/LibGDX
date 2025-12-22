package games.rednblack.editor.graph;

import com.google.common.base.Supplier;

import games.rednblack.editor.graph.data.FieldType;

public class GraphBoxInputConnectorImpl<T extends FieldType> implements GraphBoxInputConnector<T> {
    private final Side side;
    private final Supplier<Float> offsetSupplier;
    private final String fieldId;

    public GraphBoxInputConnectorImpl(Side side, Supplier<Float> offsetSupplier, String fieldId) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }
}
