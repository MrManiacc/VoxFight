package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockGlowstone extends Block {
    public BlockGlowstone(byte id) {
        super(id);
    }

    @Override
    public  BlockUV createUV() {
        return new BlockUV(9,6);
    }

    @Override
    public   BlockModel createModel() {
        return new BlockModel();
    }
}
