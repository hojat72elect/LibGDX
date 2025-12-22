package games.rednblack.editor.view.ui.panel;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import games.rednblack.h2d.common.UIDraggablePanel;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import games.rednblack.puremvc.Facade;

public class TagsPanel extends UIDraggablePanel {

    public static final String prefix = "games.rednblack.editor.view.ui.dialog.panels.TagsDialog";
    public static final String ITEM_ADD = prefix + ".ITEM_ADD";
    public static final String ITEM_REMOVED = prefix + ".ITEM_REMOVED";

    private final Facade facade;

    private final VisTable mainTable;
    private VisTable tagTable;
    private final TagItem.TagItemListener tagItemListener;

    private Set<String> tags = new HashSet<>();

    public TagsPanel() {
        super("Tags");
        addCloseButton();

        facade = Facade.getInstance();

        mainTable = new VisTable();

        add(mainTable).padBottom(4);

        tagItemListener = new TagItem.TagItemListener() {
            @Override
            public void removed(String tag) {
                tags.remove(tag);
                facade.sendNotification(ITEM_REMOVED, tag);
            }
        };
    }

    public void setEmpty() {
        mainTable.clear();
        VisLabel label = StandardWidgetsFactory.createLabel("No item selected");
        label.setAlignment(Align.center);
        mainTable.add(label).pad(10).width(278).center();
        invalidateHeight();
    }

    private void addTag(String tag) {
        tags.add(tag);
    }

    public void updateView() {
        mainTable.clear();

        tagTable = new VisTable();
        VisTable inputTable = new VisTable();

        List<String> sorted = new LinkedList<>(tags);
        Collections.sort(sorted);
        for (String tag : sorted) {
            tagTable.add(new TagItem(tag, tagItemListener)).pad(5).left().expandX().fillX();
            tagTable.row();
        }

        VisTextField newTagField = StandardWidgetsFactory.createTextField();
        VisTextButton createTagBtn = new VisTextButton("add");
        inputTable.add(newTagField).width(200);
        inputTable.add(createTagBtn).padLeft(5);

        createTagBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String tag = newTagField.getText();
                if (!tagExists(tag)) {
                    newTagField.setText("");
                    addTag(tag);
                    facade.sendNotification(ITEM_ADD, tag);
                }
            }
        });

        mainTable.add(inputTable);
        mainTable.row();
        mainTable.add(tagTable).expandX().fillX();

        invalidateHeight();
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    private boolean tagExists(String tag) {
        return tags.contains(tag);
    }
}
