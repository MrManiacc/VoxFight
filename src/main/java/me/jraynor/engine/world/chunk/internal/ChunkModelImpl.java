package me.jraynor.engine.world.chunk.internal;

import lombok.Getter;
import me.jraynor.core.other.Model;
import me.jraynor.engine.world.chunk.Chunk;
import me.jraynor.engine.world.chunk.ChunkModel;
import me.jraynor.engine.world.chunk.Direction;
import me.jraynor.engine.world.internal.WorldImpl;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChunkModelImpl implements ChunkModel {
    @Getter
    private Model model;
    private float[] vertices, normals, texCoords;
    private int[] indices;
    private Chunk chunk;
    private final float[][] blockVertices = new float[][]{{0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f}, {-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f}, {0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f}, {-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f}, {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f}, {-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f}};
    private final float[] blockNormals = new float[]{0, 0, -1, 0, 0, 1, 1, 0, 0, -1, 0, 0, 0, 1, 0, 0, -1, 0};

    public ChunkModelImpl(Chunk chunk) {
        this.chunk = chunk;
    }

    /**
     * Renders the chunk model
     */
    public void render() {
        model.bind(0, 1, 2);
        GL30.glDrawElements(GL11.GL_TRIANGLES, model.getIndexCount(), GL11.GL_UNSIGNED_BYTE, 0);
        model.unbind(0, 1, 2);
    }

    /**
     * Rebuilds the chunk
     *
     * @param blocks    the blocks of the chunk to rebuild
     * @param neighbors the chunk neighbors
     */
    public void rebuild(short[][][] blocks, Map<Direction, Chunk> neighbors) {
        if (model == null)
            model = Model.create();
        buildModelData(blocks, neighbors);
        model.bind(0, 1, 2);
        model.createAttribute(0, vertices, 3);
        model.createAttribute(1, normals, 3);
        model.createAttribute(0, texCoords, 3);
        model.createIndexBuffer(indices);
        model.unbind(0, 1, 2);
    }

    /**
     * Builds the arrays of data
     *
     * @param blocks
     * @param neighbors
     */
    private void buildModelData(short[][][] blocks, Map<Direction, Chunk> neighbors) {
        List<Float> vertices = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int y = 0; y < blocks[0].length; y++) {
            for (int x = 0; x < blocks.length; x++) {
                for (int z = 0; z < blocks[0][0].length; z++) {
                    boolean[] faces = getCulledFaces(x, y, z, neighbors);
                    for (int face = 0; face < 6; face++) {
                        if (faces[face]) {
                            for (int i = 0; i < 4; i++) {
                                vertices.add(blockVertices[face][i * 3] + x);
                                normals.add(blockNormals[face * 3]);
                                vertices.add(blockVertices[face][i * 3 + 1] + y);
                                normals.add(blockNormals[face * 3 + 1]);
                                vertices.add(blockVertices[face][i * 3 + 2] + z);
                                normals.add(blockNormals[face * 3 + 2]);

                                //TODO: update this for real block texture coords!
                                textureCoords.add(0.0f);
                                textureCoords.add(0.0f);


                            }
                            indices.add(vertices.size() / 3 - 4);
                            indices.add(vertices.size() / 3 - 3);
                            indices.add(vertices.size() / 3 - 2);
                            indices.add(vertices.size() / 3 - 2);
                            indices.add(vertices.size() / 3 - 3);
                            indices.add(vertices.size() / 3 - 1);
                        }
                    }
                }
            }
        }
        this.vertices = new float[vertices.size()];
        this.normals = new float[normals.size()];
        this.texCoords = new float[textureCoords.size()];
        this.indices = new int[indices.size()];
        for (int i = 0; i < vertices.size(); i++) this.vertices[i] = vertices.get(i);
        for (int i = 0; i < normals.size(); i++) this.normals[i] = normals.get(i);
        for (int i = 0; i < textureCoords.size(); i++) this.texCoords[i] = textureCoords.get(i);
        for (int i = 0; i < indices.size(); i++) this.indices[i] = indices.get(i);
    }

    /**
     * Gets the faces that are visible
     *
     * @return the culled faces
     */
    private boolean[] getCulledFaces(int x, int y, int z, Map<Direction, Chunk> neighbors) {
        boolean[] faces = new boolean[]{false, false, false, false, false, false};
        cullLocally(faces, x, y, z);
        noNeighborsCull(faces, x, y, x);
        neighborsCull(faces, x, y, z, neighbors);
        return faces;
    }

    /**
     * This method will do face culling relative to blocks inside the chunk
     *
     * @param faces the outputted faces
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     */
    private void cullLocally(boolean[] faces, int x, int y, int z) {
        if (x != 0)
            if (shouldCullBlock(chunk, x - 1, y, z))
                faces[3] = true;
        if (x != WorldImpl.CHUNK_WIDTH - 1)
            if (shouldCullBlock(chunk, x + 1, y, z))
                faces[2] = true;


        if (y != 0) {
            if (shouldCullBlock(chunk, x, y - 1, z))
                faces[5] = true;
        } else
            faces[5] = true;

        if (y != WorldImpl.CHUNK_HEIGHT - 1) {
            if (shouldCullBlock(chunk, x, y + 1, z))
                faces[4] = true;
        } else
            faces[4] = true;

        if (z != 0)
            if (shouldCullBlock(chunk, x, y, z - 1))
                faces[0] = true;

        if (z != WorldImpl.CHUNK_WIDTH - 1)
            if (shouldCullBlock(chunk, x, y, z + 1))
                faces[1] = true;

    }

    /**
     * checks to see if the block is 0
     *
     * @param chunk the chunk to check for
     * @param x     block x
     * @param y     block y
     * @param z     block z
     * @return whether or not to cull
     */
    private boolean shouldCullBlock(Chunk chunk, int x, int y, int z) {
        return chunk.getBlock(x, y, z) == 0;
    }

    /**
     * Checks if the edges need to be culled with the neighbors
     *
     * @param faces     the faces to cull
     * @param x         the block x
     * @param y         the block y
     * @param z         the block z
     * @param neighbors the chunk neighbors
     */
    private void neighborsCull(boolean[] faces, int x, int y, int z, Map<Direction, Chunk> neighbors) {
        if (neighbors.containsKey(Direction.NORTH)) {
            Chunk northNeighbor = neighbors.get(Direction.NORTH);
            if (z == WorldImpl.CHUNK_WIDTH - 1 && shouldCullBlock(northNeighbor, x, y, 0))
                faces[1] = true;
        }
        if (neighbors.containsKey(Direction.SOUTH)) {
            Chunk southNeighbor = neighbors.get(Direction.SOUTH);
            if (z == 0 && shouldCullBlock(southNeighbor, x, y, WorldImpl.CHUNK_WIDTH - 1))
                faces[0] = true;
        }

        if (neighbors.containsKey(Direction.EAST)) {
            Chunk eastNeighbor = neighbors.get(Direction.EAST);
            if (x == WorldImpl.CHUNK_WIDTH - 1 && shouldCullBlock(eastNeighbor, 0, y, z))
                faces[2] = true;
        }

        if (neighbors.containsKey(Direction.WEST)) {
            Chunk eastNeighbor = neighbors.get(Direction.WEST);
            if (x == 0 && shouldCullBlock(eastNeighbor, WorldImpl.CHUNK_WIDTH - 1, y, z))
                faces[3] = true;
        }
    }


    /**
     * Gets the local cull for the given chunk
     *
     * @param faces
     * @param x
     * @param y
     * @param z
     */
    private void noNeighborsCull(boolean[] faces, int x, int y, int z) {
        if (x == 0)
            faces[3] = true;
        if (x == WorldImpl.CHUNK_WIDTH - 1)
            faces[2] = true;
        if (z == 0)
            faces[0] = true;
        if (z == WorldImpl.CHUNK_WIDTH - 1)
            faces[1] = true;
        if (y == 0)
            faces[5] = true;
        if (y == WorldImpl.CHUNK_HEIGHT - 1)
            faces[4] = true;
    }
}
