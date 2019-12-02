package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

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
