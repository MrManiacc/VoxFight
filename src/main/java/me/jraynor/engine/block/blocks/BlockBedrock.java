package me.jraynor.engine.block.blocks;

import me.jraynor.engine.block.props.BlockModel;
import me.jraynor.engine.block.props.BlockUV;

public class BlockBedrock extends Block {
    public BlockBedrock(byte id) {
        super(id);
        solid = true;
        hardness = 1.0f;
    }

    @Override
    public  BlockUV createUV() {
        return new BlockUV(2, 14);
    }

    @Override
    public  BlockModel createModel() {
        return new BlockModel();
    }
}
