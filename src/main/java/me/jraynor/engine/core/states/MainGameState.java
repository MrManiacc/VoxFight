package me.jraynor.engine.core.states;

import me.jraynor.core.lighting.Pipeline;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.GameEngine;
import me.jraynor.engine.core.GameState;
import me.jraynor.engine.core.processes.render.PhysicsProcess;
import me.jraynor.engine.core.processes.render.WorldProcess;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.window.Window;
import org.apache.logging.log4j.LogManager;

public class MainGameState extends GameState {
    private Window window;
    private Pipeline pipeline;
    private PhysicsProcess physicsProcess;
    private WorldProcess worldProcess;

    public MainGameState() {
        super(LogManager.getLogger());
    }

    public void init(GameEngine engine) {
        super.init(engine);
        window = CoreRegistry.get(Window.class);
        pipeline = CoreRegistry.get(Pipeline.class);
        physicsProcess = new PhysicsProcess(context);
        physicsProcess.begin();
        worldProcess = new WorldProcess(context);
        worldProcess.begin();
    }

    public void update(float delta) {
        worldProcess.step();
        physicsProcess.step();
    }

    public void render() {
        pipeline.render();
        window.update();
    }

    public void dispose(boolean shuttingDown) {

    }

    public boolean isHibernationAllowed() {
        return false;
    }
}
