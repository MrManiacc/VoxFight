package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

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
