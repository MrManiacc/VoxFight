package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockCobble extends Block {
    public BlockCobble(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(2, 2);
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
