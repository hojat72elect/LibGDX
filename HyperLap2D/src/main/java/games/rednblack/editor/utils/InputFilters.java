package games.rednblack.editor.utils;

import com.kotcrab.vis.ui.widget.VisTextField;

public class InputFilters {
    public static final VisTextField.TextFieldFilter ALPHANUMERIC = new VisTextField.TextFieldFilter() {
        @Override
        public boolean acceptChar(VisTextField textField, char c) {

            return Character.isAlphabetic(c) || Character.isDigit(c);
        }
    };
}
