package com.mbrlabs.mundus.editor.ui.widgets;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class TextFieldWithLabel extends VisTable {

    protected VisTextField textField;
    private int width = -1;
    private final VisLabel label;

    public TextFieldWithLabel(String labelText, int width) {
        super();
        this.width = width;
        textField = new VisTextField();
        label = new VisLabel(labelText);
        setupUI();
    }

    public TextFieldWithLabel(String labelText) {
        this(labelText, -1);
    }

    private void setupUI() {
        if (width > 0) {
            add(label).left().width(width * 0.2f);
            add(textField).right().width(width * 0.8f).row();
        } else {
            add(label).left().expandX();
            add(textField).right().expandX().row();
        }
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void setEditable(boolean editable) {
        textField.setDisabled(!editable);
    }

    public void clear() {
        textField.setText("");
    }

    public void setLabelText(String text) {
        label.setText(toString());
    }
}
