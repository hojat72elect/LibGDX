package com.kotcrab.vis.ui.widget.file;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.PopupMenu.PopupMenuStyle;

public class FileChooserStyle {
    public PopupMenuStyle popupMenuStyle;

    public Drawable highlight;
    public Drawable iconArrowLeft;
    public Drawable iconArrowRight;
    public Drawable iconFolder;
    public Drawable iconFolderParent;
    public Drawable iconFolderStar;
    public Drawable iconFolderNew;
    public Drawable iconDrive;
    public Drawable iconTrash;
    public Drawable iconStar;
    public Drawable iconStarOutline;
    public Drawable iconRefresh;
    public Drawable iconListSettings;

    public Drawable iconFileText;
    public Drawable iconFileImage;
    public Drawable iconFilePdf;
    public Drawable iconFileAudio;

    public Drawable contextMenuSelectedItem;
    public Drawable expandDropdown;

    public FileChooserStyle() {
    }

    public FileChooserStyle(FileChooserStyle style) {
        this.popupMenuStyle = style.popupMenuStyle;
        this.highlight = style.highlight;
        this.iconArrowLeft = style.iconArrowLeft;
        this.iconArrowRight = style.iconArrowRight;
        this.iconFolder = style.iconFolder;
        this.iconFolderParent = style.iconFolderParent;
        this.iconFolderStar = style.iconFolderStar;
        this.iconFolderNew = style.iconFolderNew;
        this.iconDrive = style.iconDrive;
        this.iconTrash = style.iconTrash;
        this.iconStar = style.iconStar;
        this.iconStarOutline = style.iconStarOutline;
        this.iconRefresh = style.iconRefresh;
        this.iconListSettings = style.iconListSettings;
        this.iconFileText = style.iconFileText;
        this.iconFileImage = style.iconFileImage;
        this.iconFilePdf = style.iconFilePdf;
        this.iconFileAudio = style.iconFileAudio;
        this.contextMenuSelectedItem = style.contextMenuSelectedItem;
        this.expandDropdown = style.expandDropdown;
    }
}
