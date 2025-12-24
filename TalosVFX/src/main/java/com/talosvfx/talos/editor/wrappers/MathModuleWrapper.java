
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.talosvfx.talos.runtime.vfx.Expression;
import com.talosvfx.talos.runtime.vfx.modules.MathModule;
import com.talosvfx.talos.runtime.vfx.utils.MathExpressionMappings;
import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public class MathModuleWrapper extends ModuleWrapper<MathModule> {

    VisSelectBox<String> selectBox;
    private TextField aField;
    private TextField bField;

    public MathModuleWrapper() {
        super();
    }

    @Override
    protected float reportPrefWidth() {
        return 180;
    }

    @Override
    public void attachModuleToMyOutput(ModuleWrapper moduleWrapper, int mySlot, int targetSlot) {
        super.attachModuleToMyOutput(moduleWrapper, mySlot, targetSlot);

        module.a.setFlavour(module.output.getFlavour());
        module.b.setFlavour(module.output.getFlavour());
    }

    @Override
    public void setSlotInactive(int slotTo, boolean isInput) {
        super.setSlotInactive(slotTo, isInput);
        if (!isInput) {
            module.a.setFlavour(NumericalValue.Flavour.REGULAR);
            module.b.setFlavour(NumericalValue.Flavour.REGULAR);
        }
    }

    @Override
    public void setModule(MathModule module) {
        super.setModule(module);
        aField.setText(module.getDefaultA() + "");
        bField.setText(module.getDefaultB() + "");
    }

    @Override
    protected void configureSlots() {
        Array<String> mathsExpressions = new Array<>();
        MathExpressionMappings.getAvailableMathExpressions(mathsExpressions);

        selectBox = new VisSelectBox();
        selectBox.setItems(mathsExpressions);

        aField = addInputSlotWithTextField("A: ", MathModule.A);
        leftWrapper.add(selectBox).left().expandX().pad(5).padLeft(17).row();
        bField = addInputSlotWithTextField("B: ", MathModule.B);

        aField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float a = floatFromText(aField);
                module.setA(a);
            }
        });

        bField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float b = floatFromText(bField);
                module.setB(b);
            }
        });


        addOutputSlot("result", 0);

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedString = selectBox.getSelected();
                Expression expression = MathExpressionMappings.getMathExpressionForName(selectedString);

                module.setExpression(expression);
            }
        });
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        selectBox.setSelected(MathExpressionMappings.getNameForMathExpression(module.getExpression()));

        aField.setText(module.getDefaultA() + "");
        bField.setText(module.getDefaultB() + "");
    }
}
