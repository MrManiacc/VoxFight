package me.jraynor.engine.core.states;

import me.jraynor.engine.asset.internal.AssetMapperImpl;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.GameEngine;
import me.jraynor.engine.core.GameState;
import me.jraynor.engine.core.processes.registration.CreateGameObjectsProcess;
import me.jraynor.engine.core.processes.registration.LoadModulesProcess;
import me.jraynor.engine.core.processes.registration.RegisterBlocksProcess;
import org.apache.logging.log4j.LogManager;

public class PreInitializationState extends GameState {
    private Context context;

    public PreInitializationState() {
        super(LogManager.getLogger());
    }

    @Override
    public void init(GameEngine engine) {
        super.init(engine);
        this.context = engine.createChildContext();
        enqueue(new AssetMapperImpl(context));
        enqueue(new LoadModulesProcess(context));
        enqueue(new RegisterBlocksProcess(context));
        enqueue(new CreateGameObjectsProcess(context));
    }

    protected void finish() {
        engine.changeState(new PostInitializationState());
    }

    public boolean isHibernationAllowed() {
        return false;
    }

    public String getLoggingPhase() {
        return current.getMessage();
    }
}
