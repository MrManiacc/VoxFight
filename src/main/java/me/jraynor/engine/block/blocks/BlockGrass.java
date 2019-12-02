package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockGrass extends Block {
    public BlockGrass(byte id) {
        super(id);
    }

    @Override
    public BlockUV createUV() {
        return new BlockUV(new int[]{3, 3, 3, 3, 0, 2}, new int[]{0, 0, 0, 0, 0, 0});
    }

    @Override
    public BlockModel createModel() {
        return new BlockModel();
    }
}
