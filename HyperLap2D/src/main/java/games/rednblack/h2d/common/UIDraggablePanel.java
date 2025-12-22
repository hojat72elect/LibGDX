package games.rednblack.h2d.common;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;

public class UIDraggablePanel extends H2DDialog {

    public boolean isOpen;

    public UIDraggablePanel(String title) {
        super(title, false);
        setMovable(true);
        setModal(false);
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        getTitleLabel().setAlignment(Align.left);
        getTitleTable().padLeft(4).padTop(3);
        padTop(32);
    }

    @Override
    public void addCloseButton() {
        VisImageButton closeButton = new VisImageButton("close-panel");
        this.getTitleTable().add(closeButton).padTop(1).padRight(-2);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return true;
            }
        });
    }

    public void invalidateHeight() {
        float heightOld = getHeight();
        pack();
        float heightDiff = heightOld - getHeight();
        setPosition(getX(), getY() + heightDiff);
    }

    @Override
    public VisDialog show(Stage stage) {
        isOpen = true;
        return super.show(stage);
    }

    @Override
    public void hide() {
        super.hide();
        isOpen = false;
    }

    @Override
    public void close() {
        super.close();
        isOpen = false;
    }
}
