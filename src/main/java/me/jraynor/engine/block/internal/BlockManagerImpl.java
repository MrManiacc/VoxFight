package me.jraynor.engine.block.internal;


import com.google.common.collect.Maps;
import me.jraynor.engine.asset.urn.BlockUrn;
import me.jraynor.engine.block.BlockManager;
import me.jraynor.engine.block.blocks.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class BlockManagerImpl extends BlockManager {
    private Logger logger = LogManager.getLogger();
    private Map<BlockUrn, Block> blockMappings = Maps.newConcurrentMap();
    private Map<String, Short> blockNameIdMappings = Maps.newConcurrentMap();
    private Map<Short, Block> blockIdMappings = Maps.newConcurrentMap();

    @Override
    public Map<String, Short> getBlockIdMap() {
        return blockNameIdMappings;
    }

    @Override
    public Block getBlock(String uri) {
        return getBlock(blockNameIdMappings.get(uri));
    }

    public void put(BlockUrn blockUrn, Block block) {
        blockMappings.put(blockUrn, block);
        blockNameIdMappings.put(blockUrn.toString(), (short) block.getId());
        blockIdMappings.put((short) block.getId(), block);
        logger.debug("Registered block {} with id {}", blockUrn, block.getId());
    }

    @Override
    public Block getBlock(BlockUrn uri) {
        return blockMappings.get(uri);
    }

    @Override
    public Block getBlock(short id) {
        return blockIdMappings.get(id);
    }

    @Override
    public Collection<BlockUrn> getBlockUrns() {
        return blockMappings.keySet();
    }

    @Override
    public Collection<Block> getBlocks() {
        return blockMappings.values();
    }
}

