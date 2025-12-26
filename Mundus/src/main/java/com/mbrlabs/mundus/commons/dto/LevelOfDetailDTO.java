package com.mbrlabs.mundus.commons.dto;

/**
 * For reading and writing LoD data to and from disk.
 */
public class LevelOfDetailDTO {

    // [Mesh][Vertices]
    private final float[][] vertices;

    // [Mesh][Indices]
    private final short[][] indices;

    public LevelOfDetailDTO(float[][] vertices, short[][] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public float[][] getVertices() {
        return vertices;
    }

    public short[][] getIndices() {
        return indices;
    }
}
