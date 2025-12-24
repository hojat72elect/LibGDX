package com.talosvfx.talos.editor.addons.scene.logic.componentwrappers;

import com.badlogic.gdx.utils.Array;
import com.talosvfx.talos.editor.widgets.propertyWidgets.PropertyWidget;
import com.talosvfx.talos.editor.widgets.propertyWidgets.WidgetFactory;
import com.talosvfx.talos.runtime.assets.GameAssetType;
import com.talosvfx.talos.runtime.scene.components.PathRendererComponent;

public class PathRendererComponentProvider extends RendererComponentProvider<PathRendererComponent> {
    public PathRendererComponentProvider(PathRendererComponent component) {
        super(component);
    }

    @Override
    public Array<PropertyWidget> getListOfProperties() {
        Array<PropertyWidget> properties = new Array<>();

        PropertyWidget textureWidget = WidgetFactory.generateForGameAsset(component, "gameAsset", null, "Texture", GameAssetType.SPRITE);

        PropertyWidget thicknessWidget = WidgetFactory.generate(component, "thickness", "thickness");
        PropertyWidget repeatCount = WidgetFactory.generate(component, "repeatCount", "repeat count");
        PropertyWidget colorWidget = WidgetFactory.generate(component, "color", "Color");


        properties.add(textureWidget);
        properties.add(repeatCount);
        properties.add(thicknessWidget);
        properties.add(colorWidget);

        Array<PropertyWidget> superList = super.getListOfProperties();
        properties.addAll(superList);


        return properties;
    }

    @Override
    public String getPropertyBoxTitle() {
        return "Path Renderer";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
