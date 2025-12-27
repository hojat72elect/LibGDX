package com.mbrlabs.mundus.editor.ui.modules.inspector

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.widgets.CollapseWidget
import com.mbrlabs.mundus.editor.ui.widgets.FaTextButton
import com.mbrlabs.mundus.editor.utils.Fa

abstract class BaseInspectorWidget(title: String) : VisTable() {

    companion object {
        private val COLLAPSE_BTN_DOWN = Fa.CARET_UP
        private val COLLAPSE_BTN_UP = Fa.CARET_DOWN
    }

    var title: String? = null
        set(title) {
            field = title
            titleLabel.setText(title)
        }

    private val collapseBtn = FaTextButton(COLLAPSE_BTN_UP)
    private val deleteBtn = FaTextButton(Fa.TIMES)
    private var deletableBtnCell: Cell<*>? = null

    protected val collapsibleContent = VisTable()
    private val collapsibleWidget = CollapseWidget(collapsibleContent)
    private val titleLabel = VisLabel()

    private var deletable: Boolean = false

    init {
        collapseBtn.label.setFontScale(0.7f)
        deleteBtn.label.setFontScale(0.7f)
        deleteBtn.style.up = null

        deletable = false

        setupUI()
        setupListeners()

        this.title = title
    }

    private fun setupListeners() {
        // collapse
        collapseBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                collapse(!isCollapsed)
            }
        })

        // delete
        deleteBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onDelete()
            }
        })
    }

    private fun setupUI() {
        // header
        val header = VisTable()
        deletableBtnCell = header.add(deleteBtn).top().left().padBottom(4f)
        header.add(titleLabel)
        header.add(collapseBtn).right().top().width(20f).height(20f).expand().row()

        // add seperator
        header.add(Separator(UI.greenSeperatorStyle)).fillX().expandX().colspan(3).row()

        // add everything to root
        add(header).expand().fill().padBottom(10f).row()
        add(collapsibleWidget).expand().fill().row()
        isDeletable = deletable
    }

    var isDeletable: Boolean
        get() = deletable
        set(deletable) {
            this.deletable = deletable
            if (deletable) {
                deleteBtn.isVisible = true
                deletableBtnCell!!.width(20f).height(20f).padRight(5f)
            } else {
                deleteBtn.isVisible = false
                deletableBtnCell!!.width(0f).height(0f).padRight(0f)
            }
        }

    val isCollapsed: Boolean
        get() = collapsibleWidget.isCollapsed

    fun collapse(collapse: Boolean) {
        collapsibleWidget.setCollapsed(collapse, false)
        if (collapse) {
            collapseBtn.setText(COLLAPSE_BTN_DOWN)
        } else {
            collapseBtn.setText(COLLAPSE_BTN_UP)
        }
    }

    abstract fun onDelete()

    abstract fun setValues(go: GameObject)

}
