package com.rockbite.bongo.engine.components.singletons;

import com.artemis.Component;
import com.rockbite.bongo.engine.annotations.ComponentExpose;
import com.rockbite.bongo.engine.gltf.scene.SceneEnvironment;

import net.mostlyoriginal.api.Singleton;

import lombok.Data;

@Data
@Singleton
public class Environment extends Component {

    @ComponentExpose
    private SceneEnvironment sceneEnvironment = new SceneEnvironment();
}
