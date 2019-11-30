package me.jraynor.core.engine.states;

import me.jraynor.core.asset.internal.AssetMapperImpl;
import me.jraynor.core.context.Context;
import me.jraynor.core.engine.GameEngine;
import me.jraynor.core.engine.GameState;
import me.jraynor.core.engine.processes.LoadProcess;
import me.jraynor.core.engine.processes.registration.LoadModulesProcess;
import me.jraynor.core.engine.processes.registration.RegisterBlocksProcess;
import me.jraynor.core.registry.CoreRegistry;
import me.jraynor.core.time.EngineTime;
import org.apache.logging.log4j.LogManager;

import java.util.Objects;

public class StateLoading extends GameState {
    private Context context;

    public StateLoading() {
        super(LogManager.getLogger());
    }

    @Override
    public void init(GameEngine engine) {
        this.context = engine.createChildContext();
        initClient();

        progress = 0;
        maxProgress = 0;
        for (LoadProcess process : loadProcesses) {
            maxProgress += process.getExpectedCost();
        }


        popStep();
    }

    @Override
    public void dispose(boolean shuttingDown) {
    }

    private void initClient() {
        enqueue(new AssetMapperImpl(context));
        enqueue(new LoadModulesProcess(context));
        enqueue(new RegisterBlocksProcess(context));
    }

    @Override
    public void update(float delta) {
        GameEngine gameEngine = CoreRegistry.get(GameEngine.class);
        EngineTime time = CoreRegistry.get(EngineTime.class);
        long startTime = Objects.requireNonNull(time).getRealTimeInMs();
        assert gameEngine != null;
        while (current != null && time.getRealTimeInMs() - startTime < 20 && !gameEngine.hasPendingState()) {
            if (current.step()) {
                popStep();
            }
        }
    }

    @Override
    public boolean isHibernationAllowed() {
        return false;
    }

    @Override
    public String getLoggingPhase() {
        return current.getMessage();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
