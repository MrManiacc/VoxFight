package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockTnt extends Block {
    BlockTnt(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(4, 4);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
