package me.jraynor.engine.module;

import me.jraynor.engine.asset.assets.Asset;
import me.jraynor.engine.asset.assets.data.AssetData;
import me.jraynor.engine.asset.urn.Name;
import me.jraynor.engine.asset.urn.Urn;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public interface ModuleManager {
    /**
     * Get a module with the specified name
     *
     * @return the module at name
     */
    Module get(Name moduleName);

    default <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> clazz, String urn) {
        return get(clazz, new Urn(urn));
    }

    default <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> clazz, Name urn) {
        return get(clazz, urn.toString());
    }

    /**
     * Directly get an asset from the module mapper
     *
     * @param clazz the type of object to get
     * @param urn   the identifier of the asset to get
     * @return the asset
     */
    <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> clazz, Urn urn);

    /**
     * Get a module with the specified name
     *
     * @return the module at name
     */
    default Module get(String moduleName) {
        return get(new Name(moduleName));
    }

    /**
     * Puts a module into the data storage
     *
     * @param module
     * @return returns true if the list doesn't contain the module name
     */
    boolean put(Module module);

    /**
     * Remove a module at the given name
     *
     * @param moduleName
     * @return
     */
    default Module remove(String moduleName) {
        return remove(new Name(moduleName));
    }


    /**
     * Gets the collection of modules used for populating the blockManager
     *
     * @return modules collection
     */
    Collection<Module> getModules();

    /**
     * Remove a module at the given name
     *
     * @param moduleName
     * @return
     */
    Module remove(Name moduleName);


    /**
     * The current module count
     *
     * @return module count
     */
    int moduleCount();

    /**
     * The module iterator used for going through all of the modules
     *
     * @return module iterator
     */
    Iterator<Module> getIterator();

}
