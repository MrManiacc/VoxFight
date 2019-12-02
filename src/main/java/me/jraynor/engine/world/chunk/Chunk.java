package me.jraynor.engine.world.chunk;

import me.jraynor.core.other.Model;
import org.joml.Vector2i;

import java.util.Map;

public interface Chunk {
    /**
     * Gets the block in the position
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block in the given position
     */
    short getBlock(int x, int y, int z);

    /**
     * Sets the block to the type at the given position
     *
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     * @param block the block type to set
     */
    void setBlock(int x, int y, int z, short block);

    /**
     * Gets the chunk model
     *
     * @return the chunk model
     */
    ChunkModel getModel();

    /**
     * Generate the chunk
     */
    void generate();

    /**
     * Builds the chunk model from the blocks
     */
    void rebuild(Map<Direction, Chunk> neighbors);

    /**
     * The chunk origin
     *
     * @return the chunk origin
     */
    Vector2i origin();

    /**
     * get if the model is dirty
     *
     * @return
     */
    boolean isDirty();

    /**
     * sets the chunk to dirty which will cause the model to be rebuilt
     */
    void setDirty();

}

