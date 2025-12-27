package com.mbrlabs.mundus.editor.ui.modules.inspector

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.events.AssetSelectedEvent
import com.mbrlabs.mundus.editor.events.ComponentAddedEvent
import com.mbrlabs.mundus.editor.events.GameObjectSelectedEvent
import com.mbrlabs.mundus.editor.ui.widgets.AutoFocusScrollPane
import com.mbrlabs.mundus.editor.utils.Log
import com.mbrlabs.mundus.editorcommons.events.GameObjectModifiedEvent
import com.mbrlabs.mundus.editorcommons.events.ProjectChangedEvent

class Inspector : VisTable(),
    GameObjectSelectedEvent.GameObjectSelectedListener,
    GameObjectModifiedEvent.GameObjectModifiedListener,
    ComponentAddedEvent.ComponentAddedListener,
    AssetSelectedEvent.AssetSelectedListener,
    ProjectChangedEvent.ProjectChangedListener {

    companion object {
        private val TAG = Inspector::class.java.simpleName
    }

    enum class InspectorMode {
        GAME_OBJECT, ASSET, EMPTY
    }

    private var mode = InspectorMode.EMPTY
    private val root = VisTable()
    private val scrollPane = AutoFocusScrollPane(root)

    private val goInspector: GameObjectInspector
    private val assetInspector: AssetInspector

    init {
        Mundus.registerEventListener(this)

        goInspector = GameObjectInspector()
        assetInspector = AssetInspector()

        init()
    }

    fun init() {
        setBackground("window-bg")
        add(VisLabel("Inspector")).expandX().fillX().pad(3f).row()
        addSeparator().row()
        root.align(Align.top)
        scrollPane.setScrollingDisabled(true, false)
        scrollPane.setFlickScroll(false)
        scrollPane.setFadeScrollBars(false)

        add<ScrollPane>(scrollPane).expand().fill().top()
    }

    fun clearWidgets() {
        root.clear()
        mode = InspectorMode.EMPTY
    }

    override fun onGameObjectSelected(event: GameObjectSelectedEvent) {
        if (mode != InspectorMode.GAME_OBJECT) {
            mode = InspectorMode.GAME_OBJECT
            root.clear()
            root.add(goInspector).grow().row()
        }
        goInspector.setGameObject(event.gameObject!!)
    }

    override fun onGameObjectModified(event: GameObjectModifiedEvent) {
        goInspector.updateGameObject()
    }

    override fun onAssetSelected(event: AssetSelectedEvent) {
        Log.debug(TAG, event.asset.toString())
        if (mode != InspectorMode.ASSET) {
            mode = InspectorMode.ASSET
            root.clear()
            root.add(assetInspector).grow().row()
        }
        assetInspector.asset = event.asset
    }

    override fun onComponentAdded(event: ComponentAddedEvent) {
        val component = event.component

        if (component.gameObject == goInspector.getGameObject()) {
            goInspector.addComponent(component)
        }
    }

    override fun onProjectChanged(event: ProjectChangedEvent) {
        clearWidgets()
    }

}
