package me.jraynor.engine.world;

import me.jraynor.engine.block.blocks.Block;
import me.jraynor.engine.world.chunk.Chunk;
import me.jraynor.engine.world.chunk.Direction;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Map;

public interface World {
    /**
     * Gets the chunk at the given coordinates
     *
     * @param x the x of the chunk
     * @param z the y of the chunk
     * @return chunk at position or null
     */
    Chunk getChunk(int x, int z);

    /**
     * Gets the chunk at the given coordinates
     *
     * @param chunk the origin or the chunk
     * @return chunk at position or null
     */
    default Chunk getChunk(Vector2i chunk) {
        return getChunk(chunk.x, chunk.y);
    }

    /**
     * Gets the neighbors of the given chunk
     *
     * @param chunk the chunk to get neighbors for
     * @return the chunk's neighbors
     */
    Map<Direction, Chunk> getNeighbors(Chunk chunk);

    /**
     * Get a chunk's neighbor in the given direction
     *
     * @param chunk    the chunk to get neighbors for
     * @param neighbor the direction to get a neighbor for
     * @return the chunk neighbor
     */
    default Chunk getNeighbor(Chunk chunk, Direction neighbor) {
        return getNeighbor(chunk.origin().x, chunk.origin().y, neighbor);
    }

    /**
     * Get a chunk's neighbor in the given direction
     *
     * @param x        the chunk x
     * @param z        the chunk z
     * @param neighbor the direction to get a neighbor for
     * @return the chunk neighbor
     */
    Chunk getNeighbor(int x, int z, Direction neighbor);


    /**
     * Gets the neighbors of the given chunk
     *
     * @param x chunk x to get neighbors for
     * @param z chunk z to get neighbors for
     * @return the chunk's neighbors
     */
    default Map<Direction, Chunk> getNeighbors(int x, int z) {
        return getNeighbors(getChunk(x, z));
    }

    /**
     * Gets the neighbors of the given chunk
     *
     * @param chunk the chunk orgin to get neighbors for
     * @return the chunk's neighbors
     */
    default Map<Direction, Chunk> getNeighbors(Vector2i chunk) {
        return getNeighbors(getChunk(chunk));
    }

    /**
     * Gets the block at the specified block coordinates
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block at position
     */
    short getBlockID(int x, int y, int z);

    /**
     * Gets the block at the specified block coordinates
     *
     * @param block the block x, y, and z coordinate
     * @return the block at position
     */
    default short getBlockID(Vector3i block) {
        return getBlockID(block.x, block.y, block.z);
    }

    /**
     * Gets the block at the specified block coordinates
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block at position
     */
    Block getBlock(int x, int y, int z);

    /**
     * Gets the block at the specified block coordinates
     *
     * @param block the block x, y, and z coordinate
     * @return the block at position
     */
    default Block getBlock(Vector3i block) {
        return getBlock(block.x, block.y, block.z);
    }

    /**
     * Sets a block at the given coordinate
     *
     * @param x    the block x
     * @param y    the block y
     * @param z    the block z
     * @param type the block type to set to
     */
    void setBlock(int x, int y, int z, short type);

    /**
     * Sets a block at the given coordinate
     *
     * @param block the block x, y, and z coordinate
     * @param type  the block type to set to
     */
    default void setBlock(Vector3i block, short type) {
        setBlock(block.x, block.y, block.z, type);
    }

    /**
     * Sets a block at the given coordinate
     *
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     * @param block the block type to set to
     */
    void setBlock(int x, int y, int z, Block block);

    /**
     * Sets a block at the given coordinate
     *
     * @param block the block x, y, and z coordinate
     * @param block the block type to set to
     */
    default void setBlock(Vector3i block, Block type) {
        setBlock(block.x, block.y, block.z, type);
    }

    /**
     * Checks to see if a the chunk at the given position is loaded
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return chunk loaded status
     */
    boolean isChunkLoaded(int x, int z);

    /**
     * Checks to see if a the chunk at the given position is loaded
     *
     * @param chunk the chunk x and z
     * @return chunk loaded status
     */
    default boolean isChunkLoaded(Vector2i chunk) {
        return isChunkLoaded(chunk.x, chunk.y);
    }

    /**
     * Loads the chunk at the given position or reloads the chunk at the position
     * or reloads the chunk at the given position
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return returns true if reloaded or false if loaded for the first time
     */
    boolean loadChunk(int x, int z);

    /**
     * Loads the chunk at the given position or reloads the chunk at the position
     * or reloads the chunk at the given position
     *
     * @param chunk the chunk x and z
     * @return returns true if reloaded or false if loaded for the first time
     */
    default boolean loadChunk(Vector2i chunk) {
        return loadChunk(chunk.x, chunk.y);
    }

    /**
     * Unloads the chunk at the given position or reloads the chunk at the position
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return returns true if unloaded or false if not unloaded due to no chunk at the position
     */
    boolean unloadChunk(int x, int z);

    /**
     * Unloads the chunk at the given position or reloads the chunk at the position
     *
     * @param chunk the chunk x and z
     * @return returns true if unloaded or false if not unloaded due to no chunk at the position
     */
    default boolean unloadChunk(Vector2i chunk) {
        return unloadChunk(chunk.x, chunk.y);
    }

    /**
     * This method will convert a world position into a chunk position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk origin
     */
    Vector2i toChunkOrigin(int x, int y, int z);

    /**
     * This method will convert a world position into a chunk position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk origin
     */
    default Vector2i toChunkOrigin(float x, float y, float z) {
        return toChunkOrigin((int) x, (int) y, (int) z);
    }


    /**
     * This method will convert a world position into a chunk position
     *
     * @param position the world position
     * @return the normalized chunk origin
     */
    default Vector2i toChunkOrigin(Vector3i position) {
        return toChunkOrigin(position.x, position.y, position.z);
    }

    /**
     * This method will convert a world position into a chunk position
     *
     * @param position the world position that is cast to a int array
     * @return the normalized chunk origin
     */
    default Vector2i toChunkOrigin(Vector3f position) {
        return toChunkOrigin(position.x, position.y, position.z);
    }


    /**
     * This method will convert a world position into a chunk block position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk block
     */
    Vector3i toChunkBlock(int x, int y, int z);

    /**
     * This method will convert a world position into a chunk block position
     *
     * @param x the world x position
     * @param y the world y position
     * @param z the world z position
     * @return the normalized chunk block
     */
    default Vector3i toChunkBlock(float x, float y, float z) {
        return toChunkBlock((int) x, (int) y, (int) z);
    }


    /**
     * This method will convert a world position into a chunk block position
     *
     * @param position the world position
     * @return the normalized chunk block
     */
    default Vector3i toChunkBlock(Vector3i position) {
        return toChunkBlock(position.x, position.y, position.z);
    }

    /**
     * This method will convert a world position into a chunk block position
     *
     * @param position the world position that is cast to a int array
     * @return the normalized chunk block
     */
    default Vector3i toChunkBlock(Vector3f position) {
        return toChunkBlock(position.x, position.y, position.z);
    }
}
