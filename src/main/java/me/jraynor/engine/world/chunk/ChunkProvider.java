package me.jraynor.engine.world.chunk;

import org.joml.Vector3i;

import java.util.Collection;

public interface ChunkProvider {
    /**
     * Queues a chunk for generation
     *
     * @param chunk the chunk to queue
     * @return returns the current queue position
     */
    int queueChunk(Chunk chunk);

    /**
     * Checks if the chunk is ready to be rendered
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    boolean isChunkReady(int x, int z);

    /**
     * Whether not not the chunk is in the queue or in the chunk map
     *
     * @param x chunk x
     * @param z chunk z
     * @return if chuck is contained
     */
    boolean hasChunk(int x, int z);

    /**
     * gets the chunk at the position
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    Chunk getChunk(int x, int z);

    /**
     * @return returns the chunk that is being processed
     */
    Chunk getProcessing();

    /**
     * Should just remove the chunk from the chunk list or equivalent
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return if the chunk was removed or not
     */
    boolean unloadChunk(int x, int z);

    /**
     * @return returns all of the loaded chunks
     */
    Collection<Chunk> getChunks();

    /**
     * This method will generate the next chunk in the queue
     */
    void step();
}
