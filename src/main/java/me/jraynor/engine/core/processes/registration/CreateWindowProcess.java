package me.jraynor.engine.core.processes.registration;

import me.jraynor.engine.context.Context;
import me.jraynor.engine.core.processes.SingleStepLoadProcess;
import me.jraynor.engine.registry.CoreRegistry;
import me.jraynor.engine.window.Window;

public class CreateWindowProcess extends SingleStepLoadProcess {

    public CreateWindowProcess(Context context) {
        super(context);
    }

    @Override
    public String getMessage() {
        return "Creating window....";
    }

    @Override
    public boolean step() {
        CoreRegistry.get(Window.class).createWindow();
        return true;
    }

    @Override
    public int getExpectedCost() {
        return 1;
    }
}
