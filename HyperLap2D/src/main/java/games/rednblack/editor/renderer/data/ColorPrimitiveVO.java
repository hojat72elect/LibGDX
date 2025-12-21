package games.rednblack.editor.renderer.data;

/**
 * Created by azakhary on 10/21/2015.
 */
public class ColorPrimitiveVO extends MainItemVO {

    public ColorPrimitiveVO() {
        super();
    }

    public ColorPrimitiveVO(ColorPrimitiveVO vo) {
        super(vo);
    }

    @Override
    public String getResourceName() {
        throw new RuntimeException("Color Primitive doesn't have resources to load.");
    }
}
