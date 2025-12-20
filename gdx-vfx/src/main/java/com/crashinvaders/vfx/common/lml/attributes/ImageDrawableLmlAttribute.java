package com.crashinvaders.vfx.common.lml.attributes;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

public class ImageDrawableLmlAttribute implements LmlAttribute<Image> {

    @Override
    public Class<Image> getHandledType() {
        return Image.class;
    }

    @Override
    public void process(LmlParser parser, LmlTag tag, Image image, String rawAttributeData) {
        ActorConsumer<Drawable, Image> action = (ActorConsumer<Drawable, Image>) parser.parseAction(rawAttributeData, image);
        if (action == null) {
            parser.throwError("Cannot find action: " + rawAttributeData);
            return;
        }

        Drawable drawable = action.consume(image);
        image.setDrawable(drawable);
    }
}
