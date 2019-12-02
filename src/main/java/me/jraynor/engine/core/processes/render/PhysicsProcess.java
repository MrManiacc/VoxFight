package me.jraynor.engine.core.processes.render;

import me.jraynor.core.physics.PhysicsWorld;
import me.jraynor.core.physics.PlayerCollider;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.SingleStepLoadProcess;
import me.jraynor.engine.core.processes.StepBasedLoadProcess;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.time.EngineTime;

public class PhysicsProcess extends SingleStepLoadProcess {
    private PhysicsWorld physicsWorld;
    private PlayerCollider playerCollider;
    private EngineTime engineTime;

    public PhysicsProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Stepping physics...";
    }


    @Override
    public void begin() {
        physicsWorld = CoreRegistry.get(PhysicsWorld.class);
        playerCollider = CoreRegistry.get(PlayerCollider.class);
        engineTime = CoreRegistry.get(EngineTime.class);
    }


    @Override
    public boolean step() {
        physicsWorld.update(engineTime.getGameDelta());
        playerCollider.tick();
        playerCollider.update();
        return true;
    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
