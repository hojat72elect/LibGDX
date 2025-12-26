package com.mbrlabs.mundus.commons.dto;

import com.mbrlabs.mundus.commons.scene3d.components.TerrainManagerComponent;

public class TerrainManagerComponentDTO {

    private TerrainManagerComponent.ProceduralGeneration proceduralGeneration;

    public TerrainManagerComponentDTO() {
    }

    public TerrainManagerComponentDTO(final TerrainManagerComponent.ProceduralGeneration proceduralGeneration) {
        this.proceduralGeneration = proceduralGeneration;
    }

    public TerrainManagerComponent.ProceduralGeneration getProceduralGeneration() {
        return proceduralGeneration;
    }

    public void setProceduralGeneration(final TerrainManagerComponent.ProceduralGeneration proceduralGeneration) {
        this.proceduralGeneration = proceduralGeneration;
    }
}
