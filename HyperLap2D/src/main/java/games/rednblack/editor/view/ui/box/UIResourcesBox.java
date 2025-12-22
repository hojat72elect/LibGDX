package games.rednblack.editor.view.ui.box;

import com.kotcrab.vis.ui.widget.VisTable;

import games.rednblack.h2d.common.view.ui.widget.imagetabbedpane.ImageTab;
import games.rednblack.h2d.common.view.ui.widget.imagetabbedpane.ImageTabbedPane;
import games.rednblack.h2d.common.view.ui.widget.imagetabbedpane.ImageTabbedPaneListener;
import games.rednblack.puremvc.Facade;

public class UIResourcesBox extends UICollapsibleBox {
    private final Facade facade;

    private final VisTable contentTable;
    private final VisTable tabContent;

    private final ImageTabbedPane tabbedPane;

    public UIResourcesBox() {
        super("Resources");
        facade = Facade.getInstance();

        setMovable(false);
        contentTable = new VisTable();
        tabContent = new VisTable();
        tabbedPane = new ImageTabbedPane();
        tabbedPane.addListener(new ImageTabbedPaneListener() {
            @Override
            public void switchedTab(ImageTab tab) {
                if (tab == null) return;
                setActiveTabContent(tab);
            }

            @Override
            public void removedTab(ImageTab tab) {

            }

            @Override
            public void removedAllTabs() {

            }
        });
        contentTable.add(tabbedPane.getTable()).expandX().fillX().growX().padTop(8);
        contentTable.row();
        contentTable.add(tabContent).expandX().width(BOX_DEFAULT_WIDTH);
        contentTable.row();
        createCollapsibleWidget(contentTable);
    }

    public void setActiveTabContent(ImageTab tab) {
        tabContent.clear();
        tabContent.add(tab.getContentTable()).expandX().fillX();
    }

    public void addTab(int index, ImageTab tab) {
        tabbedPane.insert(index, tab);
    }
}
