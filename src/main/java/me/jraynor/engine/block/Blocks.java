package me.jraynor.engine.block;

import me.jraynor.engine.block.blocks.*;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
    private static final Map<Byte, Block> blockMap = new HashMap<>();

    public static void initBlocks() {
        registerBlock(new BlockSand((byte) 3));
        registerBlock(new BlockDirt((byte) 4));
        registerBlock(new BlockDiamond((byte) 5));
        registerBlock(new BlockClay((byte) 6));
        registerBlock(new BlockCoal((byte) 7));
        registerBlock(new BlockWater((byte) 10));
        registerBlock(new BlockBedrock((byte) 11));
    }

    public static void registerBlock(Block block) {
        blockMap.put(block.getId(), block);
    }

    public static Block getBlock(short id) {
        return blockMap.get(id);
    }


}
