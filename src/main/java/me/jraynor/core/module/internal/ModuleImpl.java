package me.jraynor.core.module.internal;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.jraynor.core.asset.assets.Asset;
import me.jraynor.core.asset.assets.data.AssetData;
import me.jraynor.core.asset.urn.Name;
import me.jraynor.core.asset.urn.Urn;
import me.jraynor.core.module.Module;

import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Immutable
public class ModuleImpl implements Module {
    private final Map<Class<? extends Asset>, Map<Urn, Asset<? extends AssetData>>> assets = new MapMaker().concurrencyLevel(6).makeMap();
    @Getter
    private Name moduleName;

    public ModuleImpl(Name moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Gets an asset with the specified type and should do type casting to allow for
     * a direct reference to the asset
     *
     * @param type
     * @param urn
     * @return returns the type casted Asset based on the input class
     * @usage Optional<BlockAsset> blockAsset = get(BlockAsset.class, urn);
     */
    public final synchronized <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> type, Urn urn) {
        if (assets.containsKey(type)) {
            Map<Urn, Asset<? extends AssetData>> assetMap = assets.get(type);
            if (assetMap.containsKey(urn))
                return Optional.of(type.cast(assetMap.get(urn)));
            return Optional.empty();
        }
        return Optional.empty();
    }

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
    public synchronized boolean put(Asset<? extends AssetData> asset) {
        Class<? extends Asset> cls = asset.getClass();
        Map<Urn, Asset<? extends AssetData>> assetMap;
        if (assets.containsKey(cls))
            assetMap = assets.get(cls);
        else
            assetMap = Maps.newConcurrentMap();
        if (!assetMap.containsKey(asset.getUrn())) {
            assetMap.putIfAbsent(asset.getUrn(), asset);
            assets.putIfAbsent(cls, assetMap);
            return true;
        }
        return false;
    }

    /**
     * puts a group of assets into their respective places
     *
     * @param assets the assets to put put into a list for
     * @return returns the number of assets that were put into the list
     */
    public final synchronized int putAll(List<Asset<? extends AssetData>> assets) {
        int total = 0;
        for (Asset<? extends AssetData> asset : assets) {
            put(asset);
            total++;
        }
        return total;
    }


    /**
     * Attempts to get the collection of assets based upon the given class
     * also does some type casting and converts to a list
     *
     * @param clazz the type of asset you which to get a collection for
     */
    public final synchronized <T extends Asset<U>, U extends AssetData> Optional<List<T>> getAll(Class<T> clazz) {
        if (assets.containsKey(clazz)) {
            Collection<? extends Asset> assetCollection = assets.get(clazz).values();
            return Optional.of(assetCollection.stream()
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    /**
     * Reloads all of the assets with the specified type
     *
     * @return the list of reloaded assets, optional will be empty if the list doesnt exist
     * and the return can be ignored, more of a convenience thing
     */
    public final synchronized <T extends Asset<U>, U extends AssetData> Optional<List<T>> reloadAll(Class<T> clazz) {
        Optional<List<T>> assets = getAll(clazz);
        if (assets.isPresent()) {
            assets.get().forEach(Asset::reload);
            return assets;
        }
        return Optional.empty();
    }

    /**
     * Disposes of all of the assets, will also count how many assets are disposed
     *
     * @param clazz the class type to target for disposal
     * @return the total number of disposed assets, -1 if the asset doesn't exist
     */
    public final synchronized <T extends Asset<U>, U extends AssetData> int disposeAll(Class<T> clazz) {
        Optional<List<T>> optAssets = getAll(clazz);
        if (optAssets.isPresent()) {
            var assets = optAssets.get();
            AtomicInteger totalDisposed = new AtomicInteger();
            totalDisposed.set(0);
            assets.forEach(asset -> {
                asset.dispose();
                totalDisposed.getAndIncrement();
            });
            this.assets.remove(clazz);
            return totalDisposed.get();
        }
        return -1;
    }

    @Override
    public <T extends Asset<U>, U extends AssetData> int assetCount(Class<T> clazz) {
        return assets.get(clazz).size();
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), moduleName, this.assets);
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj.getClass() != obj.getClass())
            return false;
        if ((obj instanceof ModuleImpl)) {
            var module = (ModuleImpl) obj;
            return (module.getModuleName().equals(this.moduleName) && module.assets.equals(this.assets));
        }
        return false;
    }
}
