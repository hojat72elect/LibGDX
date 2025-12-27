package com.mbrlabs.mundus.editor.core.converter;

import com.mbrlabs.mundus.commons.dto.TerrainManagerComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainManagerComponent;

public class TerrainManagerComponentConverter {
    public static TerrainManagerComponentDTO convert(TerrainManagerComponent c) {
        return new TerrainManagerComponentDTO(c.getProceduralGeneration());
    }

    public static TerrainManagerComponent convert(TerrainManagerComponentDTO dto, GameObject go) {
        return new TerrainManagerComponent(go, dto.getProceduralGeneration());
    }
}
