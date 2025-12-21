package games.rednblack.editor.renderer.utils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import games.rednblack.editor.renderer.data.ColorPrimitiveVO;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.data.Image9patchVO;
import games.rednblack.editor.renderer.data.LabelVO;
import games.rednblack.editor.renderer.data.LightVO;
import games.rednblack.editor.renderer.data.ParticleEffectVO;
import games.rednblack.editor.renderer.data.SimpleImageVO;
import games.rednblack.editor.renderer.data.SpriteAnimationVO;

public class HyperJson {
    private static Json json = null;

    private HyperJson() {

    }

    public static Json getJson() {
        if (json == null) {
            json = new Json();
            json.setIgnoreUnknownFields(true);
            json.setOutputType(JsonWriter.OutputType.json);

            json.addClassTag(CompositeItemVO.class.getSimpleName(), CompositeItemVO.class);
            json.addClassTag(LightVO.class.getSimpleName(), LightVO.class);
            json.addClassTag(ParticleEffectVO.class.getSimpleName(), ParticleEffectVO.class);
            json.addClassTag(SimpleImageVO.class.getSimpleName(), SimpleImageVO.class);
            json.addClassTag(SpriteAnimationVO.class.getSimpleName(), SpriteAnimationVO.class);
            json.addClassTag(LabelVO.class.getSimpleName(), LabelVO.class);
            json.addClassTag(Image9patchVO.class.getSimpleName(), Image9patchVO.class);
            json.addClassTag(ColorPrimitiveVO.class.getSimpleName(), ColorPrimitiveVO.class);
        }
        return json;
    }
}
