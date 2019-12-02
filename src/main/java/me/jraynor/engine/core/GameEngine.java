package me.jraynor.engine.core;

import me.jraynor.engine.context.Context;
import me.jraynor.engine.window.Window;

/**
 * The game core is the core of the core. It maintains a stack of game states
 */
public interface GameEngine {

    /**
     * Every game engine needs some type of window right?¬
     *
     * @return window instance
     */
    Window getWindow();

    /**
     * Runs the core, which will block the thread.
     * Invalid for a disposed core
     */
    void run(GameState initialState);


    /**
     * Request the core to stop running
     */
    void shutdown();

    /**
     * @return Whether the core is running - this is true from the point run() is called to the point shutdown is complete
     */
    boolean isRunning();

    /**
     * @return The current state of the core
     */
    GameState getCurrentState();


    boolean hasPendingState();

    /**
     * Clears all states, replacing them with newState
     *
     * @param newState
     */
    void changeState(GameState newState);

    /**
     * Creates a context that provides read access to the objects of the core context and can
     * be populated with it's own private objects.
     */
    Context createChildContext();

}
