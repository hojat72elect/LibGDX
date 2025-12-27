package com.mbrlabs.mundus.editor.tools.picker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mbrlabs.mundus.editor.core.EditorScene;
import com.mbrlabs.mundus.editor.tools.ToolHandle;
import com.mbrlabs.mundus.editor.utils.Log;

public class ToolHandlePicker extends BasePicker {

    public ToolHandlePicker() {
        super();
    }

    public ToolHandle pick(ToolHandle[] handles, EditorScene scene, int screenX, int screenY) {
        begin(scene.viewport);
        renderPickableScene(handles, scene.batch, scene.cam);
        end();
        Pixmap pm = getFrameBufferPixmap(scene.viewport);

        int x = screenX - scene.viewport.getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.viewport.getScreenY() + scene.viewport.getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        Log.trace("ToolHandlePicker", "Picking handle with id {}", id);
        for (ToolHandle handle : handles) {
            if (handle.getId() == id) {
                return handle;
            }
        }

        return null;
    }

    private void renderPickableScene(ToolHandle[] handles, ModelBatch batch, Camera cam) {
        batch.begin(cam);
        for (ToolHandle handle : handles) {
            handle.renderPick(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        fbo.dispose();
    }
}
