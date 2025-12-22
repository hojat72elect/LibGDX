package games.rednblack.editor.data.migrations.data020;

import java.util.ArrayList;
import java.util.HashMap;

import games.rednblack.editor.renderer.data.ColorPrimitiveVO;
import games.rednblack.editor.renderer.data.Image9patchVO;
import games.rednblack.editor.renderer.data.LabelVO;
import games.rednblack.editor.renderer.data.LayerItemVO;
import games.rednblack.editor.renderer.data.LightVO;
import games.rednblack.editor.renderer.data.ParticleEffectVO;
import games.rednblack.editor.renderer.data.SimpleImageVO;
import games.rednblack.editor.renderer.data.SpriteAnimationVO;
import games.rednblack.editor.renderer.data.StickyNoteVO;
import games.rednblack.h2d.extension.spine.SpineVO;
import games.rednblack.h2d.extension.talos.TalosVO;

public class CompositeVO {
    public ArrayList<SimpleImageVO> sImages = new ArrayList<>(1);
    public ArrayList<Image9patchVO> sImage9patchs = new ArrayList<>(1);
    public ArrayList<LabelVO> sLabels = new ArrayList<>(1);
    public ArrayList<CompositeItemVO> sComposites = new ArrayList<>(1);
    public ArrayList<ParticleEffectVO> sParticleEffects = new ArrayList<>(1);
    public ArrayList<TalosVO> sTalosVFX = new ArrayList<>(1);
    public ArrayList<LightVO> sLights = new ArrayList<>(1);
    public ArrayList<SpineVO> sSpineAnimations = new ArrayList<>(1);
    public ArrayList<SpriteAnimationVO> sSpriteAnimations = new ArrayList<>(1);
    public ArrayList<ColorPrimitiveVO> sColorPrimitives = new ArrayList<>(1);

    public ArrayList<LayerItemVO> layers = new ArrayList<LayerItemVO>();

    public HashMap<String, StickyNoteVO> sStickyNotes = new HashMap<>(1);
}
