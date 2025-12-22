package games.rednblack.editor.view.ui.panel;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class TagItem extends VisTable {

    private final VisTextButton tagLbl;

    private final TagItemListener listener;

    public TagItem(String tag, TagItemListener listener) {
        this.listener = listener;
        tagLbl = new VisTextButton(tag, "tagBtn");
        VisImageButton closeBtn = new VisImageButton("trash-button");
        add(tagLbl).width(180);
        add(closeBtn).padLeft(5);

        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TagItem.this.remove();
                listener.removed(tagLbl.getText().toString());
            }
        });
    }

    public String getTagName() {
        return tagLbl.getText().toString();
    }

    public interface TagItemListener {
        void removed(String tag);
    }
}
