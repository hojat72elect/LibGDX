package com.mbrlabs.mundus.editor.history.commands

import com.badlogic.gdx.scenes.scene2d.ui.Tree
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.ModelCacheManager
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.LightComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.events.ComponentAddedEvent
import com.mbrlabs.mundus.editor.events.SceneGraphChangedEvent
import com.mbrlabs.mundus.editor.history.Command
import com.mbrlabs.mundus.editor.ui.modules.outline.Outline
import com.mbrlabs.mundus.editor.utils.Log
import com.mbrlabs.mundus.editorcommons.events.TerrainAddedEvent

/**
 * Delete command for game objects Deletion will update sceneGraph and outline
 */
class DeleteCommand(private var go: GameObject?, private var node: Outline.OutlineNode) : Command {

    companion object {
        private val TAG = DeleteCommand::class.java.simpleName
    }

    private val projectManager: ProjectManager = Mundus.inject()

    private var parentGO: GameObject? = null
    private var parentNode: Tree.Node<Outline.OutlineNode, GameObject, Outline.NodeTable>? = null
    private var tree: Tree<Outline.OutlineNode, GameObject>? = null

    init {
        this.parentGO = go!!.parent
        this.parentNode = node.parent
        this.tree = node.tree
    }

    override fun execute() {
        Log.trace(TAG, "Remove game object [{}]", go)
        // remove go from sceneGraph
        go!!.remove()
        // remove from outline tree
        tree!!.remove(node)
        Mundus.postEvent(SceneGraphChangedEvent())
        ModelCacheManager.rebuildIfCached(go, true)
    }

    override fun undo() {
        Log.trace(TAG, "Undo remove of game object [{}]", go)
        // add to sceneGraph
        parentGO!!.addChild(go)
        // add to outline
        if (parentNode == null)
            tree!!.add(node)
        else
            parentNode!!.add(node)
        node.expandTo()
        Mundus.postEvent(SceneGraphChangedEvent())
        ModelCacheManager.rebuildIfCached(go, true)

        // For components that utilize gizmos we should send a ComponentAddedEvent
        // so that GizmoManager can update as needed.
        val lightComponent: LightComponent? = go!!.findComponentByType(Component.Type.LIGHT)
        if (lightComponent != null) {
            Mundus.postEvent(ComponentAddedEvent(lightComponent))
        }

        val terrainComponent: TerrainComponent? = go!!.findComponentByType(Component.Type.TERRAIN)
        if (terrainComponent != null) {
            projectManager.current().currScene.terrains.add(terrainComponent)
            Mundus.postEvent(TerrainAddedEvent(terrainComponent))
        }
    }

}
