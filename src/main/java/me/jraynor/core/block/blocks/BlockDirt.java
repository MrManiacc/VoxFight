package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockDirt extends Block {
    public BlockDirt(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(2, 0);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
