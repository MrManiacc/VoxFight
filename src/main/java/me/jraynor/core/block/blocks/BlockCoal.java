package me.jraynor.core.block.blocks;

import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;

public class BlockCoal extends Block {
    public BlockCoal(byte id) {
        super(id);
    }

    @Override
    public  BlockUV createUV() {
        return new BlockUV(7, 2);
    }

    @Override
    public  BlockModel createModel() {
        return new BlockModel();
    }
}
