package me.jraynor.engine.registry.utils;

import lombok.Getter;
import lombok.Setter;

public class Data {
    public static class BlockData {
        @Getter
        @Setter
        public byte id;
        @Getter
        @Setter
        public String displayName = "undefined";
        @Getter
        @Setter
        public boolean solid;
        @Getter
        @Setter
        public float hardness;
        @Getter
        @Setter
        public float[][] vertices = new float[0][0];

        @Getter
        @Setter
        public float[] boundingSize = new float[]{0.5f, 0.5f, 0.5f};
        @Getter
        @Setter
        public int[][] faces = new int[0][0];
        @Getter
        @Setter
        public float[] rawVertices = new float[0];
        @Getter
        public float[] light = new float[0];

        public int[] getRows() {
            int[] rows = new int[faces.length];
            for (int i = 0; i < faces.length; i++)
                rows[i] = faces[i][0];
            return rows;
        }

        public int[] getColumns() {
            int[] rows = new int[faces.length];
            for (int i = 0; i < faces.length; i++)
                rows[i] = faces[i][1];
            return rows;
        }
    }
}
