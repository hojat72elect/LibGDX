package games.rednblack.editor.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;

public abstract class UIItemCollapsibleProperties extends UIItemProperties {
    protected final String title;
    protected VisTable mainTable;
    protected VisTable header;
    protected CollapsibleWidget collapsibleWidget;

    public UIItemCollapsibleProperties(String title) {
        this.title = title;
        row().padTop(9).padBottom(6);
        add(crateHeaderTable()).expandX().fillX().padBottom(7);
        createCollapsibleWidget();
    }

    public Table crateHeaderTable() {
        header = new VisTable();
        header.setTouchable(Touchable.enabled);
        header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-active-bg"));
        header.add(StandardWidgetsFactory.createLabel(title)).left().expandX().padRight(6).padLeft(8);
        VisImageButton button = StandardWidgetsFactory.createImageButton("expandable-properties-button");
        header.add(button).padRight(8);
        header.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                collapse(header);
            }
        });
        return header;
    }

    public void collapse(VisTable header) {
        collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
        header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-" + (collapsibleWidget.isCollapsed() ? "inactive" : "active") + "-bg"));
    }

    private void createCollapsibleWidget() {
        mainTable = new VisTable();
        collapsibleWidget = new CollapsibleWidget(mainTable);
        row();
        add(collapsibleWidget).expand();
    }
}
