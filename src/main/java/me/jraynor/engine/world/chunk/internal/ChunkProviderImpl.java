package me.jraynor.engine.world.chunk.internal;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import me.jraynor.engine.world.chunk.Chunk;
import me.jraynor.engine.world.chunk.ChunkProvider;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

public class ChunkProviderImpl implements ChunkProvider {
    private final Queue<Chunk> chunkQueue = Queues.newSynchronousQueue();
    private final Map<Vector2i, Chunk> chunkMap = Maps.newConcurrentMap();
    private final Logger logger = LogManager.getLogger();
    private Chunk currentChunk;

    /**
     * Queues a chunk for generation
     *
     * @param chunk the chunk to queue
     * @return returns the current queue position or -1 if the chunk couldn't be queued
     */
    public synchronized int queueChunk(Chunk chunk) {
        if (chunkQueue.add(chunk)) {
            logger.debug("Queued chunk at: " + chunk.origin().x + ", " + chunk.origin().y);
            return chunkQueue.size();
        }
        return -1;
    }

    /**
     * Checks if the chunk is ready to be rendered
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    public synchronized boolean isChunkReady(int x, int z) {
        return chunkMap.containsKey(new Vector2i(x, z));
    }

    /**
     * Whether not not the chunk is in the queue or in the chunk map
     *
     * @param x chunk x
     * @param z chunk z
     * @return if chuck is contained
     */
    public boolean hasChunk(int x, int z) {
        for (Chunk chunk : chunkQueue) {
            if (chunk.origin().x == x && chunk.origin().y == z)
                return true;
        }
        return chunkMap.containsKey(new Vector2i(x, z));
    }

    /**
     * gets the chunk at the position
     *
     * @param x the chunk x
     * @param z the chunk z
     */
    public synchronized Chunk getChunk(int x, int z) {
        return chunkMap.get(new Vector2i(x, z));
    }

    /**
     * Should just remove the chunk from the chunk list or equivalent
     *
     * @param x the chunk x
     * @param z the chunk z
     * @return if the chunk was removed or not
     */
    public synchronized boolean unloadChunk(int x, int z) {
        Vector2i origin = new Vector2i(x, z);
        if (chunkMap.containsKey(origin)) {
            chunkMap.remove(origin);
            return true;
        }
        for (Chunk chunk : chunkQueue)
            if (chunk.origin().x == x && chunk.origin().y == z) {
                chunkQueue.remove(chunk);
                return true;
            }
        return false;
    }

    /**
     * This method will generate the next chunk in the queue
     */
    public synchronized void step() {
        currentChunk = chunkQueue.poll();
        if (currentChunk != null) {
            //TODO: generate chunk here
            currentChunk.generate();
            chunkMap.put(currentChunk.origin(), currentChunk);
            currentChunk = null;
        }
    }

    public Chunk getProcessing() {
        return currentChunk;
    }

    /**
     * @return returns all of the loaded chunks
     */
    public Collection<Chunk> getChunks() {
        return chunkMap.values();
    }
}
