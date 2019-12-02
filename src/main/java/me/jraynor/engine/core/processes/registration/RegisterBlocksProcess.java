package me.jraynor.engine.core.processes.registration;

import me.jraynor.engine.asset.assets.BlockAsset;
import me.jraynor.engine.asset.assets.data.BlockData;
import me.jraynor.engine.block.BlockManager;
import me.jraynor.engine.block.internal.BlockManagerImpl;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.SingleStepLoadProcess;
import me.jraynor.engine.core.processes.StepBasedLoadProcess;
import me.jraynor.engine.module.Module;
import me.jraynor.engine.module.ModuleManager;
import me.jraynor.engine.registry.CoreRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RegisterBlocksProcess extends SingleStepLoadProcess {
    private BlockManager blockManager;
    private ModuleManager moduleManager;
    private Logger logger = LogManager.getLogger();
    private Collection<Module> modules;

    public RegisterBlocksProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Registering Blocks...";
    }

    @Override
    public boolean step() {
        for (Module module : modules) {
            Optional<List<BlockAsset>> blockAssets = module.getAll(BlockAsset.class);
            if (blockAssets.isPresent()) {
                for (BlockAsset blockAsset : blockAssets.get()) {
                    blockAsset.reload(new BlockData());
                }
            }
        }
        return true;
    }

    @Override
    public void begin() {
        blockManager = new BlockManagerImpl();
        CoreRegistry.put(BlockManager.class, blockManager);
        moduleManager = CoreRegistry.get(ModuleManager.class);
        modules = moduleManager.getModules();
        int totalSteps = modules.stream().mapToInt(module -> module.assetCount(BlockAsset.class)).sum();
        logger.debug("found {} total BlockAsset's in {} total modules", totalSteps, moduleManager.moduleCount());
    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
