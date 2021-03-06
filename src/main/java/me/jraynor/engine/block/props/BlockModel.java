package me.jraynor.engine.block.props;

import lombok.Getter;

/**
 * Represents a model of a block
 */
public class BlockModel {
    private float[][] vertices;
    @Getter
    private float[] rawVertices;
    @Getter
    private float[] normals = {
            0, 0, -1,
            0, 0, 1,
            1, 0, 0,
            -1, 0, 0,
            0, 1, 0,
            0, -1, 0,
    };

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
