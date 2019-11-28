package me.jraynor.core.block.blocks;

import lombok.Getter;
import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;
import org.joml.Vector3f;

/**
 * Meant o represent a block in a static manor.
 * Every block should have an ID and be able to be accessed statically
 */
public abstract class Block {
    @Getter
    private BlockUV blockUV;
    @Getter
    private BlockModel blockModel;
    @Getter
    protected boolean solid;
    @Getter
    protected float hardness;
    @Getter
    private byte id;
    @Getter
    protected Vector3f halfExtents;
    @Getter
    protected boolean specialRender;

    public Block(byte id) {
        this.id = id;
        this.blockUV = createUV();
        this.blockModel = createModel();
        this.hardness = 0.0f;
        this.halfExtents = new Vector3f(0.5f, 0.5f, 0.5f);
        solid = true;
        initialize();
    }

    protected void initialize() {
    }

    protected abstract BlockUV createUV();

    protected abstract BlockModel createModel();




}
