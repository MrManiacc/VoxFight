package me.jraynor.engine.core.processes.registration;

import me.jraynor.core.lighting.Pipeline;
import me.jraynor.core.physics.PhysicsWorld;
import me.jraynor.core.world.World;
import me.jraynor.engine.block.Blocks;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.SingleStepLoadProcess;
import me.jraynor.engine.core.states.PostInitializationState;
import me.jraynor.engine.registry.CoreRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class InitGameObjectsProcess extends SingleStepLoadProcess {
    private Logger logger = LogManager.getLogger(PostInitializationState.class);

    public InitGameObjectsProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Initializing game objects...";
    }

    @Override
    public boolean step() {
        Blocks.initBlocks();//TODO: remove this its bad!

        CoreRegistry.get(Pipeline.class).init();
        logger.debug("Initialized the pipeline");
        CoreRegistry.get(World.class).init("core");
        logger.debug("Initialized the world");
        CoreRegistry.get(PhysicsWorld.class).init();
        logger.debug("Initialized the physics world");
        return true;
    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
