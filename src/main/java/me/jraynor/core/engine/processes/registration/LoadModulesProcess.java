package me.jraynor.core.engine.processes.registration;

import me.jraynor.core.asset.AssetMapper;
import me.jraynor.core.asset.assets.BlockAsset;
import me.jraynor.core.asset.assets.TextureAsset;
import me.jraynor.core.asset.urn.Name;
import me.jraynor.core.context.Context;
import me.jraynor.core.engine.processes.StepBasedLoadProcess;
import me.jraynor.core.module.ModuleManager;
import me.jraynor.core.module.internal.ModuleImpl;
import me.jraynor.core.module.internal.ModuleManagerImpl;
import me.jraynor.core.registry.CoreRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

import static me.jraynor.core.asset.AssetMapper.AssetType.BLOCK;
import static me.jraynor.core.asset.AssetMapper.AssetType.IMAGE;

public class LoadModulesProcess extends StepBasedLoadProcess {
    private Iterator<Name> moduleIterator;
    private AssetMapper assetMapper;
    private ModuleManager moduleManager;

    private static final Logger logger = LogManager.getLogger();

    public LoadModulesProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Creating modules...";
    }

    @Override
    public void begin() {
        assetMapper = context.get(AssetMapper.class);
        moduleIterator = assetMapper.getModulesIterator();
        moduleManager = new ModuleManagerImpl();
        CoreRegistry.put(ModuleManager.class, moduleManager);
        setTotalSteps(assetMapper.getModulesCount());
    }


    @Override
    public boolean step() {
        if (moduleIterator.hasNext()) {
            Name module = moduleIterator.next();
            if (!buildModule(module))
                logger.warn("{} was overwritten!", module.toLowerCase());
            stepDone();
        }
        return !moduleIterator.hasNext();
    }

    /**
     * Build the module and store it
     *
     * @param moduleName
     */
    private boolean buildModule(Name moduleName) {
        var module = new ModuleImpl(moduleName);
        if (moduleManager.put(module)) {
            assetMapper.getAssetsMap(BLOCK).forEach((urn, path) -> {
                module.put(new BlockAsset(urn));
                logger.debug("[{}, {}] new BlockAsset added to module '{}'", urn.toString(), path.toString(), moduleName.toLowerCase());
            });
            assetMapper.getAssetsMap(IMAGE).forEach((urn, path) -> {
                module.put(new TextureAsset(urn));
                logger.debug("[{}, {}] new TextureAsset added to module '{}'", urn.toString(), path.toString(), moduleName.toLowerCase());
            });
            //TODO: add other asset types!
            return true;
        }
        return false;
    }


    @Override
    public int getExpectedCost() {
        return 1;
    }


}
