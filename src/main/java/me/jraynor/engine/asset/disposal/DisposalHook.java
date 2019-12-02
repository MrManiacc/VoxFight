package me.jraynor.engine.asset.disposal;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Optional;

public class DisposalHook {
    private Logger logger = LogManager.getLogger();
    private volatile Optional<Runnable> disposeAction = Optional.empty();

    public synchronized void dispose() {
        disposeAction.ifPresent(Runnable::run);
        disposeAction = Optional.empty();
    }

    public void setDisposeAction(Runnable disposeAction) {
        setDisposeAction(Optional.of(disposeAction));
    }

    public void setDisposeAction(Optional<Runnable> disposeAction) {
        if (disposeAction.isPresent()) {
            Class<? extends Runnable> actionType = disposeAction.get().getClass();
            if ((actionType.isLocalClass() || actionType.isAnonymousClass() || actionType.isMemberClass()) && !Modifier.isStatic(actionType.getModifiers())) {
                logger.warn("Non-static anonymous or member class should not be registered as the disposal hook - this will block garbage collection enqueuing for disposal");
            }
        }
        this.disposeAction = disposeAction;
    }
}
