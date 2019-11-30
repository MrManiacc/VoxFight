package me.jraynor.core.engine;

import me.jraynor.core.context.Context;

/**
 * The game engine is the core of the engine. It maintains a stack of game states
 */
public interface GameEngine {
    /**
     * Runs the engine, which will block the thread.
     * Invalid for a disposed engine
     */
    void run(GameState initialState);


    /**
     * Request the engine to stop running
     */
    void shutdown();

    /**
     * @return Whether the engine is running - this is true from the point run() is called to the point shutdown is complete
     */
    boolean isRunning();

    /**
     * @return The current state of the engine
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
     * Creates a context that provides read access to the objects of the engine context and can
     * be populated with it's own private objects.
     */
    Context createChildContext();

}
