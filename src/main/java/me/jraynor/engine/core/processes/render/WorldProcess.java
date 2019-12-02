package me.jraynor.engine.core.processes.render;

import me.jraynor.core.lighting.Pipeline;
import me.jraynor.core.world.World;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.StepBasedLoadProcess;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.time.EngineTime;

public class WorldProcess extends StepBasedLoadProcess {
    private World world;
    private EngineTime engineTime;

    public WorldProcess(Context context) {
        super(context);
    }

    public String getMessage() {
        return null;
    }


    public void begin() {
        world = CoreRegistry.get(World.class);
        engineTime = CoreRegistry.get(EngineTime.class);
    }


    public boolean step() {
        world.update(engineTime.getGameDelta());
        world.tick(engineTime.getGameDelta());
        return true;
    }

    @Override

    public int getExpectedCost() {
        return 10;
    }
}
