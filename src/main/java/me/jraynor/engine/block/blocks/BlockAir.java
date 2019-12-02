package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockAir extends Block {
    public BlockAir(byte id) {
        super(id);
        solid = false;
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(0, 0);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
