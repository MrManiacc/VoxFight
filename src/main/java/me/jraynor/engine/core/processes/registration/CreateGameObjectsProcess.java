package me.jraynor.engine.core.processes.registration;

import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.lighting.Pipeline;
import me.jraynor.core.physics.PhysicsWorld;
import me.jraynor.core.physics.PlayerCollider;
import me.jraynor.core.world.World;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.SingleStepLoadProcess;
import me.jraynor.engine.core.states.PreInitializationState;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.registry.utils.Parser;
import me.jraynor.engine.window.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

public class CreateGameObjectsProcess extends SingleStepLoadProcess {
    private Logger logger = LogManager.getLogger(PreInitializationState.class);

    public CreateGameObjectsProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Creating game objects...";
    }

    @Override
    public boolean step() {
        PlayerEntity playerEntity = new PlayerEntity(new Vector3f());
        CoreRegistry.put(PlayerEntity.class, playerEntity);
        logger.debug("Registered the PlayerEntity to the Core Registry");
        World world = new World(playerEntity, "");
        CoreRegistry.put(World.class, world);
        logger.debug("Registered the World to the Core Registry");
        PhysicsWorld physicsWorld = new PhysicsWorld(10, playerEntity, world);
        world.setPhysicsWorld(physicsWorld);
        logger.debug("Registered the PhysicsWorld to the Core Registry");
        CoreRegistry.put(PhysicsWorld.class, physicsWorld);
        PlayerCollider playerCollider = new PlayerCollider(world.getPlayerEntity(), physicsWorld, world);
        CoreRegistry.put(PlayerCollider.class, playerCollider);
        Pipeline pipeline = new Pipeline(CoreRegistry.get(Window.class), world);
        CoreRegistry.put(Pipeline.class, pipeline);
        logger.debug("Registered the Render Pipeline to the Core Registry");
        //TODO: remove this its temp code
//        Parser.parse("core");
        return true;

    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
