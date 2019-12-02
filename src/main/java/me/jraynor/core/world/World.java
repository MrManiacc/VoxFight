package me.jraynor.core.world;

import lombok.Getter;
import me.jraynor.engine.block.Blocks;
import me.jraynor.engine.block.blocks.Block;
import me.jraynor.core.chunk.Chunk;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.generation.GenericGenerator;
import me.jraynor.core.generation.IGenerator;
import me.jraynor.core.lighting.Light;
import me.jraynor.core.physics.PhysicsWorld;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.*;

/**
 * This class is as container around a group of chunks
 * It should be able to be loaded from file and saved to file.
 * It contains the level data including where blocks are located
 */
public class World {
    private Map<Vector2i, Chunk> chunks = new HashMap<>();
    @Getter
    private WorldRenderer worldRenderer;
    private PlayerEntity playerEntity;
    private String file;
    private int culledChunks;
    private List<Chunk> missingNeighbors = new ArrayList<>();
    private PhysicsWorld physicsWorld;
    @Getter
    private Light[] lights = new Light[32];
    private int nextIndex = 0;
    private Map<Vector3i, int[]> lightMap = new HashMap<>();

    //TODO: load a level from file
    public World(PlayerEntity playerEntity, String file) {
        this.playerEntity = playerEntity;
        this.file = file;
        this.worldRenderer = new WorldRenderer();
        playerEntity.setWorld(this);
    }

    public void init(String pack) {
        GenericGenerator genericGenerator = new GenericGenerator();
        generateChunk(new Vector2i(0, 0), genericGenerator, 30);
        cullChunks();
        generateNeighbors(genericGenerator);
        missingNeighbors.clear();
        worldRenderer.init(pack);
    }

    public void setPhysicsWorld(PhysicsWorld physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    /**
     * This method will start the generation
     */
    private void generateChunk(Vector2i center, IGenerator generator, int radius) {
        if (!chunks.containsKey(center)) {
            for (int x = center.x - ((radius / 2) * 16); x < center.x + ((radius / 2) * 16); x += 16) {
                for (int z = center.y - ((radius / 2) * 16); z < center.y + ((radius / 2) * 16); z += 16) {
                    Vector2i origin = new Vector2i(x, z);
                    if (!chunks.containsKey(origin)) {
                        Chunk chunk = new Chunk(origin);
                        chunk.generateChunk(generator);
                        chunks.put(origin, chunk);
                    }
                }
            }
        }
    }

    /**
     * Simple wrapper method for using vector3i's instead of int values
     *
     * @param position
     * @param block
     */
    public void setBlock(Vector3i position, byte block) {
        setBlock(position.x, position.y, position.z, block);
    }


    /**
     * Sets a block at the given position in world space
     * by first converting the absolute position into a chunk
     * relative position
     */
    public void setBlock(int x, int y, int z, byte block) {
        Vector2i origin = calcChunk(x, y, z);
        if (chunks.containsKey(origin)) {
            Vector3i blockPos = calcBlock(x, y, z);
            Chunk chunk = chunks.get(origin);
            chunk.setBlock(blockPos.x, blockPos.y, blockPos.z, block);
            playerEntity.setChunkUpdate(true);
            Block blk = Blocks.getBlock(block);
            if (blk.isLight()) {
                setLight(x, y, z, blk.getLightColor(), blk.getQuadratic(), blk.getLightLinear());
            }

            if (block == 0 && lightMap.containsKey(new Vector3i(x, y, z))) {
                for (int index : lightMap.get(new Vector3i(x, y, z))) {
                    lights[index].setPosition(new Vector3f(0, -9999, 0));
                    lights[index].setColor(new Vector3f(0, 0, 0));
                }
            }
        }
    }

    /**
     * Calculates the proper chunk relative block coordniates given a world position
     *
     * @param x world x
     * @param y world y
     * @param z world z
     * @return the chunk space block position
     */
    private Vector3i calcBlock(int x, int y, int z) {
        Vector2i origin = calcChunk(x, y, z);
        int x1 = Math.abs(x) % 16;
        if (origin.x < 0)
            x1 = 16 - x1;
        int z1 = Math.abs(z) % 16;
        if (origin.y < 0)
            z1 = 16 - z1;
        if (x1 >= 16)
            x1 = 0;

        if (z1 >= 16)
            z1 = 0;
        int y1 = y;

        if (y1 < 0)
            y1 = 0;
        if (y1 > 255)
            y1 = 255;

        return new Vector3i(x1, y1, z1);
    }

    private Vector2i calcChunk(int x, int y, int z) {
        int xf = x;
        int zf = z;
        if (xf < 0)
            xf -= 15;
        if (zf < 0)
            zf -= 15;
        return new Vector2i(xf - (xf % 16), zf - (zf % 16));
    }

    public byte getBlock(Vector3i block) {
        return getBlock(block.x, block.y, block.z);
    }

    public byte getBlock(int x, int y, int z) {
        Vector2i origin = calcChunk(x, y, z);
        if (chunks.containsKey(origin)) {
            Vector3i blockPos = calcBlock(x, y, z);
            Chunk chunk = chunks.get(origin);
            return chunk.getBlock(blockPos.x, blockPos.y, blockPos.z);
        }
        return 0;
    }

    public void setLight(int x, int y, int z, Vector3f color, float quadratic, float linear) {
        int[] indices = new int[4];
        if (nextIndex == 31) {
            nextIndex = 0;
        }
        indices[0] = nextIndex;
        lights[nextIndex++] = new Light(new Vector3f(x + 0.75f, y + 0.75f, z + 0.75f), color, linear, quadratic);
        if (nextIndex == 31)
            nextIndex = 0;
        indices[1] = nextIndex;
        lights[nextIndex++] = new Light(new Vector3f(x - 0.75f, y + 0.75f, z - 0.75f), color, linear, quadratic);
        if (nextIndex == 31)
            nextIndex = 0;
        indices[2] = nextIndex;
        lights[nextIndex++] = new Light(new Vector3f(x - 0.75f, y + 0.75f, z + 0.75f), color, linear, quadratic);
        if (nextIndex == 31)
            nextIndex = 0;
        indices[3] = nextIndex;
        lights[nextIndex++] = new Light(new Vector3f(x + 0.75f, y + 0.75f, z - 0.75f), color, linear, quadratic);
        if (nextIndex == 31)
            nextIndex = 0;

        lightMap.put(new Vector3i(x, y, z), indices);
    }

    /**
     * Gets the light in world space
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public int getLight(int x, int y, int z) {
        Vector2i origin = calcChunk(x, y, z);
        if (chunks.containsKey(origin)) {
            Vector3i blockPos = calcBlock(x, y, z);
            Chunk chunk = chunks.get(origin);
            return chunk.getLight(blockPos.x, blockPos.y, blockPos.z);
        }
        return 0;
    }


    /**
     * Adds the neighbors to the chunks
     */
    private void cullChunks() {
        for (Vector2i origin : chunks.keySet()) {
            Chunk[] neighbors = getNeighbors(origin);
            chunks.get(origin).addNeighbors(neighbors);
            if (chunks.get(origin).isMissingNeighbors()) {
                missingNeighbors.add(chunks.get(origin));
            }
        }
    }

    private void generateNeighbors(IGenerator generator) {
        for (Chunk chunk : missingNeighbors) {
            Chunk[] neighbors = getNeighbors(chunk.getOrigin());
            Vector2i origin = chunk.getOrigin();

            Vector2i westChunk = new Vector2i(origin.x - 16, origin.y);
            if (!chunks.containsKey(westChunk)) {
                Chunk west = new Chunk(westChunk);
                west.generateChunk(generator);
                neighbors[0] = west;
            } else {
                neighbors[0] = chunks.get(westChunk);
            }
            Vector2i eastChunk = new Vector2i(origin.x + 16, origin.y);
            if (!chunks.containsKey(eastChunk)) {
                Chunk east = new Chunk(eastChunk);
                east.generateChunk(generator);
                neighbors[1] = east;
            } else {
                neighbors[1] = chunks.get(eastChunk);
            }
            Vector2i northChunk = new Vector2i(origin.x, origin.y + 16);
            if (!chunks.containsKey(northChunk)) {
                Chunk north = new Chunk(northChunk);
                north.generateChunk(generator);
                neighbors[2] = north;
            } else {
                neighbors[2] = chunks.get(northChunk);
            }
            Vector2i southChunk = new Vector2i(origin.x, origin.y - 16);
            if (!chunks.containsKey(southChunk)) {
                Chunk south = new Chunk(southChunk);
                south.generateChunk(generator);
                neighbors[3] = south;
            } else {
                neighbors[3] = chunks.get(southChunk);
            }

            chunk.addNeighbors(neighbors);
            if (!chunk.isMissingNeighbors()) {
                missingNeighbors.remove(chunk);
            }
        }
        cullChunks();
    }

    /**
     * Gets the neighbors relative to a specific chunk
     *
     * @param origin the chunk to find neighbors for
     * @return the neighboring chunks
     */
    private Chunk[] getNeighbors(Vector2i origin) {
        Vector2i westChunk = new Vector2i(origin.x - 16, origin.y);
        Vector2i eastChunk = new Vector2i(origin.x + 16, origin.y);
        Vector2i northChunk = new Vector2i(origin.x, origin.y + 16);
        Vector2i southChunk = new Vector2i(origin.x, origin.y - 16);
        return new Chunk[]{chunks.get(westChunk), chunks.get(eastChunk), chunks.get(northChunk), chunks.get(southChunk)};
    }

    /**
     * Converts a 3d position into a 2d chunk origin position
     *
     * @param position
     * @return
     */
    public Chunk getChunk(Vector3f position) {
        int x = (int) position.x;
        int z = (int) position.z;
        if (x < 0)
            x -= 16;
        if (z < 0)
            z -= 16;
        int xO = Math.round(x - (x % 16));
        int zO = Math.round(z - (z % 16));
        Vector2i origin = new Vector2i(xO, zO);
        return chunks.get(origin);
    }


    public Chunk getChunk(Vector2i origin) {
        return chunks.get(origin);
    }


    public void tick(float deltaTime) {
        culledChunks = 0;
        for (Chunk chunk : chunks.values()) {
            if (!playerEntity.chunkInFrustum(chunk)) {
                chunk.setCulled(true);
                culledChunks++;
            } else {
                chunk.setCulled(false);
                chunk.tick();
            }
        }
    }


    public Collection<Chunk> getChunks() {
        return chunks.values();
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public int getCulledChunks() {
        return culledChunks;
    }


    public void update(float v) {
        playerEntity.update(v);
    }


}
