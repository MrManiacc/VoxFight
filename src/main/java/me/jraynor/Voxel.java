package me.jraynor;

import me.jraynor.core.engine.GameEngine;
import me.jraynor.core.engine.VoxelEngine;
import me.jraynor.core.engine.states.StateLoading;

public class Voxel {
    public static final GameEngine INSTANCE = new VoxelEngine();

    public static void main(String[] args) {
        INSTANCE.run(new StateLoading());
    }
}
