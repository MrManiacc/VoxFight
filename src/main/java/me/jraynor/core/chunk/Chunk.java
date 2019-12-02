package me.jraynor.core.chunk;

import lombok.Getter;
import lombok.Setter;
import me.jraynor.engine.block.Blocks;
import me.jraynor.engine.block.blocks.Block;
import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;
import me.jraynor.core.generation.IGenerator;
import me.jraynor.core.gl.Texture;
import me.jraynor.core.other.Model;
import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * The chunk is responsible for storing all of the ids of the loaded blocks in a
 * large 3d array. The total capacity for a chunk is 131,072‬ blocks or 32 * 256 * 32
 */
public class Chunk {
    private byte[][][] blocks = new byte[16][256][16];
    private int[][][] blockLights = new int[16][256][16];
    @Getter
    private Matrix4f transform; //The 3d position needed to render the model
    @Getter
    private Vector2i origin; //The 3d position needed to render the model
    @Getter
    private Model model; //Contains the 3d data needed to render a model
    private boolean dirty = false;//If dirty, then the model needs to be deleted and rebuilt based on the blocks
    private List<Float> blockVertices = new ArrayList<>();
    private List<Float> blockNormals = new ArrayList<>();
    private List<Float> blockUvs = new ArrayList<>();
    private List<Integer> blockIndices = new ArrayList<>();
    private List<Integer> blockLighting = new ArrayList<>();
    private int[] lights;
    private float[] vertices;
    private float[] normals;
    private float[] textureCoordinates;
    private int[] indices;
    private ChunkCull chunkCull;
    private boolean ready = false;//Will be true when the chunk can be rendered
    @Getter
    private AABBf aabBf;
    @Getter
    @Setter
    private boolean culled = false;
    @Getter
    private boolean missingNeighbors = false;
    @Getter
    public static Texture atlas = Texture.loadTexture("src/main/resources/core/textures/atlas.png");;

    public Chunk(Vector2i transform) {
        this.aabBf = new AABBf(new Vector3f(transform.x, 0, transform.y), new Vector3f(transform.x + 16, 256, transform.y + 16));
        this.origin = transform;
        this.transform = new Matrix4f().identity();
        this.transform.translate(transform.x, 0, transform.y);
        this.transform.scale(1, 1, 1);
        this.chunkCull = new ChunkCull(this);
    }

    /**
     * Used to set all of the blocks inside the 3d block array before the
     * 3d model is generated and sent to opengl
     */
    public void generateChunk(IGenerator generator) {
        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 256; y++)
                for (int z = 0; z < 16; z++) {
                    blocks[x][y][z] = generator.generatorBlock(origin, x, y, z);
                    setLight(x, y, z, 1);
                }
    }


    /**
     * This method should be 5 times per second
     */
    public void tick() {
        if (dirty) {
            System.out.println("chunking");
            blockIndices.clear();
            blockLighting.clear();
            blockUvs.clear();
            blockVertices.clear();
            blockNormals.clear();
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++)
                    for (int x = 0; x < 16; x++) {
                        processBlock(Blocks.getBlock(blocks[x][y][z]), x, y, z);
                    }
            }
            populateArrays();
            if (model == null) {
                model = Model.create();
            }
            model.bind(0, 1, 2);
            model.createAttribute(0, vertices, 3);
            model.createAttribute(1, normals, 3);
            model.createAttribute(2, textureCoordinates, 2);
//            model.createIntAttribute(2, lights, 1);
            model.createIndexBuffer(indices);
            model.unbind(0, 1, 2);
            dirty = false;
            ready = true;
        }
    }


    /**
     * Used to convert the arraylists containg the data into native lists for opengl
     */
    private void populateArrays() {
        this.vertices = new float[blockVertices.size()];
        for (int i = 0; i < blockVertices.size(); i++)
            this.vertices[i] = blockVertices.get(i);
        this.indices = new int[blockIndices.size()];
        for (int i = 0; i < blockIndices.size(); i++)
            this.indices[i] = blockIndices.get(i);
        this.textureCoordinates = new float[blockUvs.size()];
        for (int i = 0; i < blockUvs.size(); i++)
            this.textureCoordinates[i] = blockUvs.get(i);
        this.lights = new int[blockLighting.size()];
        for (int i = 0; i < blockLighting.size(); i++) {
            this.lights[i] = (blockLighting.get(i));
        }

        this.normals = new float[blockNormals.size()];
        for (int i = 0; i < blockNormals.size(); i++) {
            this.normals[i] = (blockNormals.get(i));
        }
    }


    /**
     * Converts the block into opengl 3d coordinates
     *
     * @param block
     * @param x
     * @param y
     * @param z
     */
    private void processBlock(Block block, int x, int y, int z) {
        if (block.isSolid()) {
            BlockModel blockModel = block.getBlockModel();
            BlockUV blockUV = block.getBlockUV();

            boolean[] culledFaces = chunkCull.cullFaces(x, y, z);
            if (chunkCull.isVisible(culledFaces)) {
                for (int face = 0; face < blockModel.getNumFaces(); face++) {
                    if (!block.isSpecialRender()) {
                        if (culledFaces[face])
                            createBlock(x, y, z, blockModel, blockUV, face);
                    } else {
                        createBlock(x, y, z, blockModel, blockUV, face);
                    }
                }
            }
        }
    }


    private void createBlock(int x, int y, int z, BlockModel blockModel, BlockUV blockUV, int face) {
        for (int i = 0; i < 4; i++) {
            blockVertices.add(blockModel.get(face, i * 3) + x);
            blockNormals.add(blockModel.getNormals()[face * 3]);
            blockVertices.add(blockModel.get(face, i * 3 + 1) + y);
            blockNormals.add(blockModel.getNormals()[face * 3 + 1]);
            blockVertices.add(blockModel.get(face, i * 3 + 2) + z);
            blockNormals.add(blockModel.getNormals()[face * 3 + 2]);
            blockLighting.add(blockLights[x][y][z]);
            float[] uv;
            if (face >= 6)
                uv = blockUV.getUV(0, i);
            else
                uv = blockUV.getUV(face, i);
            blockUvs.add(uv[0]);
            blockUvs.add(uv[1]);
        }
        int length = blockVertices.size() / 3;
        blockIndices.add(length - 4);
        blockIndices.add(length - 3);
        blockIndices.add(length - 2);
        blockIndices.add(length - 2);
        blockIndices.add(length - 3);
        blockIndices.add(length - 1);
    }


    public void setDirty() {
        this.dirty = true;
        this.ready = false;
    }

    public boolean isReady() {
        return ready;
    }

    public void addNeighbors(Chunk[] neighbors) {
        chunkCull.setNeighbors(neighbors);
        dirty = true;
        ready = false;
        missingNeighbors = chunkCull.isMissingNeighbors();
    }

    public void setBlock(int x, int y, int z, byte blockID) {
        if (x >= 0 && y >= 0 && z >= 0) {
            if (x == 0 || x == 15 || z == 0 || z == 15)
                chunkCull.setNeighborsDirty();
            blocks[x][y][z] = blockID;
            ready = false;
            dirty = true;
        }
    }

    public byte getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    /**
     * Set the light level, can only be from 0 - 16
     */
    public void setLight(int x, int y, int z, int level) {
        blockLights[x][y][z] = level;
        dirty = true;
        ready = false;
    }

    public int getLight(int x, int y, int z) {
        return blockLights[x][y][z];
    }
}
