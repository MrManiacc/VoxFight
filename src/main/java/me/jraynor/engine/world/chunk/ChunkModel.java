package me.jraynor.engine.world.chunk;

import me.jraynor.core.other.Model;

import java.util.Map;

public interface ChunkModel {
    /**
     * Renders the chunk model
     */
    void render();

    /**
     * Gets the chunk model
     *
     * @return chunk model
     */
    Model getModel();

    /**
     * Rebuilds the chunk with the given blocks and neighbors
     *
     * @param blocks    the blocks of the chunk to rebuild
     * @param neighbors the chunk neighbors
     */
    void rebuild(short[][][] blocks, Map<Direction, Chunk> neighbors);
}
