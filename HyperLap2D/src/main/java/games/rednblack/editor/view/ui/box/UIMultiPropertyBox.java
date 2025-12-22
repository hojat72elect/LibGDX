package games.rednblack.editor.view.ui.box;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.editor.view.ui.properties.UIAbstractProperties;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;

public class UIMultiPropertyBox extends UICollapsibleBox {

    private final VisTable propertiesTable;
    private final VisScrollPane scrollPane;
    private final VisTable scrollPaneInner;

    public UIMultiPropertyBox() {
        super("Properties");
        setMovable(false);
        propertiesTable = new VisTable();
        scrollPaneInner = new VisTable();
        scrollPane = StandardWidgetsFactory.createScrollPane(scrollPaneInner);
        scrollPane.setFadeScrollBars(true);

        propertiesTable.add(scrollPane).maxHeight(Gdx.graphics.getHeight() * 0.38f).width(BOX_DEFAULT_WIDTH);
        createCollapsibleWidget(propertiesTable);
    }

    public void clearAll() {
        scrollPaneInner.clear();
        scrollPaneInner.reset();
    }

    public void addPropertyBox(UIAbstractProperties viewComponent) {
        scrollPaneInner.add(viewComponent).growX();
        scrollPaneInner.row();
    }
}
