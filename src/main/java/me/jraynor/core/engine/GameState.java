/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.jraynor.core.engine;

import com.google.common.collect.Queues;
import me.jraynor.core.context.Context;
import me.jraynor.core.engine.processes.LoadProcess;
import org.apache.logging.log4j.Logger;

import java.util.Queue;

/**
 * @version 0.1
 * <p>
 * A GameState encapsulates a different set of systems and managers being initialized
 * on state change and updated every iteration of the main loop (every frame). Existing
 * GameState implementations do not necessarily represent a state of play.
 * I.e. interacting with the Main Menu is handled through a GameState.
 */
public abstract class GameState {
    protected float progress, maxProgress;
    protected LoadProcess current;
    protected Queue<LoadProcess> loadProcesses = Queues.newArrayDeque();
    private final Logger logger;
    protected Context context;

    public GameState(Logger logger) {
        this.logger = logger;
    }

    public abstract void init(GameEngine engine);

    public abstract void dispose(boolean shuttingDown);

    public void dispose() {
        dispose(false);
    }

    public void handleInput(float delta) {
    }

    protected void enqueue(LoadProcess process) {
        loadProcesses.add(process);
    }

    public abstract void update(float delta);

    public void render() {
    }

    /**
     * @return Whether the game should hibernate when it loses focus
     */
    public abstract boolean isHibernationAllowed();

    /**
     * @return identifies the target for logging events
     */
    public String getLoggingPhase() {
        if (current != null)
            return current.getMessage();
        return "";
    }

    public Context getContext() {
        return context;
    }

    protected void popStep() {
        if (current != null) {
            progress += current.getExpectedCost();
        }
        current = null;
        if (!loadProcesses.isEmpty()) {
            current = loadProcesses.remove();
            logger.info(getLoggingPhase());
            current.begin();
        }
    }

}
