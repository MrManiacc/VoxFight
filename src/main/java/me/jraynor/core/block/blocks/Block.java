package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

/**
 * Meant o represent a block in a static manor.
 * Every block should have an ID and be able to be accessed statically
 */
public abstract class Block {
    private BlockUV blockUV;
    private BlockModel blockModel;
    protected boolean solid;
    protected float hardness;
    private byte id;

    Block(byte id) {
        this.id = id;
        this.blockUV = createUV();
        this.blockModel = createModel();
        this.hardness = 0.0f;
        solid = true;
    }

    abstract BlockUV createUV();

    abstract BlockModel createModel();

    public BlockUV getBlockUV() {
        return blockUV;
    }

    public BlockModel getBlockModel() {
        return blockModel;
    }

    public boolean isSolid() {
        return solid;
    }

    public float getHardness() {
        return hardness;
    }

    public byte getId() {
        return id;
    }
}
