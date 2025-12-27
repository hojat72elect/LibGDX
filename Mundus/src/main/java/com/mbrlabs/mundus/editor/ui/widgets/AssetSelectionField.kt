package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.editor.assets.AssetFilter
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.modules.dialogs.assets.AssetPickerDialog

class AssetSelectionField : VisTable() {

    private val textField: VisTextField
    private val btn: VisTextButton

    var pickerListener: AssetPickerDialog.AssetPickerListener? = null
    var assetFilter: AssetFilter? = null

    private val internalListener: AssetPickerDialog.AssetPickerListener

    init {
        textField = VisTextField()
        textField.isDisabled = true
        btn = VisTextButton("Select")

        add(textField).grow()
        add(btn).padLeft(5f).row()

        internalListener = object : AssetPickerDialog.AssetPickerListener {
            override fun onSelected(asset: Asset?) {
                setAsset(asset)
                pickerListener?.onSelected(asset)
            }
        }

        btn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.assetSelectionDialog.show(true, assetFilter, internalListener)
            }
        })
    }

    fun setAsset(asset: Asset?) {
        textField.text = if (asset == null) "None" else asset.name
    }

}
