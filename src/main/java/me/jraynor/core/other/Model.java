package me.jraynor.core.other;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_INT = 4;
    public final int id;
    private List<Vbo> dataVbos = new ArrayList<>();
    private Vbo indexVbo;
    private int indexCount;

    private Vector3f min, max, origin;

    /**
     * this creates a new opengl vao instance
     *
     * @return new vao instance
     */
    public static Model create() {
        int id = GL30.glGenVertexArrays();
        return new Model(id);
    }

    public void setIndexCount(int count) {
        this.indexCount = count;
    }

    private Model(int id) {
        this.id = id;
    }

    public int getIndexCount() {
        return indexCount;
    }

    /**
     * Binds a vao and the specified vbo attributes
     *
     * @param attributes vbo id's
     */
    public void bind(int... attributes) {
        bind();
        for (int i : attributes) {
            GL20.glEnableVertexAttribArray(i);
        }
    }


    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        this.min = min;
    }

    public Vector3f getMax() {
        return max;
    }

    public void setMax(Vector3f max) {
        this.max = max;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    /**
     * Unbinds a vao and it's corresponding vbo id's
     *
     * @param attributes vbo id's
     */
    public void unbind(int... attributes) {
        for (int i : attributes) {
            GL20.glDisableVertexAttribArray(i);
        }
        unbind();
    }

    /**
     * Stores the indices for the vao into a opengl buffer
     *
     * @param indices the indices for the model
     */
    public void createIndexBuffer(int[] indices) {
        this.indexVbo = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
        indexVbo.bind();
        indexVbo.storeData(indices);
        this.indexCount = indices.length;
    }

    /**
     * Stores the indices for the vao into a opengl buffer
     */
    public void createIndexBuffer(IntBuffer data) {
        this.indexVbo = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
        indexVbo.bind();
        indexVbo.storeData(data);
    }

    /**
     * Creates a vbo to store some specific data into. This is used for rendering
     *
     * @param attribute the attribute to bind to
     * @param data      the data to be put into the vbo
     * @param attrSize  the size 3 for 3d and 2 for 2d
     */
    public void createAttribute(int attribute, float[] data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, 0, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    /**
     * Creates a vbo to store some specific data into. This is used for rendering
     *
     * @param attribute the attribute to bind to
     * @param data      the data to be put into the vbo
     * @param attrSize  the size 3 for 3d and 2 for 2d
     */
    public void createAttribute(int attribute, FloatBuffer data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, 0, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    /**
     * Creates a vbo to store some specific data into. This is used for rendering
     *
     * @param attribute the attribute to bind to
     * @param data      the data to be put into the vbo
     * @param attrSize  the size 3 for 3d and 2 for 2d
     */
    public void createIntAttribute(int attribute, int[] data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, 0, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    /**
     * Creates a vbo to store some specific data into. This is used for rendering
     *
     * @param attribute the attribute to bind to
     * @param data      the data to be put into the vbo
     * @param attrSize  the size 3 for 3d and 2 for 2d
     */
    public void createIntAttribute(int attribute, IntBuffer data, int attrSize) {
        Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
        dataVbo.bind();
        dataVbo.storeData(data);
        GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, 0, 0);
        dataVbo.unbind();
        dataVbos.add(dataVbo);
    }

    /**
     * Unloads the vao from memory
     */
    public void delete() {
        GL30.glDeleteVertexArrays(id);
        for (Vbo vbo : dataVbos) {
            vbo.delete();
        }
        indexVbo.delete();
    }

    private void bind() {
        GL30.glBindVertexArray(id);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    private static class Vbo {
        private final int vboId;
        private final int type;

        private Vbo(int vboId, int type) {
            this.vboId = vboId;
            this.type = type;
        }

        /**
         * Creates a new vbo instance
         *
         * @param type
         * @return the new vbo
         */
        public static Vbo create(int type) {
            int id = GL15.glGenBuffers();
            return new Vbo(id, type);
        }

        /**
         * Bind the vbo for rendering
         */
        public void bind() {
            GL15.glBindBuffer(type, vboId);
        }

        /**
         * Unbind the vbo when done rendering
         */
        public void unbind() {
            GL15.glBindBuffer(type, 0);
        }

        /**
         * Stores the float data into this vbo
         *
         * @param data the data that is to be put into the vbo
         */
        public void storeData(float[] data) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            storeData(buffer);
        }

        /**
         * Stores the float data into this vbo
         *
         * @param data the data that is to be put into the vbo
         */
        public void storeData(int[] data) {
            IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            storeData(buffer);
        }

        /**
         * Stores the float data into this vbo
         *
         * @param data the data that is to be put into the vbo
         */
        public void storeData(IntBuffer data) {
            GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
        }

        public void storeData(FloatBuffer data) {
            GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
        }

        public void delete() {
            GL15.glDeleteBuffers(vboId);
        }
    }
}
