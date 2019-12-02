package me.jraynor.engine.core.processes;

import me.jraynor.engine.context.Context;

/**
 * A load process that requires a context
 */
public abstract class LoadProcessWithContext implements LoadProcess {
    protected Context context;

    LoadProcessWithContext(Context context) {
        this.context = context;
    }
}
