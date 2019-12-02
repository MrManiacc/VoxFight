package me.jraynor.engine.block.props;

public class BlockUV {
    private int[] rows = new int[6];
    private int[] columns = new int[6];
    private static final float UNIT = 1.0f / 16;

    //fill all faces with same texture
    public BlockUV(int row, int column) {
        for (int i = 0; i < 6; i++)
            rows[i] = row;
        for (int i = 0; i < 6; i++)
            columns[i] = column;

    }


    //fill all faces with same texture
    public BlockUV(int[] row, int[] column) {
        this.rows = row;
        this.columns = column;
    }

    public float[] getUV(int face, int vertex) {
        float[] uv = new float[2];
        switch (vertex) {
            case 0://top left vertex
                uv[0] = rows[face] * UNIT;
                uv[1] = columns[face] * UNIT;
                break;
            case 1:
                uv[0] = rows[face] * UNIT;
                uv[1] = (columns[face] + 1) * UNIT;
                break;
            case 2:
                uv[0] = (rows[face] + 1) * UNIT;
                uv[1] = columns[face] * UNIT;
                break;
            case 3:
                uv[0] = (rows[face] + 1) * UNIT;
                uv[1] = (columns[face] + 1) * UNIT;
                break;
        }
        uv[1] = 1 - uv[1];
        return uv;
    }
}
