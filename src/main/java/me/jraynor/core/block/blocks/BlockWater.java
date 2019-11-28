package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockWater extends Block {

    public BlockWater(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(13, 12);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
