package me.jraynor.core.engine.processes.registration;

import me.jraynor.core.asset.assets.BlockAsset;
import me.jraynor.core.block.BlockManager;
import me.jraynor.core.block.internal.BlockManagerImpl;
import me.jraynor.core.context.Context;
import me.jraynor.core.engine.processes.StepBasedLoadProcess;
import me.jraynor.core.module.Module;
import me.jraynor.core.module.ModuleManager;
import me.jraynor.core.registry.CoreRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class RegisterBlocksProcess extends StepBasedLoadProcess {
    private BlockManager blockManager;
    private ModuleManager moduleManager;
    private Logger logger = LogManager.getLogger();

    public RegisterBlocksProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Registering Blocks...";
    }

    @Override
    public boolean step() {
        return true;
    }

    @Override
    public void begin() {
        blockManager = new BlockManagerImpl();
        CoreRegistry.put(BlockManager.class, blockManager);
        moduleManager = CoreRegistry.get(ModuleManager.class);
        Collection<Module> modules = moduleManager.getModules();
        int totalSteps = modules.stream().mapToInt(module -> module.assetCount(BlockAsset.class)).sum();
        logger.debug("found {} total BlockAsset's in {} total modules", totalSteps, moduleManager.moduleCount());
        setTotalSteps(totalSteps);
    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
