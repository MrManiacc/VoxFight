package me.jraynor;

import me.jraynor.engine.core.GameEngine;
import me.jraynor.engine.core.VoxelEngine;
import me.jraynor.engine.core.states.PreInitializationState;
import me.jraynor.engine.window.Window;
import me.jraynor.engine.window.internal.WindowGlfw;

public class Voxel {
    public static GameEngine ENGINE;
    public static Window WINDOW;

    public static void main(String[] args) {
        WINDOW = createWindow(args);
        ENGINE = new VoxelEngine(WINDOW);
        ENGINE.run(new PreInitializationState());
    }

    private static Window createWindow(String[] args) {
        if (args.length == 0)
            return new WindowGlfw("Voxel", 1080, 720);
        if (args.length == 1)
            return new WindowGlfw(args[0], 1080, 720);
        if (args.length == 2)
            return new WindowGlfw(args[0], 1080, 720);
        if (args.length == 3)
            return new WindowGlfw(args[0], toInt(args[1]), toInt(args[2]));
        if (args.length == 4)
            return new WindowGlfw(args[0], toInt(args[1]), toInt(args[2]), toBool(args[3]));
        if (args.length == 5)
            return new WindowGlfw(args[0], toInt(args[1]), toInt(args[2]), toBool(args[3]), toBool(args[4]));
        if (args.length == 6)
            return new WindowGlfw(args[0], toInt(args[1]), toInt(args[2]), toBool(args[3]), toBool(args[4]), toBool(args[5]));
        if (args.length == 7)
            return new WindowGlfw(args[0], toInt(args[1]), toInt(args[2]), toBool(args[3]), toBool(args[4]), toBool(args[5]), toBool(args[6]));
        return null;
    }

    private static int toInt(String string) {
        return Integer.parseInt(string);
    }

    private static boolean toBool(String string) {
        return Boolean.parseBoolean(string);
    }
}
