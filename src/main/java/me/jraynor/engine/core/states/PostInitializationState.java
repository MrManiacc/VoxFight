package me.jraynor.engine.core.states;

import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.GameEngine;
import me.jraynor.engine.core.GameState;
import me.jraynor.engine.core.processes.registration.CreateWindowProcess;
import me.jraynor.engine.core.processes.registration.InitGameObjectsProcess;
import org.apache.logging.log4j.LogManager;

public class PostInitializationState extends GameState {
    private Context context;

    public PostInitializationState() {
        super(LogManager.getLogger());
    }

    @Override
    public void init(GameEngine engine) {
        super.init(engine);
        this.context = engine.createChildContext(); //The render context
        enqueue(new CreateWindowProcess(context));
        enqueue(new InitGameObjectsProcess(context));
    }

    protected void finish() {
        engine.changeState(new MainGameState());
    }

    public boolean isHibernationAllowed() {
        return false;
    }

    public String getLoggingPhase() {
        return current.getMessage();
    }
}
