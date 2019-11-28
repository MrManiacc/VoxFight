package me.jraynor.core.generation;

import org.joml.Vector2i;

public interface IGenerator {
    int getSeed();

    byte generatorBlock(Vector2i origin, int x, int y, int z);

}

