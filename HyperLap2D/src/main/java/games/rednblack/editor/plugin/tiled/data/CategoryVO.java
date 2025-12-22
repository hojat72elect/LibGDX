package games.rednblack.editor.plugin.tiled.data;

import com.badlogic.gdx.utils.Array;

public class CategoryVO {

    public String title = "size";
    public Array<AttributeVO> attributes;

    public CategoryVO(String title, Array<AttributeVO> attributes) {
        this.title = title;
        this.attributes = attributes;
    }
}
