package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockStone extends Block {
    public BlockStone(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(0, 1);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
