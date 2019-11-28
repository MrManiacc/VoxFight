package me.jraynor.core.registry.assets;

import lombok.Getter;
import me.jraynor.core.block.Blocks;
import me.jraynor.core.block.blocks.Block;
import me.jraynor.core.block.props.BlockModel;
import me.jraynor.core.block.props.BlockUV;
import org.joml.Vector3f;

import static me.jraynor.core.registry.utils.Data.*;

import java.io.File;

public class BlockAsset extends Asset implements IYamlAsset<BlockData> {
    private BlockData blockData;

    public BlockAsset(File file, String pack, String name) {
        super(file, pack, name);
    }

    @Override
    public void parseAsset(File file) {
        blockData = parseYaml(file, BlockData.class);
    }

    @Override
    public void load() {
        Blocks.registerBlock(new Block(blockData.id) {
            @Override
            protected void initialize() {
                solid = blockData.isSolid();
                hardness = blockData.getHardness();
                halfExtents = new Vector3f(blockData.getBoundingSize()[0], blockData.getBoundingSize()[1], blockData.getBoundingSize()[2]);
                specialRender = blockData.vertices.length != 0;

                if (specialRender) {
                    float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
                    float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

                    for (int i = 0; i < blockData.vertices.length; i++) {
                        for (int j = 0; j < blockData.vertices[0].length; j += 3) {
                            if (blockData.vertices[i][j] < minX)
                                minX = blockData.vertices[i][j];
                            if (blockData.vertices[i][j + 1] < minY)
                                minY = blockData.vertices[i][j + 1];
                            if (blockData.vertices[i][j + 2] < minZ)
                                minZ = blockData.vertices[i][j + 2];
                            if (blockData.vertices[i][j] > maxX)
                                maxX = blockData.vertices[i][j];
                            if (blockData.vertices[i][j + 1] > maxY)
                                maxY = blockData.vertices[i][j + 1];
                            if (blockData.vertices[i][j + 2] > maxZ)
                                maxZ = blockData.vertices[i][j + 2];
                        }
                    }

                    halfExtents.x = (Math.abs(minX) + maxX) / 2.0f;
                    halfExtents.y = (Math.abs(minY) + maxY) / 2.0f;
                    halfExtents.z = (Math.abs(minZ) + maxZ) / 2.0f;
                }


            }

            protected BlockUV createUV() {
                if (blockData.faces.length == 1) {
                    return new BlockUV(blockData.faces[0][0], blockData.faces[0][1]);
                }
                return new BlockUV(blockData.getRows(), blockData.getColumns());
            }

            protected BlockModel createModel() {
                if (blockData.vertices.length == 0)
                    return new BlockModel();
                else
                    return new BlockModel(blockData.vertices);
            }
        });
        super.load();
    }
}
