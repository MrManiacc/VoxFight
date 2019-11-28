package me.jraynor.core.block.props;

import lombok.Getter;

/**
 * Represents a model of a block
 */
public class BlockModel {
    private float[][] vertices;
    @Getter
    private float[] rawVertices;

    public BlockModel(float[] rawVertices) {
        this.rawVertices = rawVertices;
    }

    public BlockModel(float[][] vertices) {
        this.vertices = vertices;
    }

    public BlockModel() {
        this.vertices = new float[][]{
                {0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f,},
                {-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f,},
                {0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,},
                {-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f,},
                {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,},
                {-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f,}
        };
    }

    public int getNumFaces() {
        return vertices.length;
    }


    public float get(int face, int index) {
        return vertices[face][index];
    }

}
