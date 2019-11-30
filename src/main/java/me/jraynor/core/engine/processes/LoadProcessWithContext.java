package me.jraynor.core.engine.processes;

import me.jraynor.core.context.Context;

/**
 * A load process that requires a context
 */
public abstract class LoadProcessWithContext implements LoadProcess {
    protected Context context;

    LoadProcessWithContext(Context context) {
        this.context = context;
    }
}
