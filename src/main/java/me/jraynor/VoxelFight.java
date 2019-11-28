package me.jraynor;

import me.jraynor.bootstrap.IEngine;
import me.jraynor.bootstrap.Window;
import me.jraynor.core.block.Blocks;
import me.jraynor.core.entity.PlayerEntity;
import me.jraynor.core.physics.PhysicsWorld;
import me.jraynor.core.physics.PlayerCollider;
import me.jraynor.core.world.World;
import me.jraynor.ui.MainController;
import me.jraynor.uison.UIMaster;
import me.jraynor.uison.misc.Input;
import org.joml.Vector3f;

public class VoxelFight extends IEngine {
    private final Window window;
    private World world;
    private PhysicsWorld physicsWorld;
    private PlayerCollider playerCollider;

    private VoxelFight(int width, int height, String title) {
        super(20D);
        window = new Window(width, height, false, false, false, title);
        PlayerEntity playerEntity = new PlayerEntity(window, new Vector3f(0, 30, 0), 4);
        physicsWorld = new PhysicsWorld(7f, playerEntity);
        world = new World(playerEntity, "");
        world.setPhysicsWorld(physicsWorld);
        playerCollider = new PlayerCollider(world.getPlayerEntity(), physicsWorld, world);
        window.start(this);
    }

    public void postInit() {
        UIMaster.createUIMaster(window, new MainController(window, world));
        Blocks.initBlocks();
        world.init();
    }

    public void renderUI(float v) {
        UIMaster.update(window);
    }

    @Override
    public void render(float delta) {
        world.render();
    }

    public void tick(float tps) {
        playerCollider.tick();
    }

    public void update(float v) {
        physicsWorld.update(v);
        playerCollider.update();
        world.update(v);
        world.tick(v);
        Input.globalMouse();
    }


    public static void main(String[] args) {
        switch (args.length) {
            case 1:
                new VoxelFight(Integer.parseInt(args[0]), Integer.parseInt(args[0]), "VoxelFight");
                break;
            case 2:
                new VoxelFight(Integer.parseInt(args[0]), Integer.parseInt(args[1]), "VoxelFight");
                break;
            default:
                new VoxelFight(1080, 720, "VoxelFight");
        }
    }
}
