package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockSand extends Block {
    public BlockSand(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(2, 1);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
