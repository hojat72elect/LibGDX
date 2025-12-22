package games.rednblack.editor.view.ui.properties;

import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.puremvc.Facade;

public abstract class UIAbstractProperties extends VisTable {

    protected final Facade facade;

    public UIAbstractProperties() {
        facade = Facade.getInstance();
    }

    public String getPrefix() {
        return "games.rednblack.editor.view.ui.properties";
    }

    public String getUpdateEventName() {
        return getPrefix() + "." + "PROPERTIES_UPDATED";
    }
}
