package me.jraynor.core.block;


import me.jraynor.core.asset.urn.BlockUrn;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.block.blocks.Block;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public abstract class BlockManager {

    public static final Urn AIR_ID = new BlockUrn("engine:air", (short) 0);
    public static final Urn UNLOADED_ID = new BlockUrn("engine:unloaded", (short) -1);

    /**
     * @return A map of the mapping between Block Uris and Ids
     */
    public abstract Map<String, Short> getBlockIdMap();

    /**
     * Put a block urn into the storage of this class
     */
    public abstract void put(BlockUrn blockUrn, Block block);

    /**
     * @param uri
     * @return Retrieves the Block for the given uri, or null if there isn't one
     */
    public abstract Block getBlock(String uri);

    /**
     * @param uri
     * @return Retrieves the Block for the given uri, or null if there isn't one
     */
    public abstract Block getBlock(BlockUrn uri);

    /**
     * @param id
     * @return Retrieves the Block with the given id, or null if there isn't one
     */
    public abstract Block getBlock(short id);

    /**
     * @return A collection of registered (in use) block uris
     */
    public abstract Collection<BlockUrn> getBlockUrns();


    /**
     * @return A collection of registered blocks
     */
    public abstract Collection<Block> getBlocks();

}

