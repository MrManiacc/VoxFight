package me.jraynor.engine.world.chunk.internal;

import lombok.Getter;
import me.jraynor.engine.world.chunk.Chunk;
import me.jraynor.engine.world.chunk.ChunkModel;
import me.jraynor.engine.world.chunk.Direction;
import me.jraynor.engine.world.internal.WorldImpl;
import org.joml.Vector2i;

import java.util.Map;

public class ChunkImpl implements Chunk {
    private short[][][] blocks = new short[WorldImpl.CHUNK_WIDTH][WorldImpl.CHUNK_HEIGHT][WorldImpl.CHUNK_WIDTH];
    @Getter
    private boolean dirty;
    @Getter
    private ChunkModel model;

    private Vector2i origin;

    public ChunkImpl(int x, int z) {
        this.origin = new Vector2i(x, z);
        this.dirty = false;
        this.model = new ChunkModelImpl(this);
        setAllBlocks((short) 0);
    }

    /**
     * Set all of the blocks
     *
     * @param block
     */
    public void setAllBlocks(short block) {
        for (int y = 0; y < WorldImpl.CHUNK_HEIGHT; y++)
            for (int x = 0; x < WorldImpl.CHUNK_WIDTH; x++)
                for (int z = 0; z < WorldImpl.CHUNK_WIDTH; z++)
                    blocks[x][y][z] = block;
    }

    /**
     * Generate the blocks
     */
    public void generate() {
        for (int y = 0; y < WorldImpl.CHUNK_HEIGHT; y++)
            for (int x = 0; x < WorldImpl.CHUNK_WIDTH; x++)
                for (int z = 0; z < WorldImpl.CHUNK_WIDTH; z++) {
                    if (y == 0)
                        blocks[x][y][z] = 1;//TODO: actually do something with this instad of just making ground level 1
                }
    }

    /**
     * Gets the block in the position
     *
     * @param x the block x
     * @param y the block y
     * @param z the block z
     * @return the block in the given position
     */
    public short getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    /**
     * Sets the block to the type at the given position
     *
     * @param x     the block x
     * @param y     the block y
     * @param z     the block z
     * @param block the block type to set
     */
    public void setBlock(int x, int y, int z, short block) {
        blocks[x][y][z] = block;
    }

    /**
     * Builds the chunk model from the blocks
     */
    public void rebuild(Map<Direction, Chunk> neighbors) {
        if (dirty) {
            model.rebuild(blocks, neighbors);
            dirty = false;
        }
    }

    public Vector2i origin() {
        return origin;
    }

    public void setDirty() {
        this.dirty = true;
    }
}
