package me.jraynor.core.block;

import me.jraynor.core.block.blocks.*;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
    private static final Map<Byte, Block> blockMap = new HashMap<>();

    public static void initBlocks() {
        registerBlock(new BlockAir((byte) 0));
        registerBlock(new BlockStone((byte) 1));
        registerBlock(new BlockCobble((byte) 2));
        registerBlock(new BlockSand((byte) 3));
        registerBlock(new BlockDirt((byte) 4));
        registerBlock(new BlockDiamond((byte) 5));
        registerBlock(new BlockClay((byte) 6));
        registerBlock(new BlockCoal((byte) 7));
        registerBlock(new BlockGrass((byte) 8));
        registerBlock(new BlockGlowstone((byte) 9));
        registerBlock(new BlockWater((byte) 10));
        registerBlock(new BlockBedrock((byte) 11));
    }

    public static void registerBlock(Block block) {
        blockMap.put(block.getId(), block);
    }

    public static Block getBlock(byte id) {
        return blockMap.get(id);
    }


}
