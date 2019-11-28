package me.jraynor.core.physics;

import me.jraynor.core.world.World;
import org.joml.*;

import java.lang.Math;
import java.text.DecimalFormat;

public class BoxColliders {
    private AABBf[][][] boxes;
    private boolean[][][] activeBoxes;
    private Rayf playerRay;
    private World world;
    private int reach;
    private Vector3i position = new Vector3i();
    private Vector3i selectedBlock = new Vector3i();
    private Vector2f result = new Vector2f();

    public BoxColliders(World world, int reachDistance) {
        this.world = world;
        this.reach = reachDistance * 2;
        this.playerRay = new Rayf();
        this.boxes = new AABBf[reach][reach][reach];
        this.activeBoxes = new boolean[reach][reach][reach];
        for (int x = 0; x < reach; x++) {
            for (int y = 0; y < reach; y++) {
                for (int z = 0; z < reach; z++) {
                    boxes[x][y][z] = new AABBf();
                    activeBoxes[x][y][z] = true;
                }
            }
        }
    }

    public void update(Vector3f pos, Vector3f direction) {
        position.x = Math.round(pos.x);
        position.y = Math.round(pos.y);
        position.z = Math.round(pos.z);
        playerRay.oX = position.x;
        playerRay.oY = position.y;
        playerRay.oZ = position.z;
        playerRay.dX = direction.x;
        playerRay.dY = direction.y;
        playerRay.dZ = direction.z;
        for (int x = 0; x < reach; x++) {
            for (int y = 0; y < reach; y++) {
                for (int z = 0; z < reach; z++) {
                    int xO = (position.x + x) - reach / 2;
                    int yO = (position.y + y) - reach / 2;
                    int zO = (position.z + z) - reach / 2;
                    activeBoxes[x][y][z] = world.getBlock(xO, yO, zO) != 0;
                    if (activeBoxes[x][y][z]) {
                        boxes[x][y][z].minX = xO;
                        boxes[x][y][z].maxX = xO + 1f;

                        boxes[x][y][z].minY = yO;
                        boxes[x][y][z].maxY = yO + 1f;

                        boxes[x][y][z].minZ = zO;
                        boxes[x][y][z].maxZ = zO + 1f;
                        if (boxes[x][y][z].intersectRay(playerRay, result)) {
                            if (result.x > 20) {
                                selectedBlock.x = xO;
                                selectedBlock.y = yO;
                                selectedBlock.z = zO;
                            }
                        }
                    }
                }
            }
        }
    }

    public Vector3i getSelectedBlock() {
        return selectedBlock;
    }
}
