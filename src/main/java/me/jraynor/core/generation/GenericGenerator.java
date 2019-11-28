package me.jraynor.core.generation;

import org.joml.Vector2i;

import java.util.Random;

public class GenericGenerator implements IGenerator {
    private FastNoise fastNoise;
    private Random random = new Random();

    public GenericGenerator() {
        this.fastNoise = new FastNoise(getSeed());
    }

    @Override
    public int getSeed() {
        return random.nextInt(102002);
    }

    @Override
    public byte generatorBlock(Vector2i origin, int x, int y, int z) {
        double maxY = 256 * fastNoise.GetPerlinFractal(origin.x + x, origin.y + z);
        if (y == 0)
            return 11;
        if (y <= 4) {
            return 1; //dirt
        }
        if (y == 5)
            return 8;
        if (y == Math.round(maxY) + 1) {
            if (random.nextInt(200) == 69)
                return 9;
        } else if (y == Math.round(maxY)) {
            return 8;
        }

        if (y >= maxY)
            return 0;
        if (y < 6) {
            return 4; //dirt
        }
        return 1; //cobblestone
    }
}
