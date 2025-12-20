package com.kotcrab.vis.ui.test.manual;

import com.kotcrab.vis.ui.util.FloatDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestIssue131 extends VisWindow {
    public TestIssue131() {
        super("issue #131");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        VisTextField field1 = new VisTextField("0.1234");
        VisTextField field2 = new VisTextField("4.5678");
        field1.setTextFieldFilter(new FloatDigitsOnlyFilter(true));
        field2.setTextFieldFilter(new FloatDigitsOnlyFilter(true));

        add(new LinkLabel("issue #131 - decimal point lost", "https://github.com/kotcrab/vis-ui/issues/131")).colspan(2).row();
        add(field1);
        add(field2);

        setResizable(true);
        setModal(false);
        addCloseButton();
        closeOnEscape();
        pack();
        centerWindow();
    }
}
