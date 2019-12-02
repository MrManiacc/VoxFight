package me.jraynor.engine.core;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import me.jraynor.engine.context.Context;
import me.jraynor.engine.context.internal.ContextImpl;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.time.EngineTime;
import me.jraynor.engine.time.internal.TimeImpl;
import me.jraynor.engine.window.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class VoxelEngine implements GameEngine {
    @Getter
    private GameState currentState;
    @Getter
    private GameState pendingState;
    @Getter
    private boolean running = false;
    @Getter
    private Context rootContext;
    @Getter
    private EngineTime engineTime;
    @Getter
    private boolean shutdownRequested;
    @Getter
    private Window window;

    private Logger logger = LogManager.getLogger();

    public VoxelEngine(Window window) {
        this.window = window;
    }

    public synchronized void run(GameState initialState) {
        init();
        changeState(initialState);
        mainLoop();
        running = true;
    }

    private void mainLoop() {
        while (tick()) {
            currentState.render();
        }
    }

    /**
     * Init core systems like the context and engine time
     */
    private void init() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Stopwatch totalInitTime = Stopwatch.createStarted();
        {
            logEnvironmentInfo();
            rootContext = new ContextImpl();
            CoreRegistry.setContext(rootContext);
            CoreRegistry.put(GameEngine.class, this);
            CoreRegistry.put(Window.class, window);
            engineTime = new TimeImpl();
            CoreRegistry.put(EngineTime.class, engineTime);
        }
        double seconds = 0.001 * totalInitTime.elapsed(TimeUnit.MILLISECONDS);
        logger.debug("Initialization completed in {}sec.", String.format("%.2f", seconds));

    }

    /**
     * Logs software, environment and hardware information.
     */
    private void logEnvironmentInfo() {
        logger.info("Java: {} in {}", System.getProperty("java.version"), System.getProperty("java.home"));
        logger.info("Java VM: {}, version: {}", System.getProperty("java.vm.name"), System.getProperty("java.vm.version"));
        logger.info("OS: {}, arch: {}, version: {}", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
        logger.info("Max. Memory: {} MiB", Runtime.getRuntime().maxMemory() / 1024 * 1024);
        logger.info("Processors: {}", Runtime.getRuntime().availableProcessors());
    }


    private boolean tick() {
        if (shutdownRequested) {
            return false;
        }
        processPendingState();
        if (currentState == null) {
            shutdown();
            return false;
        }

        Iterator<Float> updateCycles = engineTime.tick();
        while (updateCycles.hasNext())
            currentState.update(updateCycles.next());


        return true;
    }

    /**
     * Changes the game state, i.e. to switch from the MainMenu to Ingame via Loading screen
     * (each is a GameState). The change can be immediate, if there is no current game
     * state set, or scheduled, when a current state exists and the new state is stored as
     * pending. That been said, scheduled changes occurs in the main loop through the call
     * processStateChanges(). As such, from a user perspective in normal circumstances,
     * scheduled changes are likely to be perceived as immediate.
     */
    public void changeState(GameState newState) {
        if (currentState != null) {
            pendingState = newState;    // scheduled change
        } else {
            switchState(newState);      // immediate change
        }
    }

    private void switchState(GameState newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        currentState = newState;
        newState.init(this);
        newState.postInit();
    }

    private void processPendingState() {
        if (pendingState != null) {
            switchState(pendingState);
            pendingState = null;
        }
    }


    @Override
    public Context createChildContext() {
        return new ContextImpl(rootContext);
    }

    /**
     * Causes the main loop to stop at the end of the current frame, cleanly ending
     * the current GameState, all running task threads and disposing subsystems.
     */
    public void shutdown() {
        shutdownRequested = true;
    }

    public boolean hasPendingState() {
        return pendingState != null;
    }


}
