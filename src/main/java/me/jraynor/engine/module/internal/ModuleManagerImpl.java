package me.jraynor.engine.module.internal;

import com.google.common.collect.Maps;
import me.jraynor.engine.asset.assets.Asset;
import me.jraynor.engine.asset.assets.data.AssetData;
import me.jraynor.engine.asset.urn.Name;
import me.jraynor.engine.asset.urn.Urn;
import me.jraynor.engine.module.Module;
import me.jraynor.engine.module.ModuleManager;

import javax.annotation.concurrent.Immutable;
import java.util.*;

@Immutable
public class ModuleManagerImpl implements ModuleManager {
    private final Map<Name, Module> moduleMap = Maps.newConcurrentMap();

    /**
     * Get a module with the specified name
     *
     * @return the module at name
     */
    public final synchronized Module get(Name moduleName) {
        return moduleMap.get(moduleName);
    }

    @Override
    public <T extends Asset<U>, U extends AssetData> Optional<T> get(Class<T> clazz, Urn urn) {
        Module module = get(urn.getModuleName());
        if (module == null)
            return Optional.empty();
        return module.get(clazz, urn);
    }

    /**
     * Puts a module into the data storage
     *
     * @param module
     * @return returns true if the list doesn't contain the module name
     */
    public final synchronized boolean put(Module module) {
        if (moduleMap.containsKey(module.getModuleName()))
            return false;
        moduleMap.putIfAbsent(module.getModuleName(), module);
        return true;
    }

    /**
     * Remove a module at the given name
     *
     * @param moduleName
     * @return
     */
    public Module remove(Name moduleName) {
        return moduleMap.remove(moduleName);
    }

    /**
     * The current module count
     *
     * @return module count
     */
    public int moduleCount() {
        return moduleMap.size();
    }

    /**
     * The module iterator used for going through all of the modules
     *
     * @return module iterator
     */
    public Iterator<Module> getIterator() {
        return moduleMap.values().iterator();
    }

    /**
     * Gets the collection of modules used for populating the blockManager
     *
     * @return modules collection
     */
    public Collection<Module> getModules() {
        return moduleMap.values();
    }

    /**
     * List module names for toString
     *
     * @return
     */
    public String toString() {
        return Arrays.toString(moduleMap.keySet().toArray(new Name[0]));
    }
}
