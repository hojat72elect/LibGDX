package games.rednblack.editor.controller.commands;

import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.factory.ItemFactory;
import games.rednblack.editor.renderer.data.PolygonShapeVO;

public class CreatePrimitiveCommand extends EntityModifyRevertibleCommand {

    @Override
    public void doAction() {
        Vector2 position = new Vector2(0, 0);
        PolygonShapeVO shape = PolygonShapeVO.createRect(100f / sandbox.getPixelPerWU(), 100f / sandbox.getPixelPerWU());

        if (!ItemFactory.get().createPrimitive(position, shape)) cancel();
    }

    @Override
    public void undoAction() {

    }
}
