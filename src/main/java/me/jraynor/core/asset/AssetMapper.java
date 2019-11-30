package me.jraynor.core.asset;

import com.google.common.collect.Maps;
import me.jraynor.core.asset.urn.Name;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.context.Context;
import me.jraynor.core.engine.processes.SingleStepLoadProcess;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A class that helps with assets propagation
 */
public abstract class AssetMapper extends SingleStepLoadProcess {
    protected final Map<AssetType, Map<Urn, Path>> assetMapper = Maps.newConcurrentMap();

    public enum AssetType {
        BLOCK, SHADER, FONT, SCRIPT, IMAGE
    }

    public AssetMapper(Context context) {
        super(context);
        Map<Urn, Path> blockAssets = Maps.newConcurrentMap();
        assetMapper.putIfAbsent(AssetType.BLOCK, blockAssets);
        Map<Urn, Path> shaderAssets = Maps.newConcurrentMap();
        assetMapper.putIfAbsent(AssetType.SHADER, shaderAssets);
        Map<Urn, Path> fontsAssets = Maps.newConcurrentMap();
        assetMapper.putIfAbsent(AssetType.FONT, fontsAssets);
        Map<Urn, Path> scriptAssets = Maps.newConcurrentMap();
        assetMapper.putIfAbsent(AssetType.SCRIPT, scriptAssets);
        Map<Urn, Path> imageAssets = Maps.newConcurrentMap();
        assetMapper.putIfAbsent(AssetType.IMAGE, imageAssets);
    }

    /**
     * Populate the maps
     */
    public abstract void populateMaps() throws IOException;

    /**
     * Get the map of the assets
     */
    public Map<Urn, Path> getAssetsMap(AssetType assetType) {
        return assetMapper.get(assetType);
    }

    /**
     * Get a collection of all of the asset maps
     */
    public Collection<Map<Urn, Path>> getAssetMaps() {
        return assetMapper.values();
    }


    /**
     * Get an asset's path given the module id and asset name
     */
    public abstract Path getAssetPath(AssetType assetType, String module, String asset);

    /**
     * Get an asset's path given the module id and asset name
     */
    public abstract Path getAssetPath(AssetType assetType, Name module, Name asset);

    /**
     * Get an asset's path given the urn
     */
    public abstract Path getAssetPath(AssetType assetType, Urn urn);

    /**
     * Get an asset's path given the urn
     *
     * @return
     */
    public abstract Name[] getModuleNames();

    /**
     * Get the iterator for the modules
     *
     * @return module name's iterator
     */
    public abstract Iterator<Name> getModulesIterator();

    /**
     * Get the total number of modules
     *
     * @return
     */
    public abstract int getModulesCount();

}
