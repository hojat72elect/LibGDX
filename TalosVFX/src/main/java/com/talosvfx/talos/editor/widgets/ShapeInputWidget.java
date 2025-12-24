
package com.talosvfx.talos.editor.widgets;

import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.SIDE_ALL;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.SIDE_BOTTOM;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.SIDE_LEFT;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.SIDE_RIGHT;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.SIDE_TOP;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.TYPE_ELLIPSE;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.TYPE_LINE;
import static com.talosvfx.talos.runtime.vfx.modules.OffsetModule.TYPE_SQUARE;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.SelectBoxWithZoom;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.TextFieldWithZoom;

public class ShapeInputWidget extends Table {

    Array<String> shapeTypes = new Array<>();
    Array<String> sideTypes = new Array<>();

    TextField scaleField;
    CheckBox edgeBox;
    ShapeWidget shapeWidget;
    SelectBox<String> shapeType;
    SelectBox<String> sideBox;

    ChangeListener changeListener;

    float prevScale;

    boolean lockListeners = false;

    public ShapeInputWidget(Skin skin) {
        setSkin(skin);

        shapeTypes.add("SQUARE");
        shapeTypes.add("ELLIPSE");
        shapeTypes.add("LINE");

        scaleField = new TextFieldWithZoom("7", skin);
        prevScale = 7;
        shapeWidget = new ShapeWidget(skin);
        shapeType = new SelectBoxWithZoom<>(skin);
        sideBox = new SelectBoxWithZoom<>(skin);

        sideTypes.addAll("ALL", "TOP", "BOTTOM", "LEFT", "RIGHT");

        sideBox.setItems(sideTypes);

        edgeBox = new CheckBox("", skin);
        edgeBox.setChecked(true);

        shapeType.setItems(shapeTypes);

        add(edgeBox).width(30);
        add(scaleField).width(65).padLeft(5).row();
        add(shapeWidget).size(100).colspan(2).row();
        add(shapeType).width(100).colspan(2).row();
        add(sideBox).width(100).colspan(2);


        shapeType.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                shapeWidget.setType(getShape());

                if (changeListener != null && !lockListeners) changeListener.changed(new ChangeEvent(), ShapeInputWidget.this);
            }
        });

        shapeWidget.setListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (changeListener != null && !lockListeners) changeListener.changed(new ChangeEvent(), ShapeInputWidget.this);
            }
        });

        scaleField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScale();
                if (changeListener != null && !lockListeners) changeListener.changed(new ChangeEvent(), ShapeInputWidget.this);
            }
        });

        edgeBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (changeListener != null && !lockListeners) changeListener.changed(new ChangeEvent(), ShapeInputWidget.this);
            }
        });

        sideBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (changeListener != null && !lockListeners) changeListener.changed(new ChangeEvent(), ShapeInputWidget.this);
            }
        });
    }


    public void getShapePos(Vector2 pos) {
        try {
            float scl = Float.parseFloat(scaleField.getText());
            pos.set(shapeWidget.getPosX(), shapeWidget.getPosY()).scl(scl);
        } catch (Exception e) {

        }
    }

    public void getShapeSize(Vector2 size) {
        try {
            float scl = Float.parseFloat(scaleField.getText());
            size.set(shapeWidget.getShapeWidth(), shapeWidget.getShapeHeight()).scl(scl);
        } catch (Exception e) {

        }
    }

    public void setListener(ChangeListener listener) {
        this.changeListener = listener;
    }

    public void setPos(Vector2 tmp) {
        lockListeners = true;
        try {
            float scl = Float.parseFloat(scaleField.getText());
            shapeWidget.setPos(tmp.x / scl, tmp.y / scl);
        } catch (Exception e) {

        }
        lockListeners = false;
    }

    private void updateScale() {
        try {
            float scl = Float.parseFloat(scaleField.getText());
            prevScale = scl;
        } catch (Exception e) {

        }
    }

    public void setShapeSize(Vector2 size) {
        lockListeners = true;
        try {
            float scl = Float.parseFloat(scaleField.getText());
            shapeWidget.setShapeSize(size.x / scl, size.y / scl);
        } catch (Exception e) {

        }
        lockListeners = false;
    }

    public int getShape() {
        int res = 0;
        String type = shapeType.getSelected();

        if (type.equals("SQUARE")) res = TYPE_SQUARE;
        if (type.equals("ELLIPSE")) res = TYPE_ELLIPSE;
        if (type.equals("LINE")) res = TYPE_LINE;

        return res;
    }

    public void setShape(int shape) {
        shapeType.setSelected(shapeTypes.get(shape));
    }

    public int getSide() {
        int res = 0;
        String type = sideBox.getSelected();

        if (type.equals("ALL")) res = SIDE_ALL;
        if (type.equals("TOP")) res = SIDE_TOP;
        if (type.equals("BOTTOM")) res = SIDE_BOTTOM;
        if (type.equals("LEFT")) res = SIDE_LEFT;
        if (type.equals("RIGHT")) res = SIDE_RIGHT;

        return res;
    }

    public void setSide(int side) {
        sideBox.setSelected(sideTypes.get(side));
    }

    public boolean isEdge() {
        return edgeBox.isChecked();
    }

    public void setEdge(boolean edge) {
        edgeBox.setChecked(edge);
    }

    public float getScale() {
        float scl = 0f;
        try {
            scl = Float.parseFloat(scaleField.getText());
        } catch (Exception e) {

        }

        return scl;
    }

    public void setScaleVal(float scale) {
        ChangeListener tmp = changeListener;
        changeListener = null;

        scaleField.setText(scale + "");

        changeListener = tmp;
    }
}
