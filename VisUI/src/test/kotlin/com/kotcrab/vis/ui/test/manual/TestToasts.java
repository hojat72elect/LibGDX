package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.ToastManager;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.toast.MessageToast;
import com.kotcrab.vis.ui.widget.toast.Toast;

public class TestToasts extends VisWindow {
    private final ToastManager toastManager;

    public TestToasts(Stage stage) {
        super("toasts");

        toastManager = new ToastManager(stage);

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addCloseButton();
        closeOnEscape();

        addListener(new WindowResizeListener() {
            @Override
            public void resize() {
                toastManager.resize();
            }
        });

        final VisSelectBox<String> alignment = new VisSelectBox<String>();
        alignment.setItems("top left", "top right", "top center", "bottom left", "bottom right", "bottom center");
        alignment.setSelectedIndex(1);
        alignment.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int alignIndex = alignment.getSelectedIndex();
                switch (alignIndex) {
                    case 0:
                        toastManager.setAlignment(Align.topLeft);
                        break;
                    case 1:
                        toastManager.setAlignment(Align.topRight);
                        break;
                    case 2:
                        toastManager.setAlignment(Align.top | Align.center);
                        break;
                    case 3:
                        toastManager.setAlignment(Align.bottomLeft);
                        break;
                    case 4:
                        toastManager.setAlignment(Align.bottomRight);
                        break;
                    case 5:
                        toastManager.setAlignment(Align.bottom | Align.center);
                        break;
                }
            }
        });

        VisTextButton textToastButton = new VisTextButton("text only");
        textToastButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toastManager.show("Text only toast", 3);
            }
        });

        VisTextButton messageToastButton = new VisTextButton("message toast");
        messageToastButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSimpleToast();
            }
        });

        VisTextButton customToastButton = new VisTextButton("custom toast");
        customToastButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                VisTable content = new VisTable(true);
                content.add("Toast content can be very complex").row();
                content.add("This toast has a text field");
                content.add(new VisTextField()).row();
                content.add("And a button ");
                content.add(new VisTextButton("press me"));
                toastManager.show(new Toast("dark", content));
            }
        });

        VisTextButton clearButton = new VisTextButton("clear");
        clearButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toastManager.clear();
            }
        });

        VisTable alignTable = new VisTable(true);
        alignTable.add("Alignment: ");
        alignTable.add(alignment);
        alignTable.add(clearButton);

        VisTable toastBtnTable = new VisTable(true);
        toastBtnTable.add(textToastButton);
        toastBtnTable.add(messageToastButton);
        toastBtnTable.add(customToastButton);

        add(alignTable).row();
        add(toastBtnTable).row();

        pack();
        centerWindow();
    }

    private void showSimpleToast() {
        final MessageToast messageToast = new MessageToast("This is a toast example.\nPress any of the button to close it.");
        messageToast.addLinkLabel("link1", new LinkLabel.LinkLabelListener() {
            @Override
            public void clicked(String url) {
                System.out.println("clicked: link1");
                messageToast.fadeOut();
            }
        });
        messageToast.addLinkLabel("link2", new LinkLabel.LinkLabelListener() {
            @Override
            public void clicked(String url) {
                System.out.println("clicked: link2");
                messageToast.fadeOut();
            }
        });
        messageToast.addLinkLabel("link3", new LinkLabel.LinkLabelListener() {
            @Override
            public void clicked(String url) {
                System.out.println("clicked: link3");
                messageToast.fadeOut();
            }
        });
        toastManager.show(messageToast, 5);
    }
}
