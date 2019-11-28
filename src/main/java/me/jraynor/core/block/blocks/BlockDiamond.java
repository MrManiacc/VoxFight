package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockDiamond extends Block {
    public BlockDiamond(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(5, 1);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
