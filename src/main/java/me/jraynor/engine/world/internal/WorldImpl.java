package me.jraynor.engine.world.internal;

import me.jraynor.engine.block.BlockManager;
import me.jraynor.engine.block.blocks.Block;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.world.World;
import me.jraynor.engine.world.chunk.Chunk;
import me.jraynor.engine.world.chunk.Direction;
import me.jraynor.engine.world.chunk.ChunkProvider;
import me.jraynor.engine.world.chunk.internal.ChunkImpl;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class WorldImpl implements World {
    private ChunkProvider chunkProvider;
    public static final int CHUNK_WIDTH = 16, CHUNK_HEIGHT = 256;
    private BlockManager blockManager;

    public WorldImpl(ChunkProvider chunkProvider) {
        this.chunkProvider = chunkProvider;
        this.blockManager = CoreRegistry.get(BlockManager.class);
    }

    /**
     * Gets the chunk at the given coordinates
     *
     * @param x the x of the chunk
     * @param z the y of the chunk
     * @return chunk at position or null
     */
    public Chunk getChunk(int x, int z) {
        return chunkProvider.getChunk(x, z);
    }

    /**
     * Gets the neighbors of the given chunk
     *
     * @param chunk the chunk to get neighbors for
     * @return the chunk's neighbors
     */
    public Map<Direction, Chunk> getNeighbors(Chunk chunk) {
        Map<Direction, Chunk> neighbors = new HashMap<>();
        Chunk north = getNeighbor(chunk, Direction.NORTH);
        if (north != null)
            neighbors.put(Direction.NORTH, north);
        Chunk south = getNeighbor(chunk, Direction.SOUTH);
        if (south != null)
            neighbors.put(Direction.SOUTH, south);
        Chunk east = getNeighbor(chunk, Direction.EAST);
        if (east != null)
            neighbors.put(Direction.EAST, east);
        Chunk west = getNeighbor(chunk, Direction.WEST);
        if (west != null)
            neighbors.put(Direction.WEST, west);
        return neighbors;
    }

    /**
     * Get a chunk's neighbor in the given direction
     *
     * @param x        the chunk x
     * @param z        the chunk z
     * @param neighbor the direction to get a neighbor for
     * @return the chunk neighbor
     */
    public Chunk getNeighbor(int x, int z, Direction neighbor) {
        int nx = x;
        int nz = z;
        switch (neighbor) {
            case EAST:
                nx += CHUNK_WIDTH;
                break;
            case WEST:
                nx -= CHUNK_WIDTH;
                break;
            case NORTH:
                nz += CHUNK_WIDTH;
                break;
            case SOUTH:
                nz -= CHUNK_WIDTH;
                break;
        }
        return getChunk(nx, nz);
    }

    /**
     * Gets the block at the specified block coordinates
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block at position
     */
    public short getBlockID(int x, int y, int z) {
        Vector3i normalizedBlock = toChunkBlock(x, y, z);
        Vector2i normalizedChunk = toChunkOrigin(x, y, z);
        if (isChunkLoaded(normalizedChunk)) {
            Chunk chunk = getChunk(normalizedChunk);
            return chunk.getBlock(normalizedBlock.x, normalizedBlock.y, normalizedBlock.z);
        }
        return 0;
    }


    /**
     * Gets the block at the specified block coordinates
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block at position
     */
    public Block getBlock(int x, int y, int z) {
        return blockManager.getBlock(getBlockID(x, y, z));
    }

    /**
     * Sets a block at the given coordinate
     *
     * @param x    the block x
     * @param y    the block y
     * @param z    the block z
     * @param type the block type to set to
     */
    public void setBlock(int x, int y, int z, short type) {
        setBlock(x, y, z, blockManager.getBlock(type));
    }

    /**
     * Sets a block at the given coordinate
     *
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     * @param block the block type to set to
     */
    public void setBlock(int x, int y, int z, Block block) {
        Vector3i normalizedBlock = toChunkBlock(x, y, z);
        Vector2i normalizedChunk = toChunkOrigin(x, y, z);
        if (isChunkLoaded(normalizedChunk)) {
            Chunk chunk = getChunk(normalizedChunk);
            chunk.setBlock(normalizedBlock.x, normalizedBlock.y, normalizedBlock.z, block.getId());
        }
    }


    /**
     * Checks to see if a the chunk at the given position is loaded
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return chunk loaded status
     */
    public boolean isChunkLoaded(int x, int z) {
        return chunkProvider.isChunkReady(x, z);
    }


    /**
     * Loads the chunk at the given position or reloads the chunk at the position
     * or reloads the chunk at the given position
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    public boolean loadChunk(int x, int z) {
        if (!chunkProvider.hasChunk(x, z)) {
            chunkProvider.queueChunk(new ChunkImpl(x, z));
            return false;
        } else {
            chunkProvider.unloadChunk(x, z);
            chunkProvider.queueChunk(new ChunkImpl(x, z));
            return true;
        }
    }


    /**
     * Unloads the chunk at the given position or reloads the chunk at the position
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    public boolean unloadChunk(int x, int z) {
        if (chunkProvider.hasChunk(x, z)) {
            chunkProvider.unloadChunk(x, z);
            return true;
        }
        return false;
    }


    /**
     * This method will convert a world position into a chunk position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk origin
     */
    public Vector2i toChunkOrigin(int x, int y, int z) {
        int xf = x;
        int zf = z;
        if (xf < 0)
            xf -= (CHUNK_WIDTH - 1);
        if (zf < 0)
            zf -= (CHUNK_WIDTH - 1);
        return new Vector2i(xf - (xf % CHUNK_WIDTH), zf - (zf % CHUNK_WIDTH));
    }

    /**
     * This method will convert a world position into a chunk block position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk block
     */

    public Vector3i toChunkBlock(int x, int y, int z) {
        Vector2i origin = toChunkOrigin(x, y, z);
        int x1 = Math.abs(x) % CHUNK_WIDTH;
        if (origin.x < 0)
            x1 = CHUNK_WIDTH - x1;
        int z1 = Math.abs(z) % CHUNK_WIDTH;
        if (origin.y < 0)
            z1 = CHUNK_WIDTH - z1;
        if (x1 >= CHUNK_WIDTH)
            x1 = 0;

        if (z1 >= CHUNK_WIDTH)
            z1 = 0;
        int y1 = y;

        if (y1 < 0)
            y1 = 0;
        if (y1 > CHUNK_HEIGHT)
            y1 = CHUNK_HEIGHT;
        return new Vector3i(x1, y1, z1);
    }


}
