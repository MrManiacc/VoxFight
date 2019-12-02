package me.jraynor.engine.module;

import me.jraynor.engine.asset.assets.Asset;
import me.jraynor.engine.asset.assets.data.AssetData;
import me.jraynor.engine.asset.urn.Name;
import me.jraynor.engine.asset.urn.Urn;

import java.util.List;
import java.util.Optional;

/**
 * Represents a collection of assets, which is represented by a module
 */
public interface Module {

    Name getModuleName();

    /**
     * Gets an asset with the specified type and should do type casting to allow for
     * a direct reference to the asset
     *
     * @param type
     * @param urn
     * @return returns the type casted Asset based on the input class
     * @usage Optional<BlockAsset> blockAsset = get(BlockAsset.class, urn);
     */
    <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> type, Urn urn);

    /**
     * Puts the asset into the correct map by getting the class type of the asset
     * and then storing it into the a map, which is generated if null, with the
     * urn inside the asset as a key, and the value being the asset it's self
     * and the
     *
     * @param asset the asset to be stored in the corresponding map
     * @usage var blockAsset = new BlockAsset(); //pseudo code
     * put(blockAsset);
     */
    boolean put(Asset<? extends AssetData> asset);

    /**
     * puts a group of assets into their respective places
     *
     * @param assets the assets to put put into a list for
     * @return returns the number of assets that were put into the list
     */
    int putAll(List<Asset<? extends AssetData>> assets);

    /**
     * Attempts to get the collection of assets based upon the given class
     * also does some type casting and converts to a list
     *
     * @param clazz the type of asset you which to get a collection for
     * @return
     */
    <T extends Asset<U>, U extends AssetData> Optional<List<T>> getAll(Class<T> clazz);


    /**
     * Reloads all of the assets with the specified type
     *
     * @return the list of reloaded assets, optional will be empty if the list doesnt exist
     * and the return can be ignored, more of a convenience thing
     */
    <T extends Asset<U>, U extends AssetData> Optional<List<T>> reloadAll(Class<T> clazz);


    /**
     * Disposes of all of the assets, will also count how many assets are disposed
     * and
     *
     * @param clazz the class type to target for disposal
     * @return the total number of disposed assets, -1 if the asset doesn't exist
     */
    <T extends Asset<U>, U extends AssetData> int disposeAll(Class<T> clazz);

    /**
     * The total number of assets for a given type
     *
     * @return
     */
    <T extends Asset<U>, U extends AssetData> int assetCount(Class<T> clazz);
}
