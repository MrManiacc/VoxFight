package me.jraynor.core.chunk;

import me.jraynor.core.block.Blocks;
import me.jraynor.core.block.blocks.Block;
import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;
import me.jraynor.core.generation.IGenerator;
import me.jraynor.core.other.Model;
import me.jraynor.core.physics.Body;
import me.jraynor.core.physics.BoxBody;
import me.jraynor.core.physics.PhysicsWorld;
import org.joml.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The chunk is responsible for storing all of the ids of the loaded blocks in a
 * large 3d array. The total capacity for a chunk is 131,072‬ blocks or 32 * 256 * 32
 */
public class Chunk {
    byte[][][] blocks = new byte[16][256][16];
    int[][][] blockLights = new int[16][256][16];
    private Matrix4f transform; //The 3d position needed to render the model
    private Vector2i origin; //The 3d position needed to render the model
    private Model model; //Contains the 3d data needed to render a model
    private boolean dirty = false;//If dirty, then the model needs to be deleted and rebuilt based on the blocks
    private List<Float> blockVertices = new ArrayList<>();
    private List<Float> blockUvs = new ArrayList<>();
    private List<Integer> blockIndices = new ArrayList<>();
    private List<Float> blockLighting = new ArrayList<>();
    private float[] lights;
    private float[] vertices;
    private float[] textureCoordinates;
    private int[] indices;
    private ChunkCull chunkCull;
    private boolean ready = false;//Will be true when the chunk can be rendered
    private AABBf aabBf;
    private boolean culled = false;
    private boolean missingNeighbors = false;
    private BoxBody boxBody;
    private List<Vector3i> physicsBlocks = new ArrayList<>();
    private Map<Vector3i, BoxBody> bodies = new HashMap<>();
    private PhysicsWorld world;

    public Chunk(Vector2i transform, PhysicsWorld physicsWorld) {
        this.world = physicsWorld;
        this.aabBf = new AABBf(new Vector3f(transform.x, 0, transform.y), new Vector3f(transform.x + 16, 256, transform.y + 16));
        this.origin = transform;
        this.transform = new Matrix4f().identity();
        this.transform.translate(transform.x, 0, transform.y);
        this.transform.scale(1, 1, 1);
        this.chunkCull = new ChunkCull(this);
        this.boxBody = new BoxBody(new Vector3f(transform.x, 0, transform.y), new Vector3f(16, 256, 16));
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
                    blockLights[x][y][z] = 3; //30% light to start
                }
    }


    /**
     * This method should be 5 times per second
     */
    public void tick() {
        if (dirty) {
            blockIndices.clear();
            blockLighting.clear();
            blockUvs.clear();
            blockVertices.clear();
            physicsBlocks.clear();
//            model = Model.create();
            for (int x = 0; x < 16; x++)
                for (int y = 0; y < 256; y++)
                    for (int z = 0; z < 16; z++)
                        processBlock(Blocks.getBlock(blocks[x][y][z]), x, y, z);
            propagateBodies();
            if (model == null)
                model = Model.create();
            model.bind(0, 1, 2);
            populateArrays();
            model.createAttribute(0, vertices, 3);
            model.createAttribute(1, textureCoordinates, 2);
            model.createAttribute(2, lights, 1);
            model.createIndexBuffer(indices);
            model.unbind(0, 1, 2);
            dirty = false;
            ready = true;
            chunkCull.updateLighting();
        }
    }

    /**
     * This method will create all of the intractable box bodies withing the chunk
     *
     * @return
     */
    public void propagateBodies() {
        for (int i = 0; i < physicsBlocks.size(); i++) {
            Vector3i pos = physicsBlocks.get(i);
            Vector3f blockPosition = new Vector3f((origin.x) + pos.x - 0.5f, pos.y - 0.5f, (origin.y) + pos.z - 0.5f);
            BoxBody body = new BoxBody(blockPosition, new Vector3f(1));
            body.setActivationState(Body.DISABLE_SIMULATION);
            body.getBody().setUserPointer(new Vector4i(origin.x + pos.x, pos.y, origin.y + pos.z, -69));
            bodies.put(pos, body);

        }
        physicsBlocks.clear();
    }

    public void addBodies(PhysicsWorld physicsWorld) {
        if (bodies != null)
            for (BoxBody body : bodies.values())
                physicsWorld.addBody(body);
    }

    public void removeBodies(PhysicsWorld physicsWorld) {
        if (bodies != null)
            for (BoxBody body : bodies.values())
                physicsWorld.removeBody(body);
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
        this.lights = new float[blockLighting.size()];
        for (int i = 0; i < blockLighting.size(); i++)
            this.lights[i] = (blockLighting.get(i));
    }


    /**
     * =
     * Converts the block into opengl 3d coordinates
     * =
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
                if (y != 0 && !bodies.containsKey(new Vector3i(x, y, z)))
                    physicsBlocks.add(new Vector3i(x, y, z));
                for (int face = 0; face < 6; face++) {
                    if (culledFaces[face]) {
                        for (int i = 0; i < 4; i++) {
                            blockVertices.add(blockModel.get(face, i * 3) + x);
                            blockVertices.add(blockModel.get(face, i * 3 + 1) + y);
                            blockVertices.add(blockModel.get(face, i * 3 + 2) + z);
                            blockLighting.add(blockLights[x][y][z] / 10.0f);
                            float[] uv = blockUV.getUV(face, i);
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
                }
            }
        }
    }

    public AABBf getBounds() {
        return aabBf;
    }

    public void setDirty() {
        this.dirty = true;
        ready = false;
    }

    public boolean isReady() {
        return ready;
    }

    public void addNeighbors(Chunk[] neighbors) {
        chunkCull.setNeighbors(neighbors);
        chunkCull.updateLighting();
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
            if (blockID == 0) {
                Vector3i key = new Vector3i(x, y, z);
                physicsBlocks.remove(key);
                if (bodies.containsKey(key)) {
                    world.removeBody(bodies.get(key));
                    bodies.remove(key);
                }
            }
        }
    }

    public byte getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }


    public Model getModel() {
        return model;
    }

    public boolean isDirty() {
        return dirty;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public Vector2i getOrigin() {
        return origin;
    }

    public boolean isCulled() {
        return culled;
    }

    public void setCulled(boolean culled) {
        this.culled = culled;
    }

    public BoxBody getBox() {
        return boxBody;
    }

    public boolean isMissingNeighbors() {
        return missingNeighbors;
    }
}
