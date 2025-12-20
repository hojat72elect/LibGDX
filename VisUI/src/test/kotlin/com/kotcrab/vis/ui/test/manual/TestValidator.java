package com.kotcrab.vis.ui.test.manual;

import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestValidator extends VisWindow {

    public TestValidator() {
        super("input validator (float number)");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        VisValidatableTextField textField = new VisValidatableTextField(Validators.FLOATS);

        add(textField);

        pack();
        setPosition(600, 18);
    }
}
