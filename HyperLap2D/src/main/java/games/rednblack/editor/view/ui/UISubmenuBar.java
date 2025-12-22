package games.rednblack.editor.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.editor.view.ui.box.UICompositeHierarchy;
import games.rednblack.editor.view.ui.box.UICompositeHierarchyMediator;
import games.rednblack.puremvc.Facade;

public class UISubmenuBar extends VisTable {
    private final Facade facade;

    public UISubmenuBar() {
        Skin skin = VisUI.getSkin();
        facade = Facade.getInstance();

        setBackground(skin.getDrawable("sub-menu-bg"));

        //Hierarchy
        UICompositeHierarchyMediator uiCompositeHierarchyMediator = facade.retrieveMediator(UICompositeHierarchyMediator.NAME);
        UICompositeHierarchy uiCompositeHierarchy = uiCompositeHierarchyMediator.getViewComponent();
        add(uiCompositeHierarchy).left().expand().fill().padRight(6);
    }
}
