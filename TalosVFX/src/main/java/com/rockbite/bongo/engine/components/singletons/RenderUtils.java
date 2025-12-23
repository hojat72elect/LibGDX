package com.rockbite.bongo.engine.components.singletons;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

import net.mostlyoriginal.api.Singleton;

import lombok.Data;

@Data
@Singleton
public class RenderUtils extends Component {

    private RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU));
}
