package me.jraynor.core.world;

import me.jraynor.core.chunk.Chunk;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.generation.GenericGenerator;
import me.jraynor.core.generation.IGenerator;
import me.jraynor.core.physics.PhysicsWorld;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is as container around a group of chunks
 * It should be able to be loaded from file and saved to file.
 * It contains the level data including where blocks are located
 */
public class World {
    private Map<Vector2i, Chunk> chunks = new HashMap<>();
    private WorldRenderer worldRenderer;
    private PlayerEntity playerEntity;
    private String file;
    private int culledChunks;
    private List<Chunk> missingNeighbors = new ArrayList<>();
    private PhysicsWorld physicsWorld;

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
        fixChunks(genericGenerator);
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
                        Chunk chunk = new Chunk(origin, physicsWorld);
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
        int xf = x;
        int zf = z;
        if (xf < 0)
            xf -= 15;
        if (zf < 0)
            zf -= 15;
        Vector2i origin = new Vector2i(xf - (xf % 16), zf - (zf % 16));
        if (chunks.containsKey(origin)) {
            int x1 = Math.abs(x) % 16;
            if (origin.x < 0) {
                x1 = 16 - x1;
            }
            int z1 = Math.abs(z) % 16;

            if (origin.y < 0) {
                z1 = 16 - z1;
            }
            if (x1 >= 16) {
                x1 = 0;
            }
            if (z1 >= 16) {
                z1 = 0;
            }
            if (y >= 256) {
                return;
            }
            System.out.println(x1 + ", " + y + ", " + z1 + origin.toString(DecimalFormat.getNumberInstance()));

            Chunk chunk = chunks.get(origin);
            chunk.setBlock(x1, y, z1, block);
            playerEntity.setNeedsUpdate(true);
        }
    }

    public byte getBlock(int x, int y, int z) {
        int xf = x;
        int zf = z;
        if (xf < 0)
            xf -= 15;
        if (zf < 0)
            zf -= 15;
        Vector2i origin = new Vector2i(xf - (xf % 16), zf - (zf % 16));
        if (chunks.containsKey(origin)) {
            int x1 = Math.abs(x) % 16;
            if (origin.x < 0) {
                x1 = 15 - x1;
            }
            int z1 = Math.abs(z) % 16;

            if (origin.y < 0) {
                z1 = 15 - z1;
            }

            if (y >= 128)
                return 0;
            if (y <= 0)
                return 0;
            Chunk chunk = chunks.get(origin);
            return chunk.getBlock(x1, y, z1);
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

    private void fixChunks(IGenerator generator) {
        for (Chunk chunk : missingNeighbors) {
            Chunk[] neighbors = getNeighbors(chunk.getOrigin());
            Vector2i origin = chunk.getOrigin();

            Vector2i westChunk = new Vector2i(origin.x - 16, origin.y);
            if (!chunks.containsKey(westChunk)) {
                Chunk west = new Chunk(westChunk, physicsWorld);
                west.generateChunk(generator);
                neighbors[0] = west;
            } else {
                neighbors[0] = chunks.get(westChunk);
            }
            Vector2i eastChunk = new Vector2i(origin.x + 16, origin.y);
            if (!chunks.containsKey(eastChunk)) {
                Chunk east = new Chunk(eastChunk, physicsWorld);
                east.generateChunk(generator);
                neighbors[1] = east;
            } else {
                neighbors[1] = chunks.get(eastChunk);
            }
            Vector2i northChunk = new Vector2i(origin.x, origin.y + 16);
            if (!chunks.containsKey(northChunk)) {
                Chunk north = new Chunk(northChunk, physicsWorld);
                north.generateChunk(generator);
                neighbors[2] = north;
            } else {
                neighbors[2] = chunks.get(northChunk);
            }
            Vector2i southChunk = new Vector2i(origin.x, origin.y - 16);
            if (!chunks.containsKey(southChunk)) {
                Chunk south = new Chunk(southChunk, physicsWorld);
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

    public void render() {
        worldRenderer.start(playerEntity.getCamera());
        for (Chunk chunk : chunks.values())
            worldRenderer.render(chunk);
        worldRenderer.stop();
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public int getCulledChunks() {
        return culledChunks;
    }

    public boolean hasChunk(Vector2i chunk) {
        return chunks.containsKey(chunk);
    }

    public void update(float v) {
        playerEntity.update(v);
    }
}
