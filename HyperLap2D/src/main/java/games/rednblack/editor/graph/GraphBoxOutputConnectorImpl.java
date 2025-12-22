package games.rednblack.editor.graph;

import com.google.common.base.Supplier;

import games.rednblack.editor.graph.data.FieldType;

public class GraphBoxOutputConnectorImpl<T extends FieldType> implements GraphBoxOutputConnector<T> {
    private final Side side;
    private final Supplier<Float> offsetSupplier;
    private final String fieldId;

    public GraphBoxOutputConnectorImpl(Side side, Supplier<Float> offsetSupplier, String fieldId) {
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
